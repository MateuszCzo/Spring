package mc.project.filmBase.service.admin;

import mc.project.filmBase.dto.request.UserLockedRequest;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;

import java.util.Collection;

public interface UserAdminService {
    public UserResponse get(long id);

    public Collection<UserResponse> getPage(int page);

    public UserResponse updateStatus(UserLockedRequest userLockedRequest);

    public Collection<RatingResponse> getRatings(long id, int page);
}
