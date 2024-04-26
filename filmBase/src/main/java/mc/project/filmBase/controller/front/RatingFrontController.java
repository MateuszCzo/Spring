package mc.project.filmBase.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.RatingRequest;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.service.front.RatingFrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingFrontController {
    private final RatingFrontService ratingService;

    @GetMapping("/{id}")
    public RatingResponse getRating(@PathVariable("id") long id) {
        return ratingService.get(id);
    }

    @PostMapping()
    public ResponseEntity<RatingResponse> addRating(@RequestBody RatingRequest ratingRequest) {
        try {
            return ResponseEntity.ok(ratingService.add(ratingRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping()
    public ResponseEntity<RatingResponse> updateRating(@RequestBody RatingRequest ratingRequest) {
        try {
            return ResponseEntity.ok(ratingService.update(ratingRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}/film")
    public FilmResponse getFilm(@PathVariable("id") long id) {
        return ratingService.getFilm(id);
    }
}
