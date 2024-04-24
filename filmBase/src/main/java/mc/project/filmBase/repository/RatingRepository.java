package mc.project.filmBase.repository;

import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("select r from Rating r where film = :film")
    public Collection<Rating> findAllByFilm(Film film);

    @Query("select r from Rating r")
    List<Rating> findAllRatings(Pageable page);
}
