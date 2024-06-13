//package com.autotestplatform.codehandler;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Collections;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.autotestplatform.entities.CodeFileEntity;
//import com.autotestplatform.entities.ProjectEntity;
//import com.autotestplatform.repos.FileRepository;
//import com.autotestplatform.repos.ProjectRepository;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CodeControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CodeFileService codeFileService;
//
//    @MockBean
//    private FileRepository fileRepository;
//
//    @MockBean
//    private ProjectRepository projectRepository;
//
//    private OidcUser mockOidcUser;
//
//    @BeforeEach
//    void setUp() {
//        mockOidcUser = Mockito.mock(OidcUser.class);
//        Mockito.when(mockOidcUser.getSubject()).thenReturn("testUser");
//    }
//
//    @Test
//    void testUploadCodeFile() throws Exception {
//        ProjectEntity project = new ProjectEntity();
//        project.setId(1L);
//        project.setName("Test Project");
//        project.setUserId("testUser");
//
//        Mockito.when(codeFileService.getProjectByIdAndUserId(1L, "testUser")).thenReturn(project);
//
//        MockMultipartFile file = new MockMultipartFile("file", "test.java", "text/plain", "public class Test {}".getBytes(StandardCharsets.UTF_8));
//
//        mockMvc.perform(saveCodeFile("/uploadCodeFile")
//                .file(file)
//                .param("projectId", "1")
//                .principal(() -> mockOidcUser))
//                .andExpect(status().isOk())
//                .andExpect(view().name("code"))
//                .andExpect(model().attributeExists("message"))
//                .andExpect(model().attribute("message", "File uploaded successfully!"));
//
//        Mockito.verify(codeFileService, Mockito.times(1)).saveCodeFile(Mockito.any(CodeFileEntity.class));
//    }
//
//    @Test
//    void testCompileProject() throws Exception {
//        ProjectEntity project = new ProjectEntity();
//        project.setId(1L);
//        project.setName("Test Project");
//        project.setUserId("testUser");
//
//        CodeFileEntity codeFile = new CodeFileEntity();
//        codeFile.setId(1L);
//        codeFile.setFileName("Test.java");
//        codeFile.setContent("public class Test { public static void main(String[] args) { System.out.println(\"Hello World\"); } }");
//        codeFile.setProject(project);
//
//        project.setCodeFiles(Collections.singletonList(codeFile));
//
//        Mockito.when(codeFileService.getProjectByIdAndUserId(1L, "testUser")).thenReturn(project);
//        Mockito.when(codeFileService.compileProject(1L)).thenReturn("Compilation succeeded: Hello World");
//
//        mockMvc.perform(post("/compileProject")
//                .param("projectId", "1")
//                .principal(() -> mockOidcUser))
//                .andExpect(status().isOk())
//                .andExpect(view().name("compileResult"))
//                .andExpect(model().attributeExists("result"))
//                .andExpect(model().attribute("result", "Compilation succeeded: Hello World"));
//
//        Mockito.verify(codeFileService, Mockito.times(1)).compileProject(1L);
//    }
//}
