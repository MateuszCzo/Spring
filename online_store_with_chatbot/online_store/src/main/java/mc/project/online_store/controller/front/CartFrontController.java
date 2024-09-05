package mc.project.online_store.controller.front;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.CartProductRequest;
import mc.project.online_store.service.front.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartFrontController {
    private final CartService cartService;

    @PostMapping("/cart")
    public void postProduct(
            @Valid @RequestBody CartProductRequest request) {

        cartService.postProduct(request);
    }

    @DeleteMapping("/cart")
    public void deleteProduct(
            @Valid @RequestBody CartProductRequest request) {

        cartService.deleteProduct(request);
    }
}
