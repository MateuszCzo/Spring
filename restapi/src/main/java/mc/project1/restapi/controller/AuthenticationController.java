package mc.project1.restapi.controller;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.config.JwtUtils;
import mc.project1.restapi.dto.AuthenticationRequest;
import mc.project1.restapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        ));
        final UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return ResponseEntity.ok(jwtUtils.generateToken(user));

        // return ResponseEntity.status(400).body("Can not authenticate.");
    }

    @GetMapping("/secured")
    public String secured()
    {
        return "secured";
    }
}
