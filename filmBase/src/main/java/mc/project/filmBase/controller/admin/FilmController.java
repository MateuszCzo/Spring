package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.FilmRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.service.FilmManipulationService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/film")
@RequiredArgsConstructor
public class FilmController {
    private final FilmManipulationService filmManipulationService;

    @GetMapping("/{id}")
    public FilmResponse getFilm(@PathVariable(name = "id") long id) {
        return filmManipulationService.get(id);
    }

    @GetMapping
    public Collection<FilmResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return filmManipulationService.getPage(page);
    }

    @PostMapping
    public FilmResponse addFilm(@RequestBody FilmRequest filmRequest) {
        return filmManipulationService.add(filmRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable(name = "id") long id) {
        filmManipulationService.delete(id);
    }

    @PutMapping
    public FilmResponse updateFilm(@RequestBody FilmRequest filmRequest) {
        return filmManipulationService.update(filmRequest);
    }

    @GetMapping("/{id}/actors")
    public Collection<ActorResponse> getActors(@PathVariable(name = "id") long id) {
        return filmManipulationService.getActors(id);
    }

    @GetMapping("{id}/ratings")
    public Collection<RatingResponse> getRatings(@PathVariable(name = "id") long id) {
        return filmManipulationService.getRatings(id);
    }
}
