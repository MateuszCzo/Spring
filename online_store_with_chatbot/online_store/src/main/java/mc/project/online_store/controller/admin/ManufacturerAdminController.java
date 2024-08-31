package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ManufacturerRequest;
import mc.project.online_store.dto.response.ManufacturerResponse;
import mc.project.online_store.service.admin.ManufacturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manufacturer")
@RequiredArgsConstructor
public class ManufacturerAdminController {
    private final ManufacturerService manufacturerService;

    @GetMapping("/manufacturer")
    public ResponseEntity<List<ManufacturerResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ManufacturerResponse> response = manufacturerService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/manufacturer/{id}")
    public ResponseEntity<ManufacturerResponse> getManufacturer(
            @PathVariable(name = "id") long id) {

        ManufacturerResponse response = manufacturerService.getManufacturer(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}/manufacturer")
    public ResponseEntity<ManufacturerResponse> getManufacturerByProductId(
            @PathVariable(name = "id") long productId) {

        ManufacturerResponse response = manufacturerService.getManufacturerByProductId(productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/manufacturer")
    public ResponseEntity<ManufacturerResponse> postManufacturer(
            @Valid @RequestBody ManufacturerRequest request) {

        ManufacturerResponse response = manufacturerService.postManufacturer(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/manufacturer/{id}")
    public ResponseEntity<ManufacturerResponse> putManufacturer(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody ManufacturerRequest request) {

        ManufacturerResponse response = manufacturerService.putManufacturer(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/manufacturer/{id}")
    public void deleteManufacturer(
            @PathVariable(name = "id") long id) {

        manufacturerService.deleteManufacturer(id);
    }
}