package mc.project.filmBase.repository;

import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query("select f from Film f")
    List<Film> findAllFilms(Pageable page);

    @Query("select f from Film f where title = :title")
    List<Film> findAllByTitle(String title);
}
