package com.autotestplatform;

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//import com.autotestplatform.codehandler.*;
//import com.autotestplatform.repos.*;
//import com.autotestplatform.entities.*;

class CodeFileServiceTest {

//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @InjectMocks
//    private CodeFileService codeFileService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void saveCodeFile() {
//        CodeFileEntity codeFile = new CodeFileEntity();
//        codeFileService.saveCodeFile(codeFile);
//        verify(fileRepository, times(1)).save(codeFile);
//    }
//
//    @Test
//    void saveProject() {
//        ProjectEntity project = new ProjectEntity();
//        codeFileService.saveProject(project);
//        verify(projectRepository, times(1)).save(project);
//    }
//
//    @Test
//    void deleteCodeFile() throws IOException {
//        CodeFileEntity codeFile = new CodeFileEntity();
//        codeFile.setFileName("Test.java");
//        ProjectEntity project = new ProjectEntity();
//        project.setName("TestProject");
//        codeFile.setProject(project);
//
//        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(codeFile));
//
//        codeFileService.deleteCodeFile(1L);
//
//        verify(fileRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void deleteProject() throws IOException {
//        ProjectEntity project = new ProjectEntity();
//        project.setName("TestProject");
//
//        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
//
//        codeFileService.deleteProject(1L);
//
//        verify(projectRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void compileProject() throws IOException, InterruptedException {
//        ProjectEntity project = new ProjectEntity();
//        project.setName("TestProject");
//        project.setCodeFiles(Collections.emptyList());
//
//        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
//
//        codeFileService.compileProject(1L);
//
//        verify(projectRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void cleanCompiledDirectory() throws IOException {
//        codeFileService.cleanCompiledDirectory();
//        // No verification needed as this method interacts with the file system
//    }
}
