package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.CategoryRequest;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Category;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.CategoryRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsCategoryResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Category category = new Category();
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        CategoryResponse response = new CategoryResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(categoryRepository.findByNameContaining(name, pageRequest)).thenReturn(categoryPage);
        when(objectMapper.convertValue(category, CategoryResponse.class)).thenReturn(response);

        List<CategoryResponse> serviceResponse = categoryService.getPage(name, page, pageSize);

        verify(categoryRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetCategory_thenReturnsCategoryResponse() {
        long id = 1;
        Category category = new Category();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(objectMapper.convertValue(category, CategoryResponse.class)).thenReturn(response);

        CategoryResponse serviceResponse = categoryService.getCategory(id);

        verify(categoryRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetCategory_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategory(id));
    }

    @Test
    public void givenValidProductId_whenGetCategoryByProductId_thenReturnsCategoryResponse() {
        long productId = 1;
        Product product = mock();
        Category category = new Category();
        CategoryResponse response = new CategoryResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getCategory()).thenReturn(category);
        when(objectMapper.convertValue(product.getCategory(), CategoryResponse.class)).thenReturn(response);

        CategoryResponse serviceResponse = categoryService.getCategoryByProductId(productId);

        verify(productRepository).findById(productId);
        verify(product, atLeastOnce()).getCategory();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidProductId_whenGetCategoryByProductId_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByProductId(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidCategoryRequest_whenPostCategory_thenReturnsCategoryResponse() {
        CategoryRequest request = new CategoryRequest();
        request.setParentId(1);
        MultipartFile file = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes());
        request.setFile(file);
        Category parent = new Category();
        Category category = mock();
        Image image = new Image();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(request.getParentId())).thenReturn(Optional.of(parent));
        when(objectMapper.convertValue(request, Category.class)).thenReturn(category);
        when(imageService.postImage(request.getFile())).thenReturn(image);
        when(objectMapper.convertValue(category, CategoryResponse.class)).thenReturn(response);

        CategoryResponse serviceResponse = categoryService.postCategory(request);

        verify(categoryRepository).findById(request.getParentId());
        verify(imageService).postImage(request.getFile());
        verify(category).setParent(parent);
        verify(category).setImage(image);
        verify(categoryRepository).save(category);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidIdAndCategoryRequest_whenPutCategory_thenReturnsCategoryResponse() throws JsonMappingException {
        long id = 1;
        CategoryRequest request = new CategoryRequest();
        request.setParentId(2);
        Category parent = new Category();
        Image image = new Image();
        Category category = mock();
        category.setImage(image);
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(request.getParentId())).thenReturn(Optional.of(parent));
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(objectMapper.updateValue(category, request)).thenReturn(category);
        when(objectMapper.convertValue(category, CategoryResponse.class)).thenReturn(response);

        CategoryResponse serviceResponse = categoryService.putCategory(id, request);

        verify(categoryRepository).findById(request.getParentId());
        verify(categoryRepository).findById(id);
        verify(objectMapper).updateValue(category, request);
        verify(category).setParent(parent);
        verify(categoryRepository).save(category);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndCategoryRequest_whenPutCategory_thenThrowsEntityNotFoundException() {
        long id = 1;
        CategoryRequest request = new CategoryRequest();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.putCategory(id, request));

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void givenIdAndInvalidCategoryRequest_whenPutCategory_thenThrowsEntityNotFoundException() {
        long id = 1;
        long parentId = 1;
        CategoryRequest request = new CategoryRequest();
        request.setParentId(parentId);

        assertThrows(RelationConflictException.class, () -> categoryService.putCategory(id, request));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteCategory_thenReturnsVoid() {
        long id = 1;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.countDistinctByCategory(category)).thenReturn(0);
        when(categoryRepository.countDistinctByParent(category)).thenReturn(0);

        categoryService.deleteCategory(id);

        verify(categoryRepository).findById(id);
        verify(productRepository).countDistinctByCategory(category);
        verify(categoryRepository).countDistinctByParent(category);
        verify(imageService).deleteImage(category.getImage());
        verify(categoryRepository).delete(category);
    }

    @Test
    public void givenInvalidId_whenDeleteCategory_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(id));

        verify(categoryRepository).findById(id);
        verify(imageService, never()).deleteImage(any());
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    public void givenValidIdAndInvalidProductCount_whenDeleteCategory_thenThrowsEntityNotFoundException() {
        long id = 1;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.countDistinctByCategory(category)).thenReturn(1);

        assertThrows(RelationConflictException.class, () -> categoryService.deleteCategory(id));

        verify(imageService, never()).deleteImage(any());
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    public void givenValidIdAndInvalidCategoryChildrenCount_whenDeleteCategory_thenThrowsEntityNotFoundException() {
        long id = 1;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.countDistinctByParent(category)).thenReturn(1);

        assertThrows(RelationConflictException.class, () -> categoryService.deleteCategory(id));

        verify(imageService, never()).deleteImage(any());
        verify(categoryRepository, never()).delete(any());
    }
}