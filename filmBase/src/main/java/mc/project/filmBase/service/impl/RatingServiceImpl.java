package mc.project.filmBase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.mapper.FilmMapper;
import mc.project.filmBase.mapper.RatingMapper;
import mc.project.filmBase.mapper.UserMapper;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.service.admin.RatingAdminService;
import mc.project.filmBase.service.auth.UserService;
import mc.project.filmBase.service.front.RatingFrontService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingAdminService, RatingFrontService {
    public static final int PAGE_SIZE = 20;

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final FilmMapper filmMapper;
    private final FilmRepository filmRepository;
    private final UserMapper userMapper;
    private final UserService userService;

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
        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) throw new IllegalArgumentException();

        User user = userService.getLoggedInUser();

        Film film = filmRepository.findById(ratingRequest.getFilmId()).orElseThrow();

        Optional<Rating> alreadyExistingRating = ratingRepository.findByUserAndFilm(user, film);

        if (alreadyExistingRating.isPresent()) return ratingMapper.mapToRatingResponse(alreadyExistingRating.get());

        Rating rating = Rating.builder()
                .film(film)
                .user(user)
                .description(ratingRequest.getDescription())
                .rating(ratingRequest.getRating())
                .status(RatingStatus.NOT_CONFIRMED)
                .build();

        ratingRepository.save(rating);

        return ratingMapper.mapToRatingResponse(rating);
    }

    @Transactional
    public RatingResponse update(RatingRequest ratingRequest) throws AccessDeniedException {
        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) throw new IllegalArgumentException();

        User user = userService.getLoggedInUser();

        Rating rating = ratingRepository.findById(ratingRequest.getId()).orElseThrow();

        if (rating.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("Access denied.");
        }

        rating.setRating(ratingRequest.getRating());
        rating.setDescription(ratingRequest.getDescription());
        rating.setStatus(RatingStatus.NOT_CONFIRMED);

        ratingRepository.save(rating);

        return ratingMapper.mapToRatingResponse(rating);
    }

    @Transactional
    public Collection<RatingResponse> getByStatus(RatingStatus status, int page) {
        Collection<Rating> ratings;

        if (status == null) {
            ratings = ratingRepository.findAllRatings(
                    PageRequest.of(page, PAGE_SIZE)
            );
        } else {
            ratings = ratingRepository.findAllByStatus(
                    status,
                    PageRequest.of(page, PAGE_SIZE)
            );
        }

        return ratingMapper.mapToRatingResponse(ratings);
    }

    @Transactional
    public UserResponse getUser(long id) {
        Rating rating = ratingRepository.findById(id).orElseThrow();

        return userMapper.mapToUserResponse(rating.getUser());
    }
}
