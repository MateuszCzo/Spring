package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.dto.response.UserResponse;
import mc.project.filmBase.enums.RatingStatus;
import mc.project.filmBase.service.admin.RatingAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/rating")
@RequiredArgsConstructor
public class RatingAdminController {
    private final RatingAdminService ratingService;

    @GetMapping("/{id}")
    public RatingResponse getRating(@PathVariable(name = "id") long id) {
        return ratingService.get(id);
    }

    @GetMapping
    public Collection<RatingResponse> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "status", required = false) RatingStatus status)
    {
        return ratingService.getByStatus(status, page);
    }

    @PutMapping
    public RatingResponse updateRatingStatus(@RequestBody RatingStatusRequest ratingStatusRequest) {
        return ratingService.updateStatus(ratingStatusRequest);
    }

    @GetMapping("/{id}/film")
    public FilmResponse getFilm(@PathVariable(name = "id") long id) {
        return ratingService.getFilm(id);
    }

    @GetMapping("/{id}/user")
    public UserResponse getUser(@PathVariable(name = "id") long id) {
        return ratingService.getUser(id);
    }
}
