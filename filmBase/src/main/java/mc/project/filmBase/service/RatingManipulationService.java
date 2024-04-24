package mc.project.filmBase.service;

import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;

import java.util.Collection;

public interface RatingManipulationService {
    public RatingResponse get(long id);

    public Collection<RatingResponse> getPage(int page);

    public RatingResponse updateStatus(RatingStatusRequest ratingStatusRequest);

    public FilmResponse getFilm(long id);
}
