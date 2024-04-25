package mc.project.filmBase.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.service.front.ActorFrontService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/actor")
@RequiredArgsConstructor
public class ActorFrontController {
    private final ActorFrontService actorService;

    @GetMapping("/{id}")
    public ActorResponse getActor(@PathVariable("id") long id) {
        return actorService.get(id);
    }

    @GetMapping
    public Collection<ActorResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return actorService.getPage(page);
    }

    @GetMapping("/{id}/films")
    public Collection<FilmResponse> getFilms(@PathVariable("id") long id, @RequestParam(name = "page", defaultValue = "0") int page) {
        return actorService.getFilms(id, page);
    }
}
