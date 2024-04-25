package mc.project.filmBase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.FilmRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.mapper.ActorMapper;
import mc.project.filmBase.mapper.FilmMapper;
import mc.project.filmBase.mapper.RatingMapper;
import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.repository.ActorRepository;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.service.admin.FilmAdminService;
import mc.project.filmBase.service.front.FilmFrontService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmAdminService, FilmFrontService {
    public static final int PAGE_SIZE = 20;

    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final RatingRepository ratingRepository;
    private final FilmMapper filmMapper;
    private final ActorMapper actorMapper;
    private final RatingMapper ratingMapper;

    @Transactional
    public FilmResponse get(long id) {
        Film film = filmRepository.findById(id).orElseThrow();

        return filmMapper.mapToFilmResponse(film);
    }

    @Transactional
    public Collection<FilmResponse> getPage(int page) {
        Collection<Film> films = filmRepository.findAllFilms(
                PageRequest.of(page, PAGE_SIZE)
        );

        return filmMapper.mapToFilmResponse(films);
    }

    @Transactional
    public FilmResponse add(FilmRequest filmRequest) {
        Collection<Actor> actors = actorRepository.findAllById(filmRequest.getActorIds());

        Film film = Film.builder()
                .title(filmRequest.getTitle())
                .description(filmRequest.getDescription())
                .actors(actors)
                .status(filmRequest.getStatus())
                .build();

        filmRepository.save(film);

        return filmMapper.mapToFilmResponse(film);
    }

    @Transactional
    public void delete(long id) {
        filmRepository.deleteById(id);
    }

    @Transactional
    public FilmResponse update(FilmRequest filmRequest) {
        Film film = filmRepository.findById(filmRequest.getId()).orElseThrow();

        Collection<Actor> actors = actorRepository.findAllById(filmRequest.getActorIds());

        film.setActors(actors);
        film.setTitle(filmRequest.getTitle());
        film.setDescription(filmRequest.getDescription());
        film.setStatus(filmRequest.getStatus());

        filmRepository.save(film);

        return filmMapper.mapToFilmResponse(film);
    }

    @Transactional
    public Collection<ActorResponse> getActors(long id, int page) {
        Film film = filmRepository.findById(id).orElseThrow();

        Collection<Actor> actors = actorRepository.findAllByFilms(List.of(film), PageRequest.of(page, PAGE_SIZE));

        return actorMapper.mapToActorResponse(actors);
    }

    @Transactional
    public Collection<RatingResponse> getRatings(long id, int page) {
        Film film = filmRepository.findById(id).orElseThrow();

        Collection<Rating> ratings = ratingRepository.findAllByFilm(film, PageRequest.of(page, PAGE_SIZE));

        return ratingMapper.mapToRatingResponse(ratings);
    }

    @Transactional
    public Collection<RatingResponse> getConfirmedRatings(long id, int page) {
        Film film = filmRepository.findById(id).orElseThrow();

        Collection<Rating> ratings = ratingRepository.findAllByFilmAndStatus(film, RatingStatus.CONFIRMED, PageRequest.of(page, PAGE_SIZE));

        return ratingMapper.mapToRatingResponse(ratings);
    }
}
