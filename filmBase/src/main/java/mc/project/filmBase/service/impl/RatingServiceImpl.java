package mc.project.filmBase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.mapper.FilmMapper;
import mc.project.filmBase.mapper.RatingMapper;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.service.admin.RatingAdminService;
import mc.project.filmBase.service.front.RatingFrontService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingAdminService, RatingFrontService {
    public static final int PAGE_SIZE = 20;

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final FilmMapper filmMapper;
    private final FilmRepository filmRepository;

    @Transactional
    public RatingResponse get(long id) {
        Rating rating = ratingRepository.findById(id).orElseThrow();

        return ratingMapper.mapToRatingResponse(rating);
    }

    @Transactional
    public Collection<RatingResponse> getPage(int page) {
        Collection<Rating> ratings = ratingRepository.findAllRatings(
                PageRequest.of(page, PAGE_SIZE)
        );

        return ratingMapper.mapToRatingResponse(ratings);
    }

    @Transactional
    public RatingResponse updateStatus(RatingStatusRequest ratingStatusRequest) {
        Rating rating = ratingRepository.findById(ratingStatusRequest.getId()).orElseThrow();

        rating.setStatus(ratingStatusRequest.getStatus());

        ratingRepository.save(rating);

        return ratingMapper.mapToRatingResponse(rating);
    }

    @Transactional
    public FilmResponse getFilm(long id) {
        Rating rating = ratingRepository.findById(id).orElseThrow();

        return filmMapper.mapToFilmResponse(rating.getFilm());
    }

    @Transactional
    public RatingResponse add(RatingRequest ratingRequest) {
        Film film = filmRepository.findById(ratingRequest.getFilmId()).orElseThrow();

        Rating rating = Rating.builder()
                .film(film)
                .status(ratingRequest.getStatus())
                .description(ratingRequest.getDescription())
                .rating(ratingRequest.getRating())
                .build();

        ratingRepository.save(rating);

        return ratingMapper.mapToRatingResponse(rating);
    }

    @Transactional
    public RatingResponse update(RatingRequest ratingRequest) {
        Rating rating = ratingRepository.findById(ratingRequest.getId()).orElseThrow();

        rating.setRating(ratingRequest.getRating());
        rating.setDescription(ratingRequest.getDescription());

        ratingRepository.save(rating);

        return ratingMapper.mapToRatingResponse(rating);
    }
}
