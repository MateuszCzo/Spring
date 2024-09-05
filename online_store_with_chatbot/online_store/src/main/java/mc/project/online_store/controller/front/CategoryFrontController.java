package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.service.front.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryFrontController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<CategoryResponse> responses = categoryService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{id}/children")
    public ResponseEntity<List<CategoryResponse>> getPageByParentId(
            @PathVariable(name = "id") long parentId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<CategoryResponse> responses = categoryService.getPageByParentId(parentId, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{id}/category")
    public ResponseEntity<CategoryResponse> getCategoryByProductId(
            @PathVariable(name = "id") long productId) {

        CategoryResponse response = categoryService.getCategoryByProductId(productId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{id}/parent")
    public ResponseEntity<CategoryResponse> getParent(
            @PathVariable(name = "id") long childId) {

        CategoryResponse response = categoryService.getParent(childId);

        return ResponseEntity.ok(response);
    }
}