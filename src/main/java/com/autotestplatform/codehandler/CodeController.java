package com.autotestplatform.codehandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.autotestplatform.entities.CodeFileEntity;
import com.autotestplatform.entities.ProjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CodeController {

    @Autowired
    private CodeFileService codeFileService;

    @GetMapping("/code")
    public String codeForm(Model model) {
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "code"; // Назва шаблону code.html
    }

    @PostMapping("/submitProject")
    public String submitProject(@RequestParam String projectName, Model model) {
        ProjectEntity project = new ProjectEntity();
        project.setName(projectName);
        codeFileService.saveProject(project);

        model.addAttribute("message", "Project created successfully!");
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "code"; // Повернення до шаблону code.html
    }

    @PostMapping("/submitCode")
    public String submitCode(@RequestParam Long projectId, @RequestParam String fileName, @RequestParam String content, Model model) {
        ProjectEntity project = codeFileService.getAllProjects().stream().filter(p -> p.getId().equals(projectId)).findFirst().orElse(null);
        if (project == null) {
            project = new ProjectEntity();
            project.setId(projectId);
            codeFileService.saveProject(project);
        }

        CodeFileEntity codeFile = new CodeFileEntity();
        codeFile.setFileName(fileName);
        codeFile.setContent(content);
        codeFile.setProject(project);
        codeFileService.saveCodeFile(codeFile);

        model.addAttribute("message", "Code submitted successfully!");
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "code"; // Повернення до шаблону code.html
    }

    @PostMapping("/uploadCodeFile")
    public String uploadCodeFile(@RequestParam Long projectId, @RequestParam MultipartFile file, Model model) throws IOException {
        ProjectEntity project = codeFileService.getAllProjects().stream().filter(p -> p.getId().equals(projectId)).findFirst().orElse(null);
        if (project == null) {
            model.addAttribute("message", "Project not found!");
            model.addAttribute("projects", codeFileService.getAllProjects());
            return "code"; // Повернення до шаблону code.html
        }

        String content = new String(file.getBytes());

        CodeFileEntity codeFile = new CodeFileEntity();
        codeFile.setFileName(file.getOriginalFilename());
        codeFile.setContent(content);
        codeFile.setProject(project);
        codeFileService.saveCodeFile(codeFile);

        model.addAttribute("message", "File uploaded successfully!");
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "code"; // Повернення до шаблону code.html
    }

    @GetMapping("/codeList")
    public String codeList(Model model) {
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "codeList"; // Назва шаблону для відображення списку проектів і файлів
    }

    @PostMapping("/deleteCodeFile")
    public String deleteCodeFile(@RequestParam Long fileId, Model model) throws IOException {
        codeFileService.deleteCodeFile(fileId);
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "codeList"; // Повернення до шаблону codeList.html
    }

    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam Long projectId, Model model) throws IOException {
        codeFileService.deleteProject(projectId);
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "codeList"; // Повернення до шаблону codeList.html
    }

    @PostMapping("/compileProject")
    public String compileProject(@RequestParam Long projectId, Model model) {
        try {
            String result = codeFileService.compileProject(projectId);
            model.addAttribute("result", result);
        } catch (IOException | InterruptedException e) {
            model.addAttribute("result", "Error during compilation: " + e.getMessage());
        }
        return "compileResult"; // Назва шаблону для відображення результатів компіляції
    }

    @PostMapping("/downloadCodeFile")
    public String downloadCodeFile(@RequestParam Long fileId, Model model) throws IOException {
        CodeFileEntity codeFile = codeFileService.getAllCodeFiles().stream().filter(f -> f.getId().equals(fileId)).findFirst().orElse(null);
        if (codeFile == null) {
            model.addAttribute("message", "File not found!");
            model.addAttribute("projects", codeFileService.getAllProjects());
            return "codeList"; // Повернення до шаблону codeList.html
        }

        Path tempDir = Files.createTempDirectory("download");
        Path tempFile = tempDir.resolve(codeFile.getFileName());
        Files.write(tempFile, codeFile.getContent().getBytes());

        model.addAttribute("message", "File downloaded successfully to " + tempFile.toAbsolutePath().toString());
        model.addAttribute("projects", codeFileService.getAllProjects());
        return "codeList"; // Повернення до шаблону codeList.html
    }
}
