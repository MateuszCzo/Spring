package mc.project.filmBase.service.admin;

import mc.project.filmBase.dto.request.ActorRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;

import java.util.Collection;

public interface ActorAdminService {
    public ActorResponse get(long id);

    public Collection<ActorResponse> getPage(int page);

    public ActorResponse add(ActorRequest actorRequest);

    public void delete(long id);

    public ActorResponse update(ActorRequest actorRequest);

    public Collection<FilmResponse> getFilms(long id);
}
