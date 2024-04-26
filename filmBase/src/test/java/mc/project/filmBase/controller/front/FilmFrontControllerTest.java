package mc.project.filmBase.controller.front;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.enums.UserRole;
import mc.project.filmBase.model.Actor;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmFrontControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void getFilm() throws Exception {
        // Given
        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/film/" + film.getId()))
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
        assertEquals(film.getStatus(), filmResponse.getStatus());
    }

    @Test
    @Transactional
    void getDefaultPage() throws Exception {
        // Given
        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/film"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<FilmResponse> filmResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<FilmResponse>>() {}
        );

        assertNotEquals(filmResponse.size(), 0);
    }

    @Test
    @Transactional
    void getActors() throws Exception {
        // Given
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        actorRepository.save(actor);

        Film film = Film.builder()
                .title("film_title")
                .description("film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .actors(List.of(actor))
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/film/" + film.getId() + "/actors"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<ActorResponse> actorResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<ActorResponse>>() {}
        );

        assertEquals(actorResponses.size(), 1);

        ActorResponse firstActorResponse = actorResponses.stream().findFirst().get();

        assertEquals(actor.getId(), firstActorResponse.getId());
        assertEquals(actor.getLastname(), firstActorResponse.getLastname());
        assertEquals(actor.getFirstname(), firstActorResponse.getFirstname());
    }

    @Test
    @Transactional
    void getRatings() throws Exception {
        // Given
        User user = User.builder()
                .username("test_user")
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
                .rating(3)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .user(user)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/film/" + film.getId() + "/ratings"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<RatingResponse> ratingResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<RatingResponse>>() {}
        );

        assertEquals(ratingResponses.size(), 1);

        RatingResponse firstRatingresponse = ratingResponses.stream().findFirst().get();

        assertEquals(rating.getId(), firstRatingresponse.getId());
        assertEquals(rating.getDescription(), firstRatingresponse.getDescription());
        assertEquals(rating.getRating(), firstRatingresponse.getRating());
        assertEquals(rating.getStatus(), firstRatingresponse.getStatus());
    }
}