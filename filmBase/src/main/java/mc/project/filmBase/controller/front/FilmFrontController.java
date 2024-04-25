package mc.project.filmBase.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.service.front.FilmFrontService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/film")
@RequiredArgsConstructor
public class FilmFrontController {
    private final FilmFrontService filmService;

    @GetMapping("/{id}")
    public FilmResponse getFilm(@PathVariable("id") long id) {
        return filmService.get(id);
    }

    @GetMapping()
    public Collection<FilmResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return filmService.getPage(page);
    }

    @GetMapping("/{id}/actors")
    public Collection<ActorResponse> getActors(@PathVariable("id") long id, @RequestParam(name = "page", defaultValue = "0") int page) {
        return filmService.getActors(id, page);
    }

    @GetMapping("/{id}/ratings")
    public Collection<RatingResponse> getRatings(@PathVariable("id") long id, @RequestParam(name = "page", defaultValue = "0") int page) {
        return filmService.getConfirmedRatings(id, page);
    }
}
