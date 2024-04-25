package mc.project.filmBase.service.front;

import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;

public interface RatingFrontService {
    public RatingResponse get(long id);

    public RatingResponse add(RatingRequest ratingRequest);

    public RatingResponse update(RatingRequest ratingRequest);

    public FilmResponse getFilm(long id);
}
