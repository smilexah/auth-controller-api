package sdu.edu.kz.authcontroller.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String demo() {
        return "Hello World!";
    }

    @GetMapping("/test")
    @Tag(name = "Test", description = "TEST API")
    @SecurityRequirement(name = "sduedu-demo-api")
    public String test() {
        return "Test API";
    }
}
