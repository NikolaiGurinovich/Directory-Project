package com.directoryproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Directory Project",
        description = "app for directory management",
        contact = @Contact(name = "Nikolay Gurinovich",
                email = "nikolay.gurinovich@gmail.com")
))
@SpringBootApplication
public class DirectoryProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectoryProjectApplication.class, args);
    }
}
