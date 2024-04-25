package mc.project.filmBase.service.front;

import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;

import java.util.Collection;

public interface FilmFrontService {
    public FilmResponse get(long id);

    public Collection<FilmResponse> getPage(int page);

    public Collection<ActorResponse> getActors(long id);

    public Collection<RatingResponse> getRatings(long id);
}
