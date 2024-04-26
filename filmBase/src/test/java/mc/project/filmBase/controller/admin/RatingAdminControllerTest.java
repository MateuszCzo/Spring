package mc.project.filmBase.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.enums.UserRole;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.ActorRepository;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "admin", roles = {"ADMIN"})
class RatingAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private RatingRepository ratingRepository;
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
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating/" + rating.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<RatingResponse> ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Optional<RatingResponse>>() {}
        );

        assertTrue(ratingResponse.isPresent());

        assertEquals(rating.getId(), ratingResponse.get().getId());
        assertEquals(rating.getRating(), ratingResponse.get().getRating());
        assertEquals(rating.getStatus(), ratingResponse.get().getStatus());
        assertEquals(rating.getDescription(), ratingResponse.get().getDescription());
    }

    @Test
    @Transactional
    void getPage() throws Exception {
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
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<RatingResponse> ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<RatingResponse>>() {}
        );

        assertNotEquals(0, ratingResponse.size());
    }

    @Test
    @Transactional
    void getNotConfirmedRatingsPage() throws Exception {
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

        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        Rating rating1 = Rating.builder()
                .rating(4)
                .description("rating_description1")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user1)
                .build();

        Rating rating2 = Rating.builder()
                .rating(2)
                .description("rating_description2")
                .status(RatingStatus.NOT_CONFIRMED)
                .film(film)
                .user(user2)
                .build();

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating?status=NOT_CONFIRMED"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<RatingResponse> ratingResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<RatingResponse>>() {}
        );

        assertNotEquals(0, ratingResponses.size());

        for (RatingResponse ratingResponse : ratingResponses) {
            assertEquals(RatingStatus.NOT_CONFIRMED, ratingResponse.getStatus());
        }
    }

    @Test
    @Transactional
    void updateRatingStatus() throws Exception {
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
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        RatingStatusRequest ratingStatusRequest = RatingStatusRequest.builder()
                .id(rating.getId())
                .status(RatingStatus.NOT_CONFIRMED)
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingStatusRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<RatingResponse> ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Optional<RatingResponse>>() {}
        );

        Optional<Rating> updaterRating = ratingRepository.findById(rating.getId());

        assertTrue(ratingResponse.isPresent());
        assertTrue(updaterRating.isPresent());

        assertEquals(rating.getId(), ratingResponse.get().getId());
        assertEquals(rating.getRating(), ratingResponse.get().getRating());
        assertEquals(ratingStatusRequest.getStatus(), ratingResponse.get().getStatus());
        assertEquals(rating.getDescription(), ratingResponse.get().getDescription());
        assertEquals(rating.getRating(), updaterRating.get().getRating());
        assertEquals(ratingStatusRequest.getStatus(), updaterRating.get().getStatus());
        assertEquals(rating.getDescription(), updaterRating.get().getDescription());
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
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating/" + rating.getId() + "/film"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<FilmResponse> filmResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Optional<FilmResponse>>() {}
        );

        assertTrue(filmResponse.isPresent());
        assertEquals(film.getId(), filmResponse.get().getId());
        assertEquals(film.getTitle(), filmResponse.get().getTitle());
        assertEquals(film.getDescription(), filmResponse.get().getDescription());
        assertEquals(film.getStatus(), filmResponse.get().getStatus());
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Given
        User user = User.builder()
                .username("user_name")
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
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating/" + rating.getId() + "/user"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        UserResponse userResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserResponse.class
        );

        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getRole(), userResponse.getRole());
        assertEquals(user.isAccountNonExpired(), userResponse.isAccountNonExpired());
        assertEquals(user.isEnabled(), userResponse.isEnabled());
        assertEquals(user.isAccountNonLocked(), userResponse.isAccountNonLocked());
        assertEquals(user.isCredentialsNonExpired(), userResponse.isCredentialsNonExpired());
    }
}