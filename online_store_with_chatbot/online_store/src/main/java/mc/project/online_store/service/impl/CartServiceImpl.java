package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.CartProductRequest;
import mc.project.online_store.dto.response.CartProductResponse;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.model.Cart;
import mc.project.online_store.model.CartProduct;
import mc.project.online_store.model.Product;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.CartProductRepository;
import mc.project.online_store.repository.CartRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.auth.UserService;
import mc.project.online_store.service.front.CartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Transactional
    public CartProductResponse changeProductInCart(CartProductRequest request) {
        if (request.getQuantity() <= 0) {
            deleteProduct(request.getProductId());
            return new CartProductResponse();
        }

        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUser(user)
                .orElse(new Cart());
        cart.setUser(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        if (!product.isActive()) {
            throw new EntityNotFoundException();
        }

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElse(new CartProduct());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(request.getQuantity());

        cartRepository.save(cart);
        cartProductRepository.save(cartProduct);

        CartProductResponse response = objectMapper.convertValue(cartProduct, CartProductResponse.class);
        response.setProductResponse(objectMapper.convertValue(product, ProductResponse.class));

        return response;
    }

    @Override
    public CartProductResponse postProduct(CartProductRequest request) {
        return changeProductInCart(request);
    }

    @Override
    public CartProductResponse putProduct(CartProductRequest request) {
        return changeProductInCart(request);
    }

    @Override
    public void deleteProduct(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(EntityNotFoundException::new);

        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(EntityNotFoundException::new);

        cartProductRepository.delete(cartProduct);
    }
}
