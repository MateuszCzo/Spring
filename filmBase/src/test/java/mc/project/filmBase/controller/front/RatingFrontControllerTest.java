package mc.project.filmBase.controller.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.FilmStatus;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilderSupport;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RatingFrontControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private FilmRepository filmRepository;

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
                .film(film)
                .status(RatingStatus.NOT_CONFIRMED)
                .description("rating_description")
                .rating(5)
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
    void addRating() throws Exception {
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
    void updateRating() throws Exception {
        // Given
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
}