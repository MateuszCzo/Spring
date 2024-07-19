package mc.project.online_store;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final TestRepository testRepository;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("test")
    public ResponseEntity<TestEntity> addTest(@RequestBody TestEntity testEntity) {
        log.info(testEntity.getId() + testEntity.getTest());
        return ResponseEntity.ok(testRepository.save(testEntity));
    }

    @GetMapping("test")
    public ResponseEntity<List<TestEntity>> getTests() {
        return ResponseEntity.ok(testRepository.findAll());
    }
}
