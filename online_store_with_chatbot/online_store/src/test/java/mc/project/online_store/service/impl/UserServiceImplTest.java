package mc.project.online_store.service.impl;

import mc.project.online_store.enums.UserRole;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void givenNoUserInSecurityContextHolder_whenGetLoggedInUser_thenReturnsEmptyOptional() {
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return null;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isEmpty());
    }

    @Test
    public void givenNoUserInDatabase_whenGetLoggedInUser_thenReturnsEmptyOptional() {
        String username = "username_example";

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public String getPassword() {
                return "";
            }
            @Override
            public String getUsername() {
                return username;
            }
            @Override
            public boolean isAccountNonExpired() {
                return false;
            }
            @Override
            public boolean isAccountNonLocked() {
                return false;
            }
            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }
            @Override
            public boolean isEnabled() {
                return false;
            }
        };

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return userDetails;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByName(username)).thenReturn(Optional.empty());

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isEmpty());
    }

    @Test
    public void givenUser_whenGetLoggedInUser_thenReturnsOptionalWithUser() {
        User user = new User();
        user.setId(1L);
        user.setName("name_example");
        user.setPassword("password_example");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setRole(UserRole.ROLE_USER);

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return user;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByName(user.getUsername())).thenReturn(Optional.of(user));

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isPresent());
        assertEquals(user, response.get());
    }
}