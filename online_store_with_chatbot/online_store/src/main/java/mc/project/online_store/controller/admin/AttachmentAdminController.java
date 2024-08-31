package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttachmentRequest;
import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;
import mc.project.online_store.service.admin.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AttachmentAdminController {
    private final AttachmentService attachmentService;

    @GetMapping("/attachment")
    public ResponseEntity<List<AttachmentResponse>> getPage(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AttachmentResponse> response = attachmentService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}/attachment")
    public ResponseEntity<List<AttachmentResponse>> getByProductId(
            @PathVariable(name = "id") long productId) {

        List<AttachmentResponse> response = attachmentService.getAttachmentListByProductId(productId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<AttachmentResponse> getAttachment(
            @PathVariable(name = "id") long id) {

        AttachmentResponse response = attachmentService.getAttachment(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/attachment/{id}/content")
    public ResponseEntity<Resource> getAttachmentContent(
            @PathVariable(name = "id") long id) {

        AttachmentContentResponse response = attachmentService.getAttachmentContent(id);

        return ResponseEntity.ok()
                .contentType(response.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getFileName() + "\"")
                .body(response.getContent());
    }

    @PostMapping("/attachment")
    public ResponseEntity<AttachmentResponse> postAttachment(
            @Valid @RequestBody AttachmentRequest attachmentRequest) {

        AttachmentResponse response = attachmentService.postAttachment(attachmentRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/attachment/{id}")
    public ResponseEntity<AttachmentResponse> putAttachment(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody AttachmentRequest request) {

        AttachmentResponse response = attachmentService.putAttachment(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/attachment/{id}")
    public void deleteAttachment(
            @PathVariable(name = "id") long id) {

        attachmentService.deleteAttachment(id);
    }
}
