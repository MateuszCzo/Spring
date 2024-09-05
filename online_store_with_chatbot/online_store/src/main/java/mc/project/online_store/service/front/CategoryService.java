package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getPage(String name, int page, int pageSize);

    List<CategoryResponse> getPageByParentId(long parentId, int page, int pageSize);

    CategoryResponse getCategoryByProductId(long productId);

    CategoryResponse getParent(long childId);
}
