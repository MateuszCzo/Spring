package mc.project.online_store.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AuthenticationRequest;
import mc.project.online_store.dto.request.RegisterRequest;
import mc.project.online_store.dto.response.AuthenticationResponse;
import mc.project.online_store.dto.response.RegisterResponse;
import mc.project.online_store.enums.UserRole;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.auth.AuthService;
import mc.project.online_store.service.auth.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRole(UserRole.ROLE_USER);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        RegisterResponse response = new RegisterResponse();
        response.setRegistered(true);
        response.setToken(jwtToken);

        return response;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        User user = userRepository
                .findByName(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);

        return response;
    }
}
