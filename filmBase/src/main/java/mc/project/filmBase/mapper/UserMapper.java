package mc.project.filmBase.mapper;

import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .role(user.getRole())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .username(user.getUsername())
                .build();
    }

    public Collection<UserResponse> mapToUserResponse(Collection<User> users) {
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }
}
