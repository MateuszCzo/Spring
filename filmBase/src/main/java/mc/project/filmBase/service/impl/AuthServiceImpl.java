package mc.project.filmBase.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.AuthenticationRequest;
import mc.project.filmBase.dto.request.RegisterRequest;
import mc.project.filmBase.dto.response.AuthenticationResponse;
import mc.project.filmBase.enums.UserRole;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.UserRepository;
import mc.project.filmBase.service.auth.AuthService;
import mc.project.filmBase.service.auth.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return AuthenticationResponse.builder()
                    .registered(false)
                    .build();
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .registered(true)
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        User user = userRepository
                .findByUsername(authenticationRequest.getUsername())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .registered(true)
                .build();
    }
}
