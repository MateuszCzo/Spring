package mc.project.filmBase.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    @Transactional
    void getRating() throws Exception {
        // Given
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
    void getRatings() throws Exception {
        // Given
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

        assertNotEquals(ratingResponse.size(), 0);
    }

    @Test
    @Transactional
    void updateRatingStatus() throws Exception {
        // Given
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
                .build();

        ratingRepository.save(rating);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/admin/rating/" + rating.getId() + "/films"))
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
}