package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.service.front.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductFrontController {
    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<List<ProductResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ProductResponse> response = productService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{id}/product")
    public ResponseEntity<List<ProductResponse>> getPageByCategoryId(
            @PathVariable(name = "id") long categoryId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ProductResponse> response = productService.getPageByCategoryId(categoryId, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable(name = "id") long id) {

        ProductResponse response = productService.getProduct(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/order/{id}/product")
    public ResponseEntity<List<ProductResponse>> getUserOrderProduct(
            @PathVariable(name = "id") long orderId) {

        List<ProductResponse> responses = productService.getUserOrderProduct(orderId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/cart/product")
    public ResponseEntity<List<ProductResponse>> getUserCartProduct() {

        List<ProductResponse> responses = productService.getUserCartProduct();

        return ResponseEntity.ok(responses);
    }
}