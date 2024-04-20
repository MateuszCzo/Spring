package mc.microservice.user_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping
    public String hello() {
        return "Hello World! User Controller";
    }
}
