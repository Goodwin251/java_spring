package com.autotestplatform.codehandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.autotestplatform.entities.CodeFileEntity;
import com.autotestplatform.entities.ProjectEntity;
import com.autotestplatform.repos.FileRepository;
import com.autotestplatform.repos.ProjectRepository;

@ExtendWith(MockitoExtension.class)
public class CodeFileServiceTest {
//
//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @InjectMocks
//    private CodeFileService codeFileService;

//    @BeforeEach
//    void setUp() {
//        codeFileService = new CodeFileService();
//    }

//    @Test
//    void testSaveCodeFile() {
//        CodeFileEntity codeFile = new CodeFileEntity();
//        codeFile.setFileName("TestFile.java");
//        doNothing().when(fileRepository).save(codeFile);
//
//        codeFileService.saveCodeFile(codeFile);
//
//        verify(fileRepository, times(1)).save(codeFile);
//    }
//
//    @Test
//    void testSaveProject() {
//        ProjectEntity project = new ProjectEntity();
//        project.setName("Test Project");
//        doNothing().when(projectRepository).save(project);
//
//        codeFileService.saveProject(project);
//
//        verify(projectRepository, times(1)).save(project);
//    }
//
//    @Test
//    void testGetAllCodeFiles() {
//        CodeFileEntity codeFile1 = new CodeFileEntity();
//        CodeFileEntity codeFile2 = new CodeFileEntity();
//        when(fileRepository.findAll()).thenReturn(Arrays.asList(codeFile1, codeFile2));
//
//        List<CodeFileEntity> codeFiles = codeFileService.getAllCodeFiles();
//
//        assertEquals(2, codeFiles.size());
//        verify(fileRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAllProjects() {
//        ProjectEntity project1 = new ProjectEntity();
//        ProjectEntity project2 = new ProjectEntity();
//        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));
//
//        List<ProjectEntity> projects = codeFileService.getAllProjects();
//
//        assertEquals(2, projects.size());
//        verify(projectRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetProjectsByUserId() {
//        String userId = "testUser";
//        ProjectEntity project1 = new ProjectEntity();
//        project1.setUserId(userId);
//        ProjectEntity project2 = new ProjectEntity();
//        project2.setUserId(userId);
//        when(projectRepository.findByUserId(userId)).thenReturn(Arrays.asList(project1, project2));
//
//        List<ProjectEntity> projects = codeFileService.getProjectsByUserId(userId);
//
//        assertEquals(2, projects.size());
//        verify(projectRepository, times(1)).findByUserId(userId);
//    }
//
//    @Test
//    void testGetProjectByIdAndUserId() {
//        Long projectId = 1L;
//        String userId = "testUser";
//        ProjectEntity project = new ProjectEntity();
//        project.setId(projectId);
//        project.setUserId(userId);
//        when(projectRepository.findByIdAndUserId(projectId, userId)).thenReturn(Optional.of(project));
//
//        ProjectEntity result = codeFileService.getProjectByIdAndUserId(projectId, userId);
//
//        assertNotNull(result);
//        assertEquals(projectId, result.getId());
//        verify(projectRepository, times(1)).findByIdAndUserId(projectId, userId);
//    }
//
//    @Test
//    void testGetCodeFileByIdAndUserId() {
//        Long fileId = 1L;
//        String userId = "testUser";
//        CodeFileEntity codeFile = new CodeFileEntity();
//        codeFile.setId(fileId);
//        ProjectEntity project = new ProjectEntity();
//        project.setUserId(userId);
//        codeFile.setProject(project);
//        when(fileRepository.findByIdAndProject_UserId(fileId, userId)).thenReturn(Optional.of(codeFile));
//
//        CodeFileEntity result = codeFileService.getCodeFileByIdAndUserId(fileId, userId);
//
//        assertNotNull(result);
//        assertEquals(fileId, result.getId());
//        verify(fileRepository, times(1)).findByIdAndProject_UserId(fileId, userId);
//    }
}

