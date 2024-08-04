package mc.project.online_store.service.auth;

import mc.project.online_store.dto.request.AuthenticationRequest;
import mc.project.online_store.dto.request.RegisterRequest;
import mc.project.online_store.dto.response.AuthenticationResponse;
import mc.project.online_store.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
