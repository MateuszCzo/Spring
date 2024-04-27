package mc.project.filmBase.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.AuthenticationRequest;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.request.RegisterRequest;
import mc.project.filmBase.dto.response.AuthenticationResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.enums.UserRole;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.repository.UserRepository;
import mc.project.filmBase.service.auth.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @Transactional
    void register() throws Exception {
        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .password("test_password")
                .username("test_username")
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        AuthenticationResponse authenticationResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AuthenticationResponse.class
        );

        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());

        assertNotNull(authenticationResponse);
        assertTrue(user.isPresent());

        assertEquals(registerRequest.getUsername(), user.get().getUsername());
        assertNotEquals(registerRequest.getPassword(), user.get().getPassword());

        assertTrue(authenticationResponse.isRegistered());
        assertNotEquals(authenticationResponse.getToken(), "");
    }

    @Test
    @Transactional
    void canNotRegister() throws Exception {
        // Given
        User user = User.builder()
                .username("user_name")
                .password(passwordEncoder.encode("user_password"))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        RegisterRequest registerRequest = RegisterRequest.builder()
                .password("test_password")
                .username(user.getUsername())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        AuthenticationResponse authenticationResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AuthenticationResponse.class
        );

        assertNotNull(authenticationResponse);
        assertFalse(authenticationResponse.isRegistered());
        assertNull(authenticationResponse.getToken());
    }

    @Test
    @Transactional
    void authenticate() throws Exception {
        // Given
        String password = "user_password";

        User user = User.builder()
                .username("user_name")
                .password(passwordEncoder.encode(password))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .password(password)
                .username(user.getUsername())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/authenticate")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        AuthenticationResponse authenticationResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AuthenticationResponse.class
        );

        assertNotNull(authenticationResponse);
        assertTrue(authenticationResponse.isRegistered());
        assertEquals(authenticationResponse.getToken(), jwtService.generateToken(user));
    }

    @Test
    @Transactional
    void canNotAuthenticate() throws Exception {
        // Given
        User user = User.builder()
                .username("user_name")
                .password(passwordEncoder.encode("password"))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .password("different_password")
                .username(user.getUsername())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/authenticate")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 403);
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    void canAccessAdminRoute() throws Exception {
        // Given

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film"))
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 200);
    }

    @Test
    @Transactional
    @WithMockUser
    void canNotAccessAdminRoute() throws Exception {
        // Given
        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film"))
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 403);
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void canAccessRatingFrontControllerPutMethod() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        Rating rating = Rating.builder()
                .status(RatingStatus.CONFIRMED)
                .rating(4)
                .description("rating_description")
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        RatingRequest ratingRequest = RatingRequest.builder()
                .id(rating.getId())
                .rating(2)
                .description("new_rating_description")
                .filmId(film.getId())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequest))
                )
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 200);
    }

    @Test
    @Transactional
    void canNotAccessRatingFrontControllerPutMethod() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        Rating rating = Rating.builder()
                .status(RatingStatus.CONFIRMED)
                .rating(4)
                .description("rating_description")
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        RatingRequest ratingRequest = RatingRequest.builder()
                .id(rating.getId())
                .rating(2)
                .description("new_rating_description")
                .filmId(film.getId())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequest))
                )
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 403);
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void canAccessRatingFrontControllerPostMethod() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        RatingRequest ratingRequest = RatingRequest.builder()
                .rating(2)
                .description("new_rating_description")
                .filmId(film.getId())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequest))
                )
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 200);
    }

    @Test
    @Transactional
    void canNotAccessRatingFrontControllerPostMethod() throws Exception {
        // Given
        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        RatingRequest ratingRequest = RatingRequest.builder()
                .rating(2)
                .description("new_rating_description")
                .filmId(film.getId())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequest))
                )
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 403);
    }

    @Test
    @Transactional
    void canAccessOtherRoutes() throws Exception {
        // Given
        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/film"))
                .andReturn();

        // Then
        int status = mvcResult.getResponse().getStatus();

        assertEquals(status, 200);
    }
}