package mc.project1.restapi.service;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.dto.RegisterRequest;
import mc.project1.restapi.model.Authority;
import mc.project1.restapi.model.User;
import mc.project1.restapi.repository.AuthorityRepository;
import mc.project1.restapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterUserService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request)
    {
        Authority authority = authorityRepository.findById(1L)
                .orElseThrow();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthorities(List.of(authority));
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);

        userRepository.save(user);
    }
}
