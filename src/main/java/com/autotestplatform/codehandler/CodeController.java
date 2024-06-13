package com.autotestplatform.codehandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.autotestplatform.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Tag(name = "Code Management System", description = "APIs for managing code projects and files")
@Controller
public class CodeController {

    @Autowired
    private CodeFileService codeFileService;
    @Operation(summary = "Redirect to login")
    @GetMapping("/")
    public String redirectToCode() {
        return "redirect:/code";
    }
    
    @Operation(summary = "View the code form")
    @GetMapping("/code")
    public String codeForm(Model model, @AuthenticationPrincipal OidcUser principal) {
        String userId = principal.getSubject();
        List<ProjectEntity> projects = codeFileService.getProjectsByUserId(userId);
        model.addAttribute("projects", projects);
        return "code";
    }

    @Operation(summary = "Submit a new project")
    @PostMapping("/submitProject")
    public String submitProject(@RequestParam String projectName, @AuthenticationPrincipal OidcUser principal, Model model) {
        String userId = principal.getSubject();
        ProjectEntity project = new ProjectEntity();
        project.setName(projectName);
        project.setUserId(userId);
        codeFileService.saveProject(project);

        model.addAttribute("message", "Project created successfully!");
        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "code";
    }

    @Operation(summary = "Submit code for a project")
    @PostMapping("/submitCode")
    public String submitCode(@RequestParam Long projectId, @RequestParam String fileName, @RequestParam String content, @AuthenticationPrincipal OidcUser principal, Model model) {
        String userId = principal.getSubject();
        ProjectEntity project = codeFileService.getProjectByIdAndUserId(projectId, userId);
        if (project == null) {
            model.addAttribute("message", "Project not found!");
            model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
            return "code";
        }

        CodeFileEntity codeFile = new CodeFileEntity();
        codeFile.setFileName(fileName);
        codeFile.setContent(content);
        codeFile.setProject(project);
        codeFileService.saveCodeFile(codeFile);

        model.addAttribute("message", "Code submitted successfully!");
        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "code";
    }

    @Operation(summary = "Upload a code file for a project")
    @PostMapping("/uploadCodeFile")
    public String uploadCodeFile(@RequestParam Long projectId, @RequestParam MultipartFile file, @AuthenticationPrincipal OidcUser principal, Model model) throws IOException {
        String userId = principal.getSubject();
        ProjectEntity project = codeFileService.getProjectByIdAndUserId(projectId, userId);
        if (project == null) {
            model.addAttribute("message", "Project not found!");
            model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
            return "code";
        }

        String content = new String(file.getBytes());

        CodeFileEntity codeFile = new CodeFileEntity();
        codeFile.setFileName(file.getOriginalFilename());
        codeFile.setContent(content);
        codeFile.setProject(project);
        codeFileService.saveCodeFile(codeFile);

        model.addAttribute("message", "File uploaded successfully!");
        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "code";
    }

    @Operation(summary = "View the list of code projects")
    @GetMapping("/codeList")
    public String codeList(Model model, @AuthenticationPrincipal OidcUser principal) {
        String userId = principal.getSubject();
        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "codeList";
    }

    @Operation(summary = "Delete a code file")
    @PostMapping("/deleteCodeFile")
    public String deleteCodeFile(@RequestParam Long fileId, @AuthenticationPrincipal OidcUser principal, Model model) throws IOException {
        String userId = principal.getSubject();
        CodeFileEntity codeFile = codeFileService.getCodeFileByIdAndUserId(fileId, userId);
        if (codeFile != null) {
            codeFileService.deleteCodeFile(fileId);
        }

        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "codeList";
    }

    @Operation(summary = "Delete a project")
    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam Long projectId, @AuthenticationPrincipal OidcUser principal, Model model) throws IOException {
        String userId = principal.getSubject();
        ProjectEntity project = codeFileService.getProjectByIdAndUserId(projectId, userId);
        if (project != null) {
            codeFileService.deleteProject(projectId);
        }

        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "codeList";
    }

    @Operation(summary = "Compile the project")
    @PostMapping("/compileProject")
    public String compileProject(@RequestParam Long projectId, @AuthenticationPrincipal OidcUser principal, Model model) {
        String userId = principal.getSubject();
        ProjectEntity project = codeFileService.getProjectByIdAndUserId(projectId, userId);
        if (project == null) {
            model.addAttribute("result", "Project not found!");
            return "compileResult";
        }

        try {
            String result = codeFileService.compileProject(projectId);
            model.addAttribute("result", result);
        } catch (IOException | InterruptedException e) {
            model.addAttribute("result", "Error during compilation: " + e.getMessage());
        }
        return "compileResult";
    }

    @Operation(summary = "Download a code file")
    @PostMapping("/downloadCodeFile")
    public String downloadCodeFile(@RequestParam Long fileId, @AuthenticationPrincipal OidcUser principal, Model model) throws IOException {
        String userId = principal.getSubject();
        CodeFileEntity codeFile = codeFileService.getCodeFileByIdAndUserId(fileId, userId);
        if (codeFile == null) {
            model.addAttribute("message", "File not found!");
            model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
            return "codeList";
        }

        Path tempDir = Files.createTempDirectory("download");
        Path tempFile = tempDir.resolve(codeFile.getFileName());
        Files.write(tempFile, codeFile.getContent().getBytes());

        model.addAttribute("message", "File downloaded successfully to " + tempFile.toAbsolutePath().toString());
        model.addAttribute("projects", codeFileService.getProjectsByUserId(userId));
        return "codeList";
    }
}
