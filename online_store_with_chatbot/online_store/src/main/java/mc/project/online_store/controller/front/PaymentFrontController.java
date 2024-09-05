package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.PaymentResponse;
import mc.project.online_store.service.front.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentFrontController {
    public final PaymentService paymentService;

    @GetMapping("/payment")
    public ResponseEntity<List<PaymentResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<PaymentResponse> response = paymentService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable(name = "id") long id) {

        PaymentResponse response = paymentService.getPayment(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/order/{id}/payment")
    public ResponseEntity<PaymentResponse> getUserOrderPayment(
            @PathVariable(name = "id") long orderId) {

        PaymentResponse response = paymentService.getUserOrderPayment(orderId);

        return ResponseEntity.ok(response);
    }
}