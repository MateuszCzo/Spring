package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressFrontController {
    @GetMapping()
    public ResponseEntity<?> get() {
        return ResponseEntity.ok("test");
    }
}
