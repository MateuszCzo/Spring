package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.CategoryRequest;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.service.admin.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<CategoryResponse> responses = categoryService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> getCategory(
            @PathVariable(name = "id") long id) {

        CategoryResponse response = categoryService.getCategory(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}/category")
    public ResponseEntity<CategoryResponse> getCategoryByProductId(
            @PathVariable(name = "id") long productId) {

        CategoryResponse response = categoryService.getCategoryByProductId(productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> postCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.postCategory(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> putCategory(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.putCategory(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(
            @PathVariable(name = "id") long id) {

        categoryService.deleteCategory(id);
    }
}