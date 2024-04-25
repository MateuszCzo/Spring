package mc.project.filmBase.repository;

import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("select a from Actor a")
    List<Actor> findAllActors(Pageable page);

    List<Actor> findAllByFilms(Collection<Film> films, Pageable page);

    @Query("select a from Actor a where firstname = :firstname and lastname = :lastname")
    List<Actor> findAllByFirstnameAndLastname(String firstname, String lastname);
}
