package com.autotestplatform;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.autotestplatform.codehandler.CodeFileService;

import jakarta.annotation.PreDestroy;
import io.github.cdimascio.dotenv.Dotenv;

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
    	if("#{spring.security.oauth2.client.registration.google.client-id}" == "placeholder"
    || "spring.security.oauth2.client.registration.google.client-secret" == "placeholder") 
    	{
        	Dotenv dotenv = Dotenv.configure().load();

        	System.setProperty("spring.security.oauth2.client.registration.google.client-id", dotenv.get("GOOGLE_CLIENT_ID"));
            System.setProperty("spring.security.oauth2.client.registration.google.client-secret", dotenv.get("GOOGLE_CLIENT_SECRET"));
    	}

        
        SpringApplication.run(AutomationTestPlatform1Application.class, args);
    }

    @PreDestroy
    public void cleanUp() throws IOException {
        codeFileService.cleanCompiledDirectory();
    }

}
