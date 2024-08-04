package mc.project.online_store.service.auth;

import mc.project.online_store.model.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> getLoggedInUser();
}
