package mc.project.filmBase.controller.front;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Collection;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ActorFrontControllerTest {
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
        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .build();

        actorRepository.save(actor);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/actor/" + actor.getId()))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        ActorResponse actorResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ActorResponse.class
        );

        assertNotNull(actorResponse);
        assertEquals(actor.getId(), actorResponse.getId());
        assertEquals(actor.getFirstname(), actorResponse.getFirstname());
        assertEquals(actor.getLastname(), actorResponse.getLastname());
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
        MvcResult mvcResult = mockMvc.perform(get("/actor"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<Actor> responseActors = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<Actor>>() {}
        );

        assertNotEquals(responseActors.size(), 0);
    }

    @Test
    @Transactional
    void getFilms() throws Exception {
        // Given
        Film film = Film.builder()
                .status(FilmStatus.AFTER_PREMIERE)
                .description("film_description")
                .title("film_title")
                .build();

        Actor actor = Actor.builder()
                .firstname("actor_firstname")
                .lastname("actor_lastname")
                .films(List.of(film))
                .build();


        filmRepository.save(film);
        actorRepository.save(actor);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/actor/" + actor.getId() + "/films"))
                .andExpect(status().is(200))
                .andReturn();

        // Then
        Collection<FilmResponse> filmResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Collection<FilmResponse>>() {}
        );

        assertNotEquals(filmResponses.size(), 0);
    }
}