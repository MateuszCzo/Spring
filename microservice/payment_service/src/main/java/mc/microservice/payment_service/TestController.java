package mc.microservice.payment_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping
    public String hello() {
        return "Hello World! Payment Controller";
    }
}
