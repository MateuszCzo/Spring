package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingStatusRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.service.RatingManipulationService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingManipulationService ratingManipulationService;

    @GetMapping("/{id}")
    public RatingResponse getRating(@PathVariable(name = "id") long id) {
        return ratingManipulationService.get(id);
    }

    @GetMapping
    public Collection<RatingResponse> getRatings(@RequestParam(name = "page", defaultValue = "0") int page) {
        return ratingManipulationService.getPage(page);
    }

    @PutMapping
    public RatingResponse updateRatingStatus(@RequestBody RatingStatusRequest ratingStatusRequest) {
        return ratingManipulationService.updateStatus(ratingStatusRequest);
    }

    @GetMapping("/{id}/films")
    public FilmResponse getFilm(@PathVariable(name = "id") long id) {
        return ratingManipulationService.getFilm(id);
    }
}
