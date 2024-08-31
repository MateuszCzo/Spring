package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.DeliveryRequest;
import mc.project.online_store.dto.response.DeliveryResponse;
import mc.project.online_store.service.admin.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DeliveryAdminController {
    private final DeliveryService deliveryService;

    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<DeliveryResponse> response = deliveryService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> getDelivery(
            @PathVariable(name = "id") long id) {

        DeliveryResponse response = deliveryService.getDelivery(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{id}/delivery")
    public ResponseEntity<DeliveryResponse> getDeliveryByOrderId(
            @PathVariable(name = "id") long orderId) {

        DeliveryResponse response = deliveryService.getDeliveryByOrderId(orderId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delivery")
    public ResponseEntity<DeliveryResponse> postDelivery(
            @Valid @RequestBody DeliveryRequest request) {

        DeliveryResponse response = deliveryService.postDelivery(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> putDelivery(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody DeliveryRequest request) {

        DeliveryResponse response = deliveryService.putDelivery(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delivery/{id}")
    public void deleteDelivery(
            @PathVariable(name = "id") long id) {

        deliveryService.deleteDelivery(id);
    }
}