package mc.project1.restapi.controller;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.dto.RegisterRequest;
import mc.project1.restapi.service.RegisterUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController
{
    private final RegisterUserService registerUserService;

    @PostMapping("/register")
    public void registerAccount(@RequestBody RegisterRequest request)
    {
        registerUserService.registerUser(request);
    }
}
