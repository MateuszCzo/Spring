package mc.project.filmBase.service.front;

import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;

import java.util.Collection;

public interface ActorFrontService {
    public ActorResponse get(long id);

    public Collection<ActorResponse> getPage(int page);

    public Collection<FilmResponse> getFilms(long id, int page);
}
