package mc.project.filmBase.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.service.front.RatingFrontService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class RatingFrontController {
    private final RatingFrontService ratingService;

    @GetMapping("/{id}")
    public RatingResponse getRating(@PathVariable("id") long id) {
        return ratingService.get(id);
    }

    @PostMapping()
    public RatingResponse addRating(@RequestBody RatingRequest ratingRequest) {
        return ratingService.add(ratingRequest);
    }

    @PutMapping()
    public RatingResponse updateRating(@RequestBody RatingRequest ratingRequest) {
        return ratingService.update(ratingRequest);
    }

    @GetMapping("/{id}/film")
    public FilmResponse getFilm(@PathVariable("id") long id) {
        return ratingService.getFilm(id);
    }
}
