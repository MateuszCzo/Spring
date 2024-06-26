package mc.project.filmBase.repository;

import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByFilm(Film film, Pageable page);

    @Query("select r from Rating r")
    List<Rating> findAllRatings(Pageable page);

    List<Rating> findAllByStatus(RatingStatus status, Pageable page);

    List<Rating> findAllByFilmAndStatus(Film film, RatingStatus status, Pageable page);

    List<Rating> findAllByUser(User user, Pageable page);

    Optional<Rating> findByUserAndFilm(User user, Film film);
}
