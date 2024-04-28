package mc.project.filmBase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.ActorRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.mapper.ActorMapper;
import mc.project.filmBase.mapper.FilmMapper;
import mc.project.filmBase.model.Actor;
import mc.project.filmBase.model.Film;
import mc.project.filmBase.repository.ActorRepository;
import mc.project.filmBase.repository.FilmRepository;
import mc.project.filmBase.service.admin.ActorAdminService;
import mc.project.filmBase.service.cache.CacheService;
import mc.project.filmBase.service.front.ActorFrontService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorAdminService, ActorFrontService {
    public static final int PAGE_SIZE = 20;

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;
    private final ActorMapper actorMapper;
    private final FilmMapper filmMapper;
    private final CacheService cacheService;

    @Transactional
    @Cacheable("Actor")
    public ActorResponse get(long id) {
        Actor actor = actorRepository.findById(id).orElseThrow();

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    @Cacheable("ActorPage")
    public Collection<ActorResponse> getPage(int page) {
        Collection<Actor> actors = actorRepository.findAllActors(
                PageRequest.of(page, PAGE_SIZE)
        );

        return actorMapper.mapToActorResponse(actors);
    }

    @Transactional
    @CachePut(value = "Actor", key = "#result.id")
    public ActorResponse add(ActorRequest actorRequest) {
        Collection<Film> films = filmRepository.findAllById(actorRequest.getFilmIds());

        Actor actor = Actor.builder()
                .firstname(actorRequest.getFirstname())
                .lastname(actorRequest.getLastname())
                .films(films)
                .build();

        actorRepository.save(actor);

        cacheService.evictActorPages();
        films.forEach(film -> cacheService.evictFilmActors(film.getId()));

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    public void delete(long id) {
        Actor actor = actorRepository.findById(id).orElseThrow();

        cacheService.evictActor(id);
        cacheService.evictActorFilms(id);
        cacheService.evictActorPages();
        if (actor.getFilms() != null) actor.getFilms().forEach(film -> cacheService.evictFilmActors(film.getId()));

        actorRepository.deleteById(id);
    }

    @Transactional
    @CachePut(value = "Actor", key = "#result.id")
    public ActorResponse update(ActorRequest actorRequest) {
        Collection<Film> films = filmRepository.findAllById(actorRequest.getFilmIds());

        Actor actor = actorRepository.findById(actorRequest.getId()).orElseThrow();
        actor.setFirstname(actorRequest.getFirstname());
        actor.setLastname(actorRequest.getLastname());
        actor.setFilms(films);

        actorRepository.save(actor);

        cacheService.evictActorFilms(actor.getId());
        cacheService.evictActorPages();
        films.forEach(film -> cacheService.evictFilmActors(film.getId()));

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    @Cacheable("ActorFilms")
    public Collection<FilmResponse> getFilms(long id, int page) {
        Actor actor = actorRepository.findById(id).orElseThrow();

        Collection<Film> films = filmRepository.findAllByActors(List.of(actor), PageRequest.of(page, PAGE_SIZE));

        return filmMapper.mapToFilmResponse(films);
    }
}
