package mc.project.filmBase.controller.admin;

import lombok.RequiredArgsConstructor;
import mc.project.filmBase.dto.request.ActorRequest;
import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.dto.response.FilmResponse;
import mc.project.filmBase.service.admin.ActorAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/admin/actor")
@RequiredArgsConstructor
public class ActorAdminController {
    private final ActorAdminService actorService;

    @GetMapping("/{id}")
    public ActorResponse getActor(@PathVariable(name = "id") long id) {
        return actorService.get(id);
    }

    @GetMapping
    public Collection<ActorResponse> getPage(@RequestParam(name = "page", defaultValue = "0") int page) {
        return actorService.getPage(page);
    }

    @PostMapping
    public ActorResponse addActor(@RequestBody ActorRequest actorRequest) {
        return actorService.add(actorRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable(name = "id") long id) {
        actorService.delete(id);
    }

    @PutMapping
    public ActorResponse updateActor(@RequestBody ActorRequest actorRequest) {
        return actorService.update(actorRequest);
    }

    @GetMapping("/{id}/films")
    public Collection<FilmResponse> getFilms(@PathVariable(name = "id") long id) {
        return actorService.getFilms(id);
    }
}
