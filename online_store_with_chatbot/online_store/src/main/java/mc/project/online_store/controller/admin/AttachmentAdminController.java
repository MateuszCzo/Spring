package mc.project.online_store.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/attachment")
@RequiredArgsConstructor
public class AttachmentAdminController {
    @GetMapping()
    public ResponseEntity<?> get() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
