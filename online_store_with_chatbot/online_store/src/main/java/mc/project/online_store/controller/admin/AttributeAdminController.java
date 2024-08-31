package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttributeRequest;
import mc.project.online_store.dto.response.AttributeResponse;
import mc.project.online_store.service.admin.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AttributeAdminController {
    private final AttributeService attributeService;

    @GetMapping("/product/{id}/attribute")
    public ResponseEntity<List<AttributeResponse>> getAttributeListByProductId(
            @PathVariable(name = "id") long productId) {

        List<AttributeResponse> responses = attributeService.getAttributeListByProductId(productId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/attribute_type/{id}/attribute")
    public ResponseEntity<List<AttributeResponse>> getPageByAttributeTypeId(
            @PathVariable(name = "id") long attributeId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AttributeResponse> responses = attributeService.getPageByAttributeTypeId(attributeId, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/attribute/{id}")
    public ResponseEntity<AttributeResponse> getAttribute(
            @PathVariable(name = "id") long id) {

        AttributeResponse response = attributeService.getAttribute(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/attribute")
    public ResponseEntity<AttributeResponse> postAttribute(
            @RequestBody @Valid AttributeRequest request) {

        AttributeResponse response = attributeService.postAttribute(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/attribute/{id}")
    public ResponseEntity<AttributeResponse> putAttribute(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid AttributeRequest request) {

        AttributeResponse response = attributeService.putAttribute(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/attribute/{id}")
    public void deleteAttribute(
            @PathVariable(name = "id") long id) {

        attributeService.deleteAttribute(id);
    }
}