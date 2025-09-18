package org.example.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        System.out.println("Received GET /");
        return "Greetings from Spring Boot!";
    }

}
