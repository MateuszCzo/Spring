package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;
import mc.project.online_store.service.admin.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ContactAdminController {
    private final ContactService contactService;

    @GetMapping("/user/{id}/contact")
    public ResponseEntity<List<ContactResponse>> getPageByUserId(
            @PathVariable(name = "id") long userId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ContactResponse> response = contactService.getPageByUserId(userId, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/contact/{id}")
    public ResponseEntity<ContactResponse> getContact(
            @PathVariable(name = "id") long id) {

        ContactResponse response = contactService.getContact(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{id}/contact")
    public ResponseEntity<ContactResponse> postContact(
            @PathVariable(name = "id") long userId,
            @Valid @RequestBody ContactRequest request) {

        ContactResponse response = contactService.postContact(userId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/contact/{id}")
    public ResponseEntity<ContactResponse> putContact(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody ContactRequest request) {

        ContactResponse response = contactService.putContact(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/contact/{id}")
    public void deleteContact(
            @PathVariable(name = "id") long id) {

        contactService.deleteContact(id);
    }
}