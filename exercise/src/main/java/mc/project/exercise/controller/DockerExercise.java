package mc.project.exercise.controller;

import lombok.RequiredArgsConstructor;
import mc.project.exercise.model.Text;
import mc.project.exercise.repository.TextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/exercise2")
@RequiredArgsConstructor
public class DockerExercise {
    private final TextRepository textRepository;

    @PostMapping
    public Text postText(@RequestBody String content) {
        Text text = Text.builder()
                .content(content)
                .build();

        textRepository.save(text);

        return text;
    }

    @GetMapping
    public Collection<Text> getTexts() {
        return textRepository.findAll();
    }
}
