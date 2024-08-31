package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.service.admin.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<List<OrderResponse>> getPage(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<OrderResponse> response = orderService.getPage(page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}/order")
    public ResponseEntity<List<OrderResponse>> getPageByUserId(
            @PathVariable(name = "id") long userId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<OrderResponse> response = orderService.getPageByUserId(userId, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable(name = "id") long id) {

        OrderResponse response = orderService.getOrder(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponse> putOrder(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response = orderService.putOrder(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrder(
            @PathVariable(name = "id") long id) {

        orderService.deleteOrder(id);
    }
}