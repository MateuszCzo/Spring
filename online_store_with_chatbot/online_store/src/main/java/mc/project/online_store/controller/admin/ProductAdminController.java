package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ProductRequest;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.service.admin.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<List<ProductResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ProductResponse> response = productService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{id}/product")
    public ResponseEntity<List<ProductResponse>> getPageByOrderId(
            @PathVariable(name = "id") long orderId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ProductResponse> response = productService.getPageByOrderId(orderId, page, pageSize);

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

    @PostMapping("/product")
    public ResponseEntity<ProductResponse> postProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.postProduct(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductResponse> putProduct(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.putProduct(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/product/{id}")
    public void deletePayment(
            @PathVariable(name = "id") long id) {

        productService.deleteProduct(id);
    }
}