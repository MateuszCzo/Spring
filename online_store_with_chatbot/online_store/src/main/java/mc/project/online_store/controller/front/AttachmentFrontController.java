package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;
import mc.project.online_store.service.front.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttachmentFrontController {
    public final AttachmentService attachmentService;

    @GetMapping("/attachment")
    public ResponseEntity<List<AttachmentResponse>> getPage(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AttachmentResponse> responses = attachmentService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{id}/attachment")
    public ResponseEntity<List<AttachmentResponse>> getAttachmentListByProductId(
            @PathVariable(name = "id") long productId) {

        List<AttachmentResponse> responses = attachmentService.getAttachmentListByProductId(productId);

        return ResponseEntity.ok(responses);
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
}
