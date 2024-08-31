package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.CategoryRequest;
import mc.project.online_store.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getPage(String name, int page, int pageSize);

    CategoryResponse getCategory(long id);

    CategoryResponse getCategoryByProductId(long productId);

    CategoryResponse postCategory(CategoryRequest request);

    CategoryResponse putCategory(long id, CategoryRequest request);

    void deleteCategory(long id);
}
