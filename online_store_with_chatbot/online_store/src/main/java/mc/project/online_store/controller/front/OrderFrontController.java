package mc.project.online_store.controller.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.service.front.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderFrontController {
    private final OrderService orderService;

    @GetMapping("/user/order")
    public ResponseEntity<List<OrderResponse>> getUserPage(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<OrderResponse> responses = orderService.getUserPage(page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/order/{id}")
    public ResponseEntity<OrderResponse> getUserOrder(
            @PathVariable(name = "id") long id) {

        OrderResponse response = orderService.getUserOrder(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> postOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response = orderService.postUserOrder(request);

        return ResponseEntity.ok(response);
    }
}