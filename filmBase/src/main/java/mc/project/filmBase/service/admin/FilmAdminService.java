package mc.project.filmBase.service.admin;

import mc.project.filmBase.dto.request.FilmRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;

import java.util.Collection;

public interface FilmAdminService {
    public FilmResponse get(long id);

    public Collection<FilmResponse> getPage(int page);

    public FilmResponse add(FilmRequest filmRequest);

    public void delete(long id);

    public FilmResponse update(FilmRequest filmRequest);

    public Collection<ActorResponse> getActors(long id);

    public Collection<RatingResponse> getRatings(long id);
}
