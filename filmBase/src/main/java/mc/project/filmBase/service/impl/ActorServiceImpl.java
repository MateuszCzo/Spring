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
import mc.project.filmBase.service.ActorManipulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorManipulationService {
    public static final int PAGE_SIZE = 20;

    private final ActorRepository actorRepository;
    private final FilmRepository filmRepository;
    private final ActorMapper actorMapper;
    private final FilmMapper filmMapper;

    @Transactional
    public ActorResponse get(long id) {
        Actor actor = actorRepository.findById(id).orElseThrow();

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    public Collection<ActorResponse> getPage(int page) {
        Collection<Actor> actors = actorRepository.findAllActors(
                PageRequest.of(page, PAGE_SIZE)
        );

        return actorMapper.mapToActorResponse(actors);
    }

    @Transactional
    public ActorResponse add(ActorRequest actorRequest) {
        Collection<Film> films = filmRepository.findAllById(actorRequest.getFilmIds());

        Actor actor = Actor.builder()
                .firstname(actorRequest.getFirstname())
                .lastname(actorRequest.getLastname())
                .films(films)
                .build();

        actorRepository.save(actor);

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    public void delete(long id) {
        actorRepository.deleteById(id);
    }

    @Transactional
    public ActorResponse update(ActorRequest actorRequest) {
        Collection<Film> films = filmRepository.findAllById(actorRequest.getFilmIds());

        Actor actor = actorRepository.findById(actorRequest.getId()).orElseThrow();
        actor.setFirstname(actorRequest.getFirstname());
        actor.setLastname(actorRequest.getLastname());
        actor.setFilms(films);

        actorRepository.save(actor);

        return actorMapper.mapToActorResponse(actor);
    }

    @Transactional
    public Collection<FilmResponse> getFilms(long id) {
        Actor actor = actorRepository.findById(id).orElseThrow();

        return filmMapper.mapToFilmResponse(actor.getFilms());
    }
}
