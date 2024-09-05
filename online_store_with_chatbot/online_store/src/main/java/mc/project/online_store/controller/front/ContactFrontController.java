package mc.project.online_store.controller.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;
import mc.project.online_store.service.front.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactFrontController {
    private final ContactService contactService;

    @GetMapping("/user/contact")
    public ResponseEntity<List<ContactResponse>> getUserPage(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ContactResponse> responses = contactService.getUserPage(page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/contact/{id}")
    public ResponseEntity<ContactResponse> getUserContact(
            @PathVariable(name = "id") long id) {

        ContactResponse response = contactService.getUserContact(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/contact")
    public ResponseEntity<ContactResponse> postUserContact(
            @Valid @RequestBody ContactRequest request) {

        ContactResponse response = contactService.postUserContact(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/contact/{id}")
    public ResponseEntity<ContactResponse> putUserContact(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody ContactRequest request) {

        ContactResponse response = contactService.putUserContact(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/contact/{id}")
    public void deleteUserContact(
            @PathVariable(name = "id") long id) {

        contactService.deleteUserContact(id);
    }
}