package mc.project.online_store.validator.impl;

import mc.project.online_store.model.User;
import mc.project.online_store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UniqueUsernameValidatorTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UniqueUsernameValidator uniqueUsernameValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenNullUsername_whenIsValid_thenReturnsTrue() {
        boolean isValid = uniqueUsernameValidator.isValid(null, null);

        assertTrue(isValid);
    }

    @Test
    public void givenValidUsername_whenIsValid_thenReturnsTrue() {
        String username = "valid_username";
        when(userRepository.findByName(username)).thenReturn(Optional.empty());

        boolean isValid = uniqueUsernameValidator.isValid(username, null);

        assertTrue(isValid);
    }

    @Test
    public void givenInvalidUsername_whenIsValid_thenReturnsFalse() {
        String username = "invalid_username";
        User user = new User();
        user.setName(username);
        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        boolean isValid = uniqueUsernameValidator.isValid(username, null);

        assertFalse(isValid);
    }
}