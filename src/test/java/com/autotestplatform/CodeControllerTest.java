package com.autotestplatform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.autotestplatform.codehandler.*;
import com.autotestplatform.entities.*;

class CodeControllerTest {

    @Mock
    private CodeFileService codeFileService;

    @Mock
    private Model model;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CodeController codeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void codeForm() {
        String result = codeController.codeForm(model);
        verify(codeFileService, times(1)).getAllProjects();
        assertEquals("code", result);
    }

    @Test
    void submitProject() {
        String result = codeController.submitProject("TestProject", model);
        verify(codeFileService, times(1)).saveProject(any(ProjectEntity.class));
        verify(codeFileService, times(1)).getAllProjects();
        assertEquals("code", result);
    }

    @Test
    void submitCode() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        when(codeFileService.getAllProjects()).thenReturn(Collections.singletonList(project));

        String result = codeController.submitCode(1L, "Test.java", "public class Test {}", model);
        verify(codeFileService, times(1)).saveCodeFile(any(CodeFileEntity.class));
        verify(codeFileService, times(2)).getAllProjects();
        assertEquals("code", result);
    }

    @Test
    void uploadCodeFile() throws IOException {
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        when(codeFileService.getAllProjects()).thenReturn(Collections.singletonList(project));
        when(file.getBytes()).thenReturn("public class Test {}".getBytes());
        when(file.getOriginalFilename()).thenReturn("Test.java");

        String result = codeController.uploadCodeFile(1L, file, model);
        verify(codeFileService, times(1)).saveCodeFile(any(CodeFileEntity.class));
        verify(codeFileService, times(2)).getAllProjects();
        assertEquals("code", result);
    }

    @Test
    void codeList() {
        String result = codeController.codeList(model);
        verify(codeFileService, times(1)).getAllProjects();
        assertEquals("codeList", result);
    }

    @Test
    void deleteCodeFile() throws IOException {
        String result = codeController.deleteCodeFile(1L, model);
        verify(codeFileService, times(1)).deleteCodeFile(1L);
        verify(codeFileService, times(1)).getAllProjects();
        assertEquals("codeList", result);
    }

    @Test
    void deleteProject() throws IOException {
        String result = codeController.deleteProject(1L, model);
        verify(codeFileService, times(1)).deleteProject(1L);
        verify(codeFileService, times(1)).getAllProjects();
        assertEquals("codeList", result);
    }

    @Test
    void compileProject() throws IOException, InterruptedException {
        String result = codeController.compileProject(1L, model);
        verify(codeFileService, times(1)).compileProject(1L);
        assertEquals("compileResult", result);
    }

    @Test
    void downloadCodeFile() throws IOException {
        CodeFileEntity codeFile = new CodeFileEntity();
        codeFile.setId(1L);
        codeFile.setFileName("Test.java");
        codeFile.setContent("public class Test {}");

        when(codeFileService.getAllCodeFiles()).thenReturn(Collections.singletonList(codeFile));

        String result = codeController.downloadCodeFile(1L, model);
        verify(codeFileService, times(1)).getAllCodeFiles();
        assertEquals("codeList", result);
    }
}
