package com.autotestplatform.codehandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

import com.autotestplatform.entities.*;
import com.autotestplatform.repos.*;

@Service
public class CodeFileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public void saveCodeFile(CodeFileEntity codeFile) {
        fileRepository.save(codeFile);
        saveFileToSystem(codeFile);
    }

    public void saveProject(ProjectEntity project) {
        projectRepository.save(project);
    }

    public List<CodeFileEntity> getAllCodeFiles() {
        return fileRepository.findAll();
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<ProjectEntity> getProjectsByUserId(String userId) {
        return projectRepository.findByUserId(userId);
    }

    public ProjectEntity getProjectByIdAndUserId(Long projectId, String userId) {
        return projectRepository.findByIdAndUserId(projectId, userId).orElse(null);
    }

    public CodeFileEntity getCodeFileByIdAndUserId(Long fileId, String userId) {
        return fileRepository.findByIdAndProject_UserId(fileId, userId).orElse(null);
    }

    public void deleteCodeFile(Long fileId) throws IOException {
        CodeFileEntity codeFile = fileRepository.findById(fileId).orElse(null);
        if (codeFile != null) {
            fileRepository.deleteById(fileId);
            Path projectDir = Paths.get("compiled", codeFile.getProject().getName()).toAbsolutePath();
            Path filePath = projectDir.resolve(codeFile.getFileName());
            Path classFilePath = projectDir.resolve(codeFile.getFileName().replace(".java", ".class"));
            Files.deleteIfExists(filePath);
            Files.deleteIfExists(classFilePath);

            // If the project directory is empty, delete it
            if (isDirectoryEmpty(projectDir)) {
                Files.deleteIfExists(projectDir);
            }
        }
    }

    public void deleteProject(Long projectId) throws IOException {
        ProjectEntity project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            projectRepository.deleteById(projectId);
            Path projectDir = Paths.get("compiled", project.getName()).toAbsolutePath();
            if (Files.exists(projectDir)) {
                Files.walk(projectDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            }
        }
    }

    public String compileProject(Long projectId) throws IOException, InterruptedException {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));

        // Create a directory for the project
        Path projectDir = Paths.get("compiled", project.getName()).toAbsolutePath();
        if (Files.exists(projectDir)) {
            Files.walk(projectDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
        Files.createDirectories(projectDir);

        List<Path> sourceFiles = new ArrayList<>();
        StringBuilder compileOutput = new StringBuilder();
        for (CodeFileEntity codeFile : project.getCodeFiles()) {
            String fileName = codeFile.getFileName();
            if (!fileName.endsWith(".java")) {
                fileName += ".java";
            }

            // Save the file to the filesystem
            Path filePath = projectDir.resolve(fileName);
            Files.write(filePath, codeFile.getContent().getBytes());
            sourceFiles.add(filePath);
        }

        // Compile all files
        List<String> javacArgs = new ArrayList<>();
        javacArgs.add("javac");
        javacArgs.addAll(sourceFiles.stream().map(Path::toString).collect(Collectors.toList()));

        ProcessBuilder compileProcessBuilder = new ProcessBuilder(javacArgs);
        compileProcessBuilder.directory(projectDir.toFile()); // Set the working directory to projectDir
        Process compileProcess = compileProcessBuilder.start();
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                compileOutput.append("Compilation failed:\n").append(line).append("\n");
            }
            return compileOutput.toString();
        } else {
            compileOutput.append("Compilation succeeded\n");
        }

        for (CodeFileEntity codeFile : project.getCodeFiles()) {
            String fileName = codeFile.getFileName();
            if (fileName.endsWith(".java")) {
                fileName = fileName.substring(0, fileName.length() - 5); // Remove the .java extension
            }
            String className = getClassNameFromFile(Paths.get(projectDir.toString(), fileName + ".java"));

            if (className.isEmpty()) {
                className = fileName;
            }

            if (containsMainMethod(Paths.get(projectDir.toString(), fileName + ".java"))) {
                // Run the compiled class
                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", projectDir.toString(), className);
                runProcessBuilder.directory(projectDir.toFile()); // Set the working directory to projectDir
                Process runProcess = runProcessBuilder.start();

                // Collect standard output
                BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = outputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // Collect error output
                BufferedReader errorOutputReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                while ((line = errorOutputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int runExitCode = runProcess.waitFor();

                if (runExitCode != 0) {
                    compileOutput.append("Execution failed for ").append(className).append(":\n").append(output.toString());
                } else {
                    compileOutput.append("Execution succeeded for ").append(className).append(":\n").append(output.toString());
                }
            }
        }

        return compileOutput.toString();
    }

    private void saveFileToSystem(CodeFileEntity codeFile) {
        try {
            Path projectDir = Paths.get("compiled", codeFile.getProject().getName()).toAbsolutePath();
            if (!Files.exists(projectDir)) {
                Files.createDirectories(projectDir);
            }
            Path filePath = projectDir.resolve(codeFile.getFileName());
            Files.write(filePath, codeFile.getContent().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getClassNameFromFile(Path javaFilePath) throws IOException {
        BufferedReader reader = Files.newBufferedReader(javaFilePath);
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("public class ") || line.startsWith("class ")) {
                String[] tokens = line.split("\\s+");
                int classIndex = -1;
                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("class")) {
                        classIndex = i;
                        break;
                    }
                }
                if (classIndex != -1 && classIndex < tokens.length - 1) {
                	reader.close();
                    return tokens[classIndex + 1];
                }
            }
        }
        reader.close();
        return javaFilePath.getFileName().toString().replace(".java", "");
    }

    private boolean containsMainMethod(Path javaFilePath) throws IOException {
        BufferedReader reader = Files.newBufferedReader(javaFilePath);
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.contains("public static void main(String[] args)")) {
                reader.close();
            	return true;
            }
        }
        reader.close();
        return false;
    }

    private boolean isDirectoryEmpty(Path path) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            return !directoryStream.iterator().hasNext();
        }
    }

    public void cleanCompiledDirectory() throws IOException {
        Path compiledDir = Paths.get("compiled").toAbsolutePath();
        if (Files.exists(compiledDir)) {
            Files.walk(compiledDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
