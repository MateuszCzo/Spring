package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.ProductRequest;
import mc.project.online_store.dto.response.CartProductResponse;
import mc.project.online_store.dto.response.OrderProductResponse;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.admin.ImageService;
import mc.project.online_store.service.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsProductResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(List.of(product));
        ProductResponse response = new ProductResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(productRepository.findByNameContaining(name, pageRequest)).thenReturn(productPage);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(response);

        List<ProductResponse> serviceResponse = productService.getPage(name, page, pageSize);

        verify(productRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidCategoryId_whenGetPageByCategoryId_thenReturnsProductResponseList() {
        long categoryId = 1;
        int page = 0;
        int pageSize = 10;
        Category category = mock();
        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(List.of(product));
        ProductResponse response = new ProductResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(category, pageRequest)).thenReturn(productPage);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(response);

        List<ProductResponse> serviceResponse = productService.getPageByCategoryId(categoryId, page, pageSize);

        verify(categoryRepository).findById(categoryId);
        verify(productRepository).findByCategory(category, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidCategoryId_whenGetPageByCategoryId_thenThrowsEntityNotFoundException() {
        long categoryId = 1;
        int page = 0;
        int pageSize = 10;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getPageByCategoryId(categoryId, page, pageSize));

        verify(categoryRepository).findById(categoryId);
    }

    @Test
    public void givenValidId_whenGetProduct_thenReturnsProductResponse() {
        long id = 1;
        Product product = new Product();
        ProductResponse response = new ProductResponse();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(response);

        ProductResponse serviceResponse = productService.getProduct(id);

        verify(productRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetProduct_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProduct(id));

        verify(productRepository).findById(id);
    }

    @Test
    public void givenValidProductRequest_whenPostProduct_thenReturnsProductResponse() {
        ProductRequest request = new ProductRequest();
        request.setManufacturerId(2);
        request.setCategoryId(3);
        request.setAttachmentsIds(List.of(4));
        request.setAttributesIds(List.of(5));
        MultipartFile coverFile = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes());
        request.setCover(coverFile);
        MultipartFile imageFile = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes());
        request.setImages(List.of(imageFile));

        Manufacturer manufacturer = new Manufacturer();
        Category category = new Category();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(new Attachment()));
        Set<Attribute> attributeSet = new HashSet<>(List.of(new Attribute()));
        Image cover = new Image();
        Image image = new Image();
        Product product = mock();
        ProductResponse response = new ProductResponse();
        ArgumentCaptor<Set<Image>> imageSetCapture = ArgumentCaptor.forClass(Set.class);

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(attachmentRepository.findByIdIn(request.getAttachmentsIds())).thenReturn(attachmentSet);
        when(attributeRepository.findByIdIn(request.getAttributesIds())).thenReturn(attributeSet);
        when(imageService.postImage(request.getCover())).thenReturn(cover);
        when(imageService.postImage(imageFile)).thenReturn(image);
        when(objectMapper.convertValue(request, Product.class)).thenReturn(product);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(response);

        ProductResponse serviceResponse = productService.postProduct(request);

        verify(manufacturerRepository).findById(request.getManufacturerId());
        verify(categoryRepository).findById(request.getCategoryId());
        verify(attachmentRepository).findByIdIn(request.getAttachmentsIds());
        verify(attributeRepository).findByIdIn(request.getAttributesIds());
        verify(imageService).postImage(request.getCover());
        verify(imageService).postImage(imageFile);
        verify(objectMapper).convertValue(request, Product.class);
        verify(product).setImage(cover);
        verify(product).setManufacturer(manufacturer);
        verify(product).setCategory(category);
        verify(product).setAttachments(attachmentSet);
        verify(product).setAttributes(attributeSet);
        verify(product).setImages(imageSetCapture.capture());
        verify(productRepository).save(product);

        assertEquals(response, serviceResponse);
        assertEquals(1, imageSetCapture.getValue().size());
        assertTrue(imageSetCapture.getValue().contains(image));
    }

    @Test
    public void givenValidProductRequestAndInvalidManufacturerId_whenPostProduct_thenThrowsEntityNotFoundException() {
        ProductRequest request = new ProductRequest();

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.postProduct(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenValidProductRequestAndInvalidCategoryId_whenPostProduct_thenThrowsEntityNotFoundException() {
        ProductRequest request = new ProductRequest();

        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.postProduct(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenValidIdAndProductRequest_whenPutProduct_thenReturnsProductResponse() throws JsonMappingException {
        long id = 1;
        ProductRequest request = new ProductRequest();
        request.setManufacturerId(2);
        request.setCategoryId(3);
        request.setAttachmentsIds(List.of(4));
        request.setAttributesIds(List.of(5));

        Manufacturer manufacturer = new Manufacturer();
        Category category = new Category();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(new Attachment()));
        Set<Attribute> attributeSet = new HashSet<>(List.of(new Attribute()));
        Product product = mock();
        ProductResponse response = new ProductResponse();

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(attachmentRepository.findByIdIn(request.getAttachmentsIds())).thenReturn(attachmentSet);
        when(attributeRepository.findByIdIn(request.getAttributesIds())).thenReturn(attributeSet);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(objectMapper.updateValue(product, request)).thenReturn(product);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(response);

        ProductResponse serviceResponse = productService.putProduct(id, request);

        verify(manufacturerRepository).findById(request.getManufacturerId());
        verify(categoryRepository).findById(request.getCategoryId());
        verify(attachmentRepository).findByIdIn(request.getAttachmentsIds());
        verify(attributeRepository).findByIdIn(request.getAttributesIds());
        verify(productRepository).findById(id);
        verify(objectMapper).updateValue(product, request);
        verify(product).setManufacturer(manufacturer);
        verify(product).setCategory(category);
        verify(product).setAttachments(attachmentSet);
        verify(product).setAttributes(attributeSet);
        verify(productRepository).save(product);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndProductRequest_whenPutProduct_thenThrowsEntityNotFoundException() {
        long id = 1;
        ProductRequest request = new ProductRequest();
        request.setManufacturerId(2);
        request.setCategoryId(3);
        request.setAttachmentsIds(List.of(4));
        request.setAttributesIds(List.of(5));

        Manufacturer manufacturer = new Manufacturer();
        Category category = new Category();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(new Attachment()));
        Set<Attribute> attributeSet = new HashSet<>(List.of(new Attribute()));

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(attachmentRepository.findByIdIn(request.getAttachmentsIds())).thenReturn(attachmentSet);
        when(attributeRepository.findByIdIn(request.getAttributesIds())).thenReturn(attributeSet);
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.putProduct(id, request));

        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenInvalidManufacturerId_whenPutProduct_thenThrowsEntityNotFoundException() {
        long id = 1;
        ProductRequest request = new ProductRequest();
        request.setManufacturerId(2);
        request.setCategoryId(3);
        request.setAttachmentsIds(List.of(4));
        request.setAttributesIds(List.of(5));

        Category category = new Category();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(new Attachment()));
        Set<Attribute> attributeSet = new HashSet<>(List.of(new Attribute()));
        Product product = new Product();

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.empty());
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(attachmentRepository.findByIdIn(request.getAttachmentsIds())).thenReturn(attachmentSet);
        when(attributeRepository.findByIdIn(request.getAttributesIds())).thenReturn(attributeSet);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        assertThrows(EntityNotFoundException.class, () -> productService.putProduct(id, request));

        verify(manufacturerRepository).findById(request.getManufacturerId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenInvalidCategoryId_whenPutProduct_thenThrowsEntityNotFoundException() {
        long id = 1;
        ProductRequest request = new ProductRequest();
        request.setManufacturerId(2);
        request.setCategoryId(3);
        request.setAttachmentsIds(List.of(4));
        request.setAttributesIds(List.of(5));

        Manufacturer manufacturer = new Manufacturer();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(new Attachment()));
        Set<Attribute> attributeSet = new HashSet<>(List.of(new Attribute()));
        Product product = new Product();

        when(manufacturerRepository.findById(request.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());
        when(attachmentRepository.findByIdIn(request.getAttachmentsIds())).thenReturn(attachmentSet);
        when(attributeRepository.findByIdIn(request.getAttributesIds())).thenReturn(attributeSet);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        assertThrows(EntityNotFoundException.class, () -> productService.putProduct(id, request));

        verify(categoryRepository).findById(request.getCategoryId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void givenValidId_whenDeleteProduct_thenReturnsVoid() {
        long id = 1;
        Image cover = new Image();
        Image image = new Image();
        Set<Image> imageSet = new HashSet<>(List.of(image));
        Product product = new Product();
        product.setImage(cover);
        product.setImages(imageSet);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(orderRepository.countDistinctByProducts(product)).thenReturn(0);

        productService.deleteProduct(id);

        verify(productRepository).findById(id);
        verify(orderRepository).countDistinctByProducts(product);
        verify(imageService, atLeastOnce()).deleteImage(product.getImage());
        verify(imageService, atLeastOnce()).deleteImage(image);
        verify(productRepository).delete(product);
    }

    @Test
    public void givenInvalidId_whenDeleteProduct_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(id));

        verify(productRepository).findById(id);
        verify(imageService, never()).deleteImage(any(Image.class));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    public void givenValidIdAndInvalidOrderCount_whenDeleteProduct_thenThrowsRelationConflictException() {
        long id = 1;
        Product product = new Product();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(orderRepository.countDistinctByProducts(product)).thenReturn(1);

        assertThrows(RelationConflictException.class, () -> productService.deleteProduct(id));

        verify(productRepository).findById(id);
        verify(orderRepository).countDistinctByProducts(product);
        verify(imageService, never()).deleteImage(any(Image.class));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    public void givenValidOrderId_whenGetOrderProductList_thenReturnsOrderProductResponseList() {
        long orderId = 1;
        Order order = new Order();
        Product product = new Product();
        OrderProduct orderProduct = mock();
        Set<OrderProduct> orderProductSet = new HashSet<>(List.of(orderProduct));
        OrderProductResponse response = mock();
        ProductResponse productResponse = new ProductResponse();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderProductRepository.findByOrder(order)).thenReturn(orderProductSet);
        when(orderProduct.getProduct()).thenReturn(product);
        when(objectMapper.convertValue(orderProduct, OrderProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        List<OrderProductResponse> serviceResponse = productService.getOrderProductList(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderProductRepository).findByOrder(order);
        verify(response).setProductResponse(productResponse);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidOrderId_whenGetOrderProductList_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getOrderProductList(orderId));

        verify(orderRepository).findById(orderId);
    }

    @Test
    public void givenValidOrderId_whenGetUserOrderProductList_thenReturnsOrderProductResponseList() {
        long orderId = 1;
        User user = new User();
        Order order = new Order();
        Product product = new Product();
        OrderProduct orderProduct = mock();
        Set<OrderProduct> orderProductSet = new HashSet<>(List.of(orderProduct));
        OrderProductResponse response = mock();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.of(order));
        when(orderProductRepository.findByOrder(order)).thenReturn(orderProductSet);
        when(orderProduct.getProduct()).thenReturn(product);
        when(objectMapper.convertValue(orderProduct, OrderProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        List<OrderProductResponse> serviceResponse = productService.getUserOrderProductList(orderId);

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByIdAndUser(orderId, user);
        verify(orderProductRepository).findByOrder(order);
        verify(orderProduct).getProduct();
        verify(response).setProductResponse(productResponse);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUser_whenGetUserOrderProductList_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getUserOrderProductList(orderId));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenInvalidOrderId_whenGetUserOrderProductList_thenThrowsEntityNotFoundException() {
        long orderId = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getUserOrderProductList(orderId));

        verify(orderRepository).findByIdAndUser(orderId, user);
    }

    @Test
    public void whenGetUserCartProductList_thenReturnsCartProductResponseList() {
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        CartProduct cartProduct = mock();
        Set<CartProduct> cartProductSet = new HashSet<>(List.of(cartProduct));
        CartProductResponse response = mock();
        ProductResponse productResponse = new ProductResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartProductRepository.findByCart(cart)).thenReturn(cartProductSet);
        when(cartProduct.getProduct()).thenReturn(product);
        when(objectMapper.convertValue(cartProduct, CartProductResponse.class)).thenReturn(response);
        when(objectMapper.convertValue(product, ProductResponse.class)).thenReturn(productResponse);

        List<CartProductResponse> serviceResponse = productService.getUserCartProductList();

        verify(userService).getLoggedInUser();
        verify(cartRepository).findByUser(user);
        verify(cartProductRepository).findByCart(cart);
        verify(cartProduct).getProduct();
        verify(response).setProductResponse(productResponse);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUser_whenGetUserCartProductList_thenThrowsEntityNotFoundException() {
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getUserCartProductList());

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenInvalidCart_whenGetUserCartProductList_thenThrows() {
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getUserCartProductList());

        verify(cartRepository).findByUser(user);
    }
}