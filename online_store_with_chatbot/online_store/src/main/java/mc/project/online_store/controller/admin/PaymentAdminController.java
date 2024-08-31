package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.PaymentRequest;
import mc.project.online_store.dto.response.PaymentResponse;
import mc.project.online_store.service.admin.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PaymentAdminController {
    private final PaymentService paymentService;

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

    @GetMapping("/order/{id}/payment")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            @PathVariable(name = "id") long orderId) {

        PaymentResponse response = paymentService.getPaymentByOrderId(orderId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> postPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.postPayment(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<PaymentResponse> putPayment(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.putPayment(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/payment/{id}")
    public void deletePayment(
            @PathVariable(name = "id") long id) {

        paymentService.deletePayment(id);
    }
}