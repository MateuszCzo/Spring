package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attribute_type")
@RequiredArgsConstructor
public class AttributeTypeFrontController {
    @GetMapping()
    public ResponseEntity<?> get() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}