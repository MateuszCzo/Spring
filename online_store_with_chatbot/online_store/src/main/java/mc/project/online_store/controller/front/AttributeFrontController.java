package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.AttributeResponse;
import mc.project.online_store.service.front.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttributeFrontController {
    private final AttributeService attributeService;

    @GetMapping("/product/{id}/attribute")
    public ResponseEntity<List<AttributeResponse>> getAttributeListByProductId(
            @PathVariable(name = "id") long productId) {

        List<AttributeResponse> responses = attributeService.getAttributeListByProductId(productId);

        return ResponseEntity.ok(responses);
    }
}