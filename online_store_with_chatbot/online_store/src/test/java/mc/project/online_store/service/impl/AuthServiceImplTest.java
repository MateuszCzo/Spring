package mc.project.online_store.service.impl;

import mc.project.online_store.dto.request.AuthenticationRequest;
import mc.project.online_store.dto.request.RegisterRequest;
import mc.project.online_store.dto.response.RegisterResponse;
import mc.project.online_store.enums.UserRole;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidRegisterRequest_whenRegister_thenReturnsValidRegisterResponse() {
        String encodedPassword = "encoded_password";
        String token = "example_token";

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("example_username");
        request.setPassword("example_password");

        User user = new User();
        user.setName(request.getUsername());
        user.setPassword(encodedPassword);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRole(UserRole.ROLE_USER);

        when(userRepository.findByName(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any())).thenReturn(token);

        RegisterResponse response = authService.register(request);

        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertTrue(response.isRegistered());
        assertEquals(token, response.getToken());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.isAccountNonExpired(), savedUser.isAccountNonExpired());
        assertEquals(user.isAccountNonLocked(), savedUser.isAccountNonLocked());
        assertEquals(user.isCredentialsNonExpired(), savedUser.isCredentialsNonExpired());
        assertEquals(user.isEnabled(), savedUser.isEnabled());
        assertEquals(user.getRole(), savedUser.getRole());
        assertEquals(user.isEnabled(), savedUser.isEnabled());
    }

    @Test
    public void givenValidAuthenticationRequest_whenAuthenticate_thenReturnsValidAuthenticationResponse() {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setUsername("example_username");
        authRequest.setPassword("example_password");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> usrPasAuthTokArgC =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        User user = new User();
        user.setName(authRequest.getUsername());
        user.setPassword(authRequest.getPassword());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRole(UserRole.ROLE_USER);

        String token = "valid_token";

        when(userRepository.findByName(authRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        authService.authenticate(authRequest);

        verify(authenticationManager).authenticate(usrPasAuthTokArgC.capture());

        UsernamePasswordAuthenticationToken usrPasAuthTokArg = usrPasAuthTokArgC.getValue();

        assertEquals(authRequest.getUsername(), usrPasAuthTokArg.getName());
        assertEquals(authRequest.getPassword(), usrPasAuthTokArg.getCredentials());
    }
}