package mc.project.online_store.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AuthenticationRequest;
import mc.project.online_store.dto.request.RegisterRequest;
import mc.project.online_store.dto.response.AuthenticationResponse;
import mc.project.online_store.dto.response.RegisterResponse;
import mc.project.online_store.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest) {

        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok(authService.authenticate(authenticationRequest));
    }
}
