package mc.project.filmBase.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.UserLockedRequest;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(value = "admin", roles = {"ADMIN"})
class UserControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private FilmRepository filmRepository;

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

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/user/" + user.getId()))
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

    @Test
    @Transactional
    void getPage() throws Exception {
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

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/user"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<UserResponse> ratingResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<UserResponse>>() {}
        );

        assertNotEquals(0, ratingResponse.size());
    }

    @Test
    @Transactional
    void updateAccountNonLocked() throws Exception {
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

        UserLockedRequest userLockedRequest = UserLockedRequest.builder()
                .accountNonLocked(false)
                .id(user.getId())
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLockedRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        UserResponse userResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserResponse.class
        );

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        assertNotNull(userResponse);
        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getRole(), userResponse.getRole());
        assertEquals(user.isAccountNonExpired(), userResponse.isAccountNonExpired());
        assertEquals(user.isEnabled(), userResponse.isEnabled());
        assertEquals(user.isAccountNonLocked(), userResponse.isAccountNonLocked());
        assertEquals(user.isCredentialsNonExpired(), userResponse.isCredentialsNonExpired());
        assertEquals(userLockedRequest.isAccountNonLocked(), updatedUser.isAccountNonLocked());
    }

    @Test
    @Transactional
    void getRatings() throws Exception {
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
        MvcResult mvcResult = mockMvc.perform(get("/admin/user/" + user.getId() + "/ratings"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<RatingResponse> ratingResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<RatingResponse>>() {}
        );

        assertNotNull(ratingResponses);
        assertEquals(ratingResponses.size(), 1);

        RatingResponse firstratingResponse = ratingResponses.stream().findFirst().orElseThrow();

        assertEquals(rating.getId(), firstratingResponse.getId());
        assertEquals(rating.getRating(), firstratingResponse.getRating());
        assertEquals(rating.getStatus(), firstratingResponse.getStatus());
        assertEquals(rating.getDescription(), firstratingResponse.getDescription());
    }
}