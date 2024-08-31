package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mc.project.online_store.dto.request.CategoryRequest;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Category;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.CategoryRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.CategoryService;
import mc.project.online_store.service.admin.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;

    @Override
    public List<CategoryResponse> getPage(String name, int page, int pageSize) {
        Page<Category> categories = categoryRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize)
        );

        return categories
                .map(category -> objectMapper.convertValue(category, CategoryResponse.class))
                .toList();
    }

    @Override
    public CategoryResponse getCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategoryByProductId(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(product.getCategory(), CategoryResponse.class);
    }

    @Override
    @Transactional
    public CategoryResponse postCategory(CategoryRequest request) {
        Category parent = categoryRepository.findById(request.getParentId())
                .orElse(null);

        Category category = objectMapper.convertValue(request, Category.class);

        Image image = imageService.postImage(request.getFile());

        category.setParent(parent);
        category.setImage(image);

        categoryRepository.save(category);

        return objectMapper.convertValue(category, CategoryResponse.class);
    }

    @Override
    @Transactional
    public CategoryResponse putCategory(long id, CategoryRequest request) {
        if (id == request.getParentId()) {
            throw new RelationConflictException("Invalid parent id");
        }

        Category parent = categoryRepository.findById(request.getParentId())
                .orElse(null);

        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            category = objectMapper.updateValue(category, request);
        } catch (JsonMappingException e) { }
        category.setParent(parent);

        categoryRepository.save(category);

        return objectMapper.convertValue(category, CategoryResponse.class);
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int productCount = productRepository.countDistinctByCategory(category);
        if (productCount > 0) {
            throw new RelationConflictException("Cannot delete category - category contain products");
        }

        int childrenCount = categoryRepository.countDistinctByParent(category);
        if (childrenCount > 0) {
            throw new RelationConflictException("Cannot delete category - category contain children");
        }

        imageService.deleteImage(category.getImage());

        categoryRepository.delete(category);
    }
}
