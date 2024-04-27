package mc.project.filmBase.controller.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.enums.UserRole;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.repository.UserRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RatingFrontControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void getRating() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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
                .film(film)
                .status(RatingStatus.NOT_CONFIRMED)
                .description("rating_description")
                .rating(5)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/rating/" + rating.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        RatingResponse ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                RatingResponse.class
        );

        assertNotNull(ratingResponse);
        assertEquals(rating.getId(), ratingResponse.getId());
        assertEquals(rating.getRating(), ratingResponse.getRating());
        assertEquals(rating.getDescription(), ratingResponse.getDescription());
        assertEquals(rating.getStatus(), ratingResponse.getStatus());
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void addRating() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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
                .andExpect(status().is(200))
                .andReturn();

        // Then
        RatingResponse ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                RatingResponse.class
        );

        assertNotNull(ratingResponse);
        assertEquals(ratingRequest.getRating(), ratingResponse.getRating());
        assertEquals(ratingRequest.getDescription(), ratingResponse.getDescription());
        assertEquals(RatingStatus.NOT_CONFIRMED, ratingResponse.getStatus());
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void updateRating() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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
                .andExpect(status().is(200))
                .andReturn();

        // Then
        RatingResponse ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                RatingResponse.class
        );

        assertNotNull(ratingResponse);
        assertEquals(rating.getId(), ratingResponse.getId());
        assertEquals(ratingRequest.getRating(), ratingResponse.getRating());
        assertEquals(ratingRequest.getDescription(), ratingResponse.getDescription());
        assertEquals(RatingStatus.NOT_CONFIRMED, ratingResponse.getStatus());
    }

    @Test
    @Transactional
    void getFilm() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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

        // When
        MvcResult mvcResult = mockMvc.perform(get("/rating/" + rating.getId() + "/film"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        FilmResponse filmResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                FilmResponse.class
        );

        assertNotNull(filmResponse);
        assertEquals(film.getId(), filmResponse.getId());
        assertEquals(film.getTitle(), filmResponse.getTitle());
        assertEquals(film.getDescription(), filmResponse.getDescription());
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void userCanNotAddMoreThanOneRating() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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
        RatingResponse ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                RatingResponse.class
        );

        assertNotNull(ratingResponse);
        assertEquals(rating.getId(), ratingResponse.getId());
        assertEquals(rating.getDescription(), ratingResponse.getDescription());
        assertEquals(rating.getStatus(), ratingResponse.getStatus());
    }

    @Test
    @Transactional
    @WithMockUser("username")
    void validateRating() throws Exception {
        // Given
        User user = User.builder()
                .username("username")
                .password("password")
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
                .rating(6)
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

        assertEquals(status, 400);
    }


    @Test
    @Transactional
    @WithMockUser("username1")
    void otherUserUpdatingRating() throws Exception {
        // Given
        User user1 = User.builder()
                .username("username1")
                .password("password1")
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .username("username2")
                .password("password2")
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .role(UserRole.USER)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword(), user1.getAuthorities());

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
                .user(user2)
                .build();

        ratingRepository.save(rating);

        RatingRequest ratingRequest = RatingRequest.builder()
                .id(rating.getId())
                .rating(3)
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
}
