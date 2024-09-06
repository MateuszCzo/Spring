package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.CartProductResponse;
import mc.project.online_store.dto.response.OrderProductResponse;
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

    @GetMapping("/user/cart/product")
    public ResponseEntity<List<CartProductResponse>> getUserCartProductList() {
        List<CartProductResponse> responses = productService.getUserCartProductList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/order/{id}/product")
    public ResponseEntity<List<OrderProductResponse>> getUserOrderProductList(
            @PathVariable(name = "id") long orderId) {

        List<OrderProductResponse> response = productService.getUserOrderProductList(orderId);

        return ResponseEntity.ok(response);
    }
}