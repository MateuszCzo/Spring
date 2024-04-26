package mc.project.filmBase.service.auth;

import mc.project.filmBase.dto.request.AuthenticationRequest;
import mc.project.filmBase.dto.request.RegisterRequest;
import mc.project.filmBase.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
