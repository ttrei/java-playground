package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext ctx, ContentService contentService) {
        return args -> {
            if (args.length != 1) {
                throw new IllegalArgumentException("Exactly one argument required");
            }
            contentService.run(Integer.parseInt(args[0]));
            int exitCode = SpringApplication.exit(ctx, () -> 0);
            System.exit(exitCode);
        };
    }
}
