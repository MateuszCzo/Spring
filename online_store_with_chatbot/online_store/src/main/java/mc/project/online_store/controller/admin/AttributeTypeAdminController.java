package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttributeTypeRequest;
import mc.project.online_store.dto.response.AttributeTypeResponse;
import mc.project.online_store.service.admin.AttributeTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/attribute_type")
@RequiredArgsConstructor
public class AttributeTypeAdminController {
    private final AttributeTypeService attributeTypeService;

    @GetMapping()
    public ResponseEntity<List<AttributeTypeResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AttributeTypeResponse> responses = attributeTypeService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("{id}")
    public ResponseEntity<AttributeTypeResponse> getAttributeType(
            @PathVariable(name = "id") long id) {

        AttributeTypeResponse response = attributeTypeService.getAttributeType(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<AttributeTypeResponse> postAttributeType(
            @RequestBody @Valid AttributeTypeRequest request) {

        AttributeTypeResponse response = attributeTypeService.postAttributeType(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<AttributeTypeResponse> putAttributeType(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid AttributeTypeRequest request) {

        AttributeTypeResponse response = attributeTypeService.putAttributeType(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public void deleteAttributeType(
            @PathVariable(name = "id") long id) {

        attributeTypeService.deleteAttributeType(id);
    }
}