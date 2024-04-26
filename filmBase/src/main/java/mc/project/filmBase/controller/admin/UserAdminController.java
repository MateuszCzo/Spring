package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.UserLockedRequest;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.service.admin.UserAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userService;

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable(name = "id") long id) {
        return userService.get(id);
    }

    @GetMapping
    public Collection<UserResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return userService.getPage(page);
    }

    @PutMapping
    public UserResponse updateAccountNonLocked(@RequestBody UserLockedRequest userLockedRequest) {
        return userService.update(userLockedRequest);
    }

    @GetMapping("/{id}/ratings")
    public Collection<RatingResponse> getRatings(@PathVariable(name = "id") long id, @RequestParam(name = "page", defaultValue = "0") int page) {
        return userService.getRatings(id, page);
    }
}
