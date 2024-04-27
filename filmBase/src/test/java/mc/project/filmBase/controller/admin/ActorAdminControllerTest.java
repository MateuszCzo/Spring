package mc.project.filmBase.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.ActorRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.repository.ActorRepository;
import mc.project.filmBase.repository.FilmRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(value = "admin", roles = {"ADMIN"})
class ActorAdminControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private FilmRepository filmRepository;

    @Test
    @Transactional
    void getActor() throws Exception {
        // Given
        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        Actor newActor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .films(List.of(film))
                .build();

        filmRepository.save(film);
        actorRepository.save(newActor);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/actor/" + newActor.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        ActorResponse responseActor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorResponse.class);

        assertNotNull(responseActor);
        assertEquals(responseActor.getId(), newActor.getId());
        assertEquals(responseActor.getFirstname(), newActor.getFirstname());
        assertEquals(responseActor.getLastname(), newActor.getLastname());
    }

    @Test
    @Transactional
    void getDefaultPage() throws Exception {
        // Given
        Actor newActor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        actorRepository.save(newActor);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/actor"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<ActorResponse> responseActors = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<ActorResponse>>() {}
        );

        assertNotEquals(0, responseActors.size());
    }

    @Test
    @Transactional
    void addActor() throws Exception {
        // Given
        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        filmRepository.save(film);

        ActorRequest actorRequest = ActorRequest.builder()
                .lastname("actor_lastname")
                .firstname("actor_firstname")
                .filmIds(List.of(film.getId()))
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/admin/actor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(actorRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        ActorResponse responseActor = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ActorResponse.class
        );

        assertNotNull(responseActor);
        assertEquals(responseActor.getLastname(), actorRequest.getLastname());
        assertEquals(responseActor.getFirstname(), actorRequest.getFirstname());

        List<Actor> actors = actorRepository.findAllByFirstnameAndLastname(actorRequest.getFirstname(), actorRequest.getLastname());

        assertEquals(1, actors.size());

        Actor actor = actors.stream().findFirst().orElseThrow();

        assertEquals(1, actor.getFilms().size());

        assertEquals(actor.getFilms().stream().findFirst().orElseThrow().getId(), film.getId());
    }

    @Test
    @Transactional
    void deleteActor() throws Exception {
        // Given
        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .build();

        Actor newActor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .films(List.of(film))
                .build();

        filmRepository.save(film);
        actorRepository.save(newActor);

        // When
        mockMvc.perform(delete("/admin/actor/" + newActor.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Optional<Actor> deletedActor = actorRepository.findById(newActor.getId());
        Optional<Film> savedFilm = filmRepository.findById(film.getId());

        assertTrue(deletedActor.isEmpty());
        assertTrue(savedFilm.isPresent());
    }

    @Test
    @Transactional
    void updateActor() throws Exception {
        // Given
        Film film1 = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title1")
                .description("film_description1")
                .build();

        Film film2 = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title2")
                .description("film_description2")
                .build();

        Film film3 = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title3")
                .description("film_description3")
                .build();

        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .films(List.of(film1, film2))
                .build();

        filmRepository.save(film1);
        filmRepository.save(film2);
        filmRepository.save(film3);
        actorRepository.save(actor);

        ActorRequest actorRequest = ActorRequest.builder()
                .id(actor.getId())
                .lastname("new_actor_lastname")
                .firstname("new_actor_firstname")
                .filmIds(List.of(film2.getId(), film3.getId()))
                .build();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/actor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorRequest))
                )
                .andExpect(status().is(200))
                .andReturn();

        // Then
        ActorResponse actorResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ActorResponse.class
        );

        Optional<Actor> updatedActor = actorRepository.findById(actor.getId());

        assertNotNull(actorResponse);
        assertTrue(updatedActor.isPresent());
        assertEquals(actorRequest.getId(), actorResponse.getId());
        assertEquals(actorRequest.getId(), updatedActor.get().getId());
        assertEquals(actorRequest.getFirstname(), actorResponse.getFirstname());
        assertEquals(actorRequest.getFirstname(), updatedActor.get().getFirstname());
        assertEquals(actorRequest.getLastname(), actorResponse.getLastname());
        assertEquals(actorRequest.getLastname(), updatedActor.get().getLastname());

        Collection<Film> films = updatedActor.get().getFilms();

        assertEquals(List.of(film2, film3), films);

        Optional<Film> existingFilm1 = filmRepository.findById(film1.getId());

        assertTrue(existingFilm1.isPresent());
    }

    @Test
    @Transactional
    void getFilms() throws Exception {
        // Given
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        actorRepository.save(actor);

        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .title("film_title")
                .description("film_description")
                .actors(List.of(actor))
                .build();

        filmRepository.save(film);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/actor/" + actor.getId() + "/films"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<FilmResponse> filmResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<FilmResponse>>() {}
        );

        assertEquals(1, filmResponses.size());
        assertEquals(film.getTitle(), filmResponses.stream().findFirst().orElseThrow().getTitle());
    }

    @Test
    @Transactional
    void canNotGetNotExistingActor() throws Exception {
        // Given

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/admin/actor/0"));
        });

        // Then
        assertTrue(exception.getMessage().contains("No value present"));
    }

    @Test
    @Transactional
    void canNotGetNotExistingActorsFilms() throws Exception {
        // Given

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/admin/actor/0/films"));
        });

        // Then
        assertTrue(exception.getMessage().contains("No value present"));
    }
}