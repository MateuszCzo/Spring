package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.DeliveryResponse;
import mc.project.online_store.service.front.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryFrontController {
    private final DeliveryService deliveryService;

    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<DeliveryResponse> responses = deliveryService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> getDelivery(
            @PathVariable(name = "id") long id) {

        DeliveryResponse response = deliveryService.getDelivery(id);

        return ResponseEntity.ok(response);
    }
}