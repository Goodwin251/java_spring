package com.autotestplatform;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.autotestplatform.codehandler.CodeFileService;

import jakarta.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories("com.autotestplatform")
@ComponentScan(basePackages = { "com.autotestplatform" })
@EntityScan(basePackages = "com.autotestplatform")
public class AutomationTestPlatform1Application {


    private final CodeFileService codeFileService;

    public AutomationTestPlatform1Application(CodeFileService codeFileService) {
        this.codeFileService = codeFileService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AutomationTestPlatform1Application.class, args);
    }

    @PreDestroy
    public void cleanUp() throws IOException {
        codeFileService.cleanCompiledDirectory();
    }

}
