package mc.project.kafka.controller;

import lombok.RequiredArgsConstructor;
import mc.project.kafka.service.KafkaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaService kafkaService;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message) {
        kafkaService.sendMessage(message);
        return ResponseEntity.ok("Message sent");
    }
}
