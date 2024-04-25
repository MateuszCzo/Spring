package mc.project.filmBase.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.FilmRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.repository.ActorRepository;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmAdminControllerTest {

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

    @Test
    @Transactional
    void getFilm() throws Exception {
        // Given
        Film newFilm = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        filmRepository.save(newFilm);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film/" + newFilm.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        FilmResponse filmResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FilmResponse.class);

        assertEquals(filmResponse.getId(), newFilm.getId());
        assertEquals(filmResponse.getTitle(), newFilm.getTitle());
        assertEquals(filmResponse.getDescription(), newFilm.getDescription());
        assertEquals(filmResponse.getStatus(), newFilm.getStatus());
    }

    @Test
    @Transactional
    void getPage() throws Exception {
        // Given
        Film newFilm = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        filmRepository.save(newFilm);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<FilmResponse> filmResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<FilmResponse>>() {}
        );

        assertNotEquals(filmResponses.size(), 0);
    }

    @Test
    @Transactional
    void addFilm() throws Exception {
        // Given
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        actorRepository.save(actor);

        FilmRequest filmRequest = FilmRequest.builder()
                .actorIds(List.of(actor.getId()))
                .title("new_film_description")
                .description("new_film_description")
                .status(FilmStatus.AFTER_PREMIERE)
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/admin/film")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<FilmResponse> filmResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Optional<FilmResponse>>() {}
        );

        assertTrue(filmResponse.isPresent());

        Film film = filmRepository.findById(filmResponse.get().getId()).orElseThrow();

        assertEquals(filmRequest.getTitle(), film.getTitle());
        assertEquals(filmRequest.getTitle(), filmResponse.get().getTitle());
        assertEquals(filmRequest.getDescription(), film.getDescription());
        assertEquals(filmRequest.getDescription(), filmResponse.get().getDescription());
        assertEquals(filmRequest.getStatus(), film.getStatus());
        assertEquals(filmRequest.getStatus(), filmResponse.get().getStatus());

        Collection<Actor> actors = film.getActors();

        assertEquals(actors.size(), 1);
        assertEquals(actors.stream().findFirst().orElseThrow().getId(), actor.getId());
    }

    @Test
    @Transactional
    void deleteFilm() throws Exception {
        // Given
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .actors(List.of(actor))
                .build();

        actorRepository.save(actor);
        filmRepository.save(film);

        // When
        mockMvc.perform(delete("/admin/film/" + film.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<Film> deletedFilm = filmRepository.findById(film.getId());
        Optional<Actor> savedActor = actorRepository.findById(actor.getId());

        assertTrue(deletedFilm.isEmpty());
        assertTrue(savedActor.isPresent());
    }

    @Test
    @Transactional
    void updateFilm() throws Exception {
        // Given
        Actor actor1 = Actor.builder()
                .firstname("actor_firstname1")
                .lastname("actor_lastname1")
                .build();

        Actor actor2 = Actor.builder()
                .firstname("actor_firstname2")
                .lastname("actor_lastname2")
                .build();

        Actor actor3 = Actor.builder()
                .firstname("actor_firstname3")
                .lastname("actor_lastname3")
                .build();

        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .actors(List.of(actor1, actor2))
                .build();

        actorRepository.save(actor1);
        actorRepository.save(actor2);
        actorRepository.save(actor3);
        filmRepository.save(film);

        FilmRequest filmRequest = FilmRequest.builder()
                .id(film.getId())
                .status(FilmStatus.AFTER_PREMIERE)
                .title("new_film_title")
                .description("new_film_description")
                .actorIds(List.of(actor2.getId(), actor3.getId()))
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/film")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<FilmResponse> filmResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Optional<FilmResponse>>() {}
        );

        Film savedFilm = filmRepository.findById(film.getId()).orElseThrow();

        assertTrue(filmResponse.isPresent());
        assertEquals(filmRequest.getId(), savedFilm.getId());
        assertEquals(filmRequest.getId(), filmResponse.get().getId());
        assertEquals(filmRequest.getTitle(), savedFilm.getTitle());
        assertEquals(filmRequest.getTitle(), filmResponse.get().getTitle());
        assertEquals(filmRequest.getDescription(), savedFilm.getDescription());
        assertEquals(filmRequest.getDescription(), filmResponse.get().getDescription());
        assertEquals(filmRequest.getStatus(), savedFilm.getStatus());
        assertEquals(filmRequest.getStatus(), filmResponse.get().getStatus());
        assertEquals(List.of(actor2, actor3), savedFilm.getActors());
    }

    @Test
    @Transactional
    void getActors() throws Exception {
        // Given
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        Film newFilm = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .actors(List.of(actor))
                .build();

        actorRepository.save(actor);
        filmRepository.save(newFilm);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film/" + newFilm.getId() + "/actors"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<ActorResponse> actorResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<ActorResponse>>() {}
        );

        assertEquals(actorResponses.size(), 1);

        ActorResponse firstActorResponse = actorResponses.stream().findFirst().get();

        assertEquals(firstActorResponse.getId(), actor.getId());
        assertEquals(firstActorResponse.getFirstname(), actor.getFirstname());
        assertEquals(firstActorResponse.getLastname(), actor.getLastname());
    }

    @Test
    // @Transactional
    void getRatings() throws Exception {
        // Given
        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        filmRepository.save(film);

        Rating rating = Rating.builder()
                .rating(4)
                .description("rating_description")
                .status(RatingStatus.CONFIRMED)
                .film(film)
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/film/" + film.getId() + "/ratings"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<RatingResponse> ratingResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<RatingResponse>>() {}
        );

        assertEquals(ratingResponses.size(), 1);

        RatingResponse firstRatingResponse = ratingResponses.stream().findFirst().get();

        assertEquals(rating.getId(), firstRatingResponse.getId());
        assertEquals(rating.getRating(), firstRatingResponse.getRating());
        assertEquals(rating.getDescription(), firstRatingResponse.getDescription());
        assertEquals(rating.getStatus(), firstRatingResponse.getStatus());
    }
}