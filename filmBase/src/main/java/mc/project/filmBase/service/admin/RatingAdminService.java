package mc.project.filmBase.service.admin;

import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.enums.RatingStatus;

import java.util.Collection;

public interface RatingAdminService {
    public RatingResponse get(long id);

    public Collection<RatingResponse> getPage(int page);

    public Collection<RatingResponse> getByStatus(RatingStatus status, int page);

    public RatingResponse updateStatus(RatingStatusRequest ratingStatusRequest);

    public FilmResponse getFilm(long id);

    public UserResponse getUser(long id);
}
