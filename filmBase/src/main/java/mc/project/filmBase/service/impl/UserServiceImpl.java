package mc.project.filmBase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.UserLockedRequest;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.mapper.RatingMapper;
import mc.project.filmBase.mapper.UserMapper;
import mc.project.filmBase.model.Rating;
import mc.project.filmBase.model.User;
import mc.project.filmBase.repository.RatingRepository;
import mc.project.filmBase.repository.UserRepository;
import mc.project.filmBase.service.admin.UserAdminService;
import mc.project.filmBase.service.auth.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserAdminService, UserService {
    public static final int PAGE_SIZE = 20;

    private final UserRepository userResponse;
    private final UserMapper userMapper;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Transactional
    public UserResponse get(long id) {
        User user = userResponse.findById(id).orElseThrow();

        return userMapper.mapToUserResponse(user);
    }

    @Transactional
    public Collection<UserResponse> getPage(int page) {
        Collection<User> users = userResponse.findAllUsers(
                PageRequest.of(page, PAGE_SIZE)
        );

        return userMapper.mapToUserResponse(users);
    }

    @Transactional
    public UserResponse update(UserLockedRequest userLockedRequest) {
        User user = userResponse.findById(userLockedRequest.getId()).orElseThrow();

        user.setAccountNonLocked(userLockedRequest.isAccountNonLocked());

        userResponse.save(user);

        return userMapper.mapToUserResponse(user);
    }

    @Transactional
    public Collection<RatingResponse> getRatings(long id, int page) {
        User user = userResponse.findById(id).orElseThrow();

        Collection<Rating> ratings = ratingRepository.findAllByUser(
                user,
                PageRequest.of(page, PAGE_SIZE)
        );

        return ratingMapper.mapToRatingResponse(ratings);
    }

    @Transactional
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        if (userDetails == null) throw new NullPointerException();

        return userResponse.findByUsername(userDetails.getUsername()).orElseThrow();
    }
}
