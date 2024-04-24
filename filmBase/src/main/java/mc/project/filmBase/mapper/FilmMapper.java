package mc.project.filmBase.mapper;

import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.model.Film;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmMapper {
    public FilmResponse mapToFilmResponse(Film film) {
        return FilmResponse.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .status(film.getStatus())
                .build();
    }

    public List<FilmResponse> mapToFilmResponse(Collection<Film> films) {
        if (films == null) return Collections.emptyList();

        return films.stream()
                .map(this::mapToFilmResponse)
                .collect(Collectors.toList());
    }
}
