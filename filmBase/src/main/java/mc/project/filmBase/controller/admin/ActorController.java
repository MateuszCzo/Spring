package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.ActorRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.service.ActorManipulationService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/actor")
@RequiredArgsConstructor
public class ActorController {
    private final ActorManipulationService actorManipulationService;

    @GetMapping("/{id}")
    public ActorResponse getActor(@PathVariable(name = "id") long id) {
        return actorManipulationService.get(id);
    }

    @GetMapping
    public Collection<ActorResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return actorManipulationService.getPage(page);
    }

    @PostMapping
    public ActorResponse addActor(@RequestBody ActorRequest actorRequest) {
        return actorManipulationService.add(actorRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable(name = "id") long id) {
        actorManipulationService.delete(id);
    }

    @PutMapping
    public ActorResponse updateActor(@RequestBody ActorRequest actorRequest) {
        return actorManipulationService.update(actorRequest);
    }

    @GetMapping("/{id}/films")
    public Collection<FilmResponse> getFilms(@PathVariable(name = "id") long id) {
        return actorManipulationService.getFilms(id);
    }
}
