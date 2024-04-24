package mc.project.filmBase.mapper;

import mc.project.filmBase.dto.response.ActorResponse;
import mc.project.filmBase.model.Actor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorMapper {
    public ActorResponse mapToActorResponse(Actor actor) {
        return ActorResponse.builder()
                .id(actor.getId())
                .firstname(actor.getFirstname())
                .lastname(actor.getLastname())
                .build();
    }

    public List<ActorResponse> mapToActorResponse(Collection<Actor> actors) {
        if (actors == null) return Collections.emptyList();

        return actors.stream()
                .map(this::mapToActorResponse)
                .collect(Collectors.toList());
    }
}
