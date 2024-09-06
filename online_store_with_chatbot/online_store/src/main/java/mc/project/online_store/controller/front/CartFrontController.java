package mc.project.online_store.controller.front;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.CartProductRequest;
import mc.project.online_store.dto.response.CartProductResponse;
import mc.project.online_store.service.front.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartFrontController {
    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<CartProductResponse> postProduct(
            @Valid @RequestBody CartProductRequest request) {

        CartProductResponse response = cartService.postProduct(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cart")
    public ResponseEntity<CartProductResponse> putProduct(
            @Valid @RequestBody CartProductRequest request) {;

        CartProductResponse response = cartService.putProduct(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart/product/{id}")
    public void deleteProduct(
            @PathVariable(name = "id") long id) {

        cartService.deleteProduct(id);
    }
}
