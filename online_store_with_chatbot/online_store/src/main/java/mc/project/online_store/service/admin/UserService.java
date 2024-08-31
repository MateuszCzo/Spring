package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.UserRequest;
import mc.project.online_store.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getPage(String name, int page, int pageSize);

    UserResponse getUser(long id);

    UserResponse getUserByOrderId(long orderId);

    UserResponse putUser(long id, UserRequest request);

    void deleteUser(long id);
}
