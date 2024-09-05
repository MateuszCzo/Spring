package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.AttributeTypeResponse;
import mc.project.online_store.service.front.AttributeTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttributeTypeFrontController {
    private final AttributeTypeService attributeTypeService;

    @GetMapping("/attribute/{id}/attribute_type")
    public ResponseEntity<AttributeTypeResponse> getAttributeTypeByAttributeId(
            @PathVariable(name = "id") long attributeId) {

        AttributeTypeResponse response = attributeTypeService.getAttributeTypeByAttributeId(attributeId);

        return ResponseEntity.ok(response);
    }
}