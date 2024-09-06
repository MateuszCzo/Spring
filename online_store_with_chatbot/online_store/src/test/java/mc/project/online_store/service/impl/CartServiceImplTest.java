package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.CartProductRequest;
import mc.project.online_store.dto.response.CartProductResponse;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.model.Cart;
import mc.project.online_store.model.CartProduct;
import mc.project.online_store.model.Product;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidCartProductRequest_whenChangeProductInCart_thenReturnsCartProductResponse() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(2);
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        product.setActive(true);
        CartProduct cartProduct = mock();
        CartProductResponse response = mock();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartProduct));
        when(cartProduct.getProduct()).thenReturn(product);
        when(objectMapper.convertValue(cartProduct, CartProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        CartProductResponse serviceResponse = cartService.changeProductInCart(request);

        verify(userService).getLoggedInUser();
        verify(cartRepository).findByUser(user);
        verify(productRepository).findById(request.getProductId());
        verify(cartProductRepository).findByCartAndProduct(cart, product);
        verify(cartProduct).setCart(cart);
        verify(cartProduct).setProduct(product);
        verify(cartProduct).setQuantity(request.getQuantity());
        verify(cartProductRepository).save(cartProduct);
        verify(response).setProductResponse(productResponse);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUser_whenChangeProductInCart_thenThrowsEntityNotFoundException() {
        CartProductRequest request = new CartProductRequest();
        request.setQuantity(2);

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.changeProductInCart(request));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenInvalidCart_whenChangeProductInCart_thenNotThrowsEntityNotFoundException() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(2);
        User user = new User();
        Product product = new Product();
        product.setActive(true);
        CartProduct cartProduct = mock();
        CartProductResponse response = new CartProductResponse();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(any(), eq(product))).thenReturn(Optional.of(cartProduct));
        when(objectMapper.convertValue(cartProduct, CartProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        CartProductResponse serviceResponse = cartService.changeProductInCart(request);

        assertNotNull(serviceResponse);
    }

    @Test
    public void givenInvalidCartProductRequest_whenChangeProductInCart_thenThrowsCartProductResponse() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(2);
        User user = new User();
        Cart cart = new Cart();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.changeProductInCart(request));

        verify(productRepository).findById(request.getProductId());
    }

    @Test
    public void givenInvalidProduct_whenChangeProductInCart_thenThrowsCartProductResponse() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(2);
        User user = new User();
        Cart cart = new Cart();
        Product product = mock();
        CartProduct cartProduct = mock();
        CartProductResponse response = new CartProductResponse();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(false);
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartProduct));
        when(objectMapper.convertValue(cartProduct, CartProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        assertThrows(EntityNotFoundException.class, () -> cartService.changeProductInCart(request));

        verify(product).isActive();
    }

    @Test
    public void givenInvalidCartProduct_whenChangeProductInCart_thenNotThrowsCartProductResponse() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(2);
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        product.setActive(true);
        CartProductResponse response = new CartProductResponse();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());
        when(objectMapper.convertValue(any(), eq(CartProductResponse.class))).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        CartProductResponse serviceResponse = cartService.changeProductInCart(request);

        verify(cartProductRepository).findByCartAndProduct(cart, product);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenQuantityLesserOrEqualToZero_whenChangeProductInCart_thenDeleteProduct() {
        CartProductRequest request = new CartProductRequest();
        request.setProductId(1);
        request.setQuantity(0);
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        product.setActive(true);
        CartProduct cartProduct = new CartProduct();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartProduct));

        CartProductResponse serviceResponse = cartService.changeProductInCart(request);

        verify(cartProductRepository).delete(cartProduct);

        assertNotNull(serviceResponse);
    }

    @Test
    public void givenValidId_whenDeleteProduct_thenReturnsVoid() {
        long id = 1;
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        CartProduct cartProduct = new CartProduct();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(cartProduct));

        cartService.deleteProduct(id);

        verify(userService).getLoggedInUser();
        verify(cartRepository).findByUser(user);
        verify(productRepository).findById(id);
        verify(cartProductRepository).findByCartAndProduct(cart, product);
        verify(cartProductRepository).delete(cartProduct);
    }

    @Test
    public void givenInvalidUser_whenDeleteProduct_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.deleteProduct(id));

        verify(userService).getLoggedInUser();
        verify(cartProductRepository, never()).delete(any());
    }

    @Test
    public void givenInvalidCart_whenDeleteProduct_thenThrowsEntityNotFoundException() {
        long id = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.deleteProduct(id));

        verify(cartRepository).findByUser(user);
        verify(cartProductRepository, never()).delete(any());
    }

    @Test
    public void givenInvalidId_whenDeleteProduct_thenThrowsEntityNotFoundException() {
        long id = 1;
        User user = new User();
        Cart cart = new Cart();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.deleteProduct(id));

        verify(productRepository).findById(id);
        verify(cartProductRepository, never()).delete(any());
    }

    @Test
    public void givenInvalidCartProduct_whenDeleteProduct_thenReturnsVoid() {
        long id = 1;
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.deleteProduct(id));

        verify(cartProductRepository).findByCartAndProduct(cart, product);
        verify(cartProductRepository, never()).delete(any());
    }
}