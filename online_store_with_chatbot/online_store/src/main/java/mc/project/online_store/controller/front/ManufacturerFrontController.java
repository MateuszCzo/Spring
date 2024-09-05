package mc.project.online_store.controller.front;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.ManufacturerResponse;
import mc.project.online_store.service.front.ManufacturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManufacturerFrontController {
    private final ManufacturerService manufacturerService;

    @GetMapping("/manufacturer")
    public ResponseEntity<List<ManufacturerResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<ManufacturerResponse> responses = manufacturerService.getPage(name, page, pageSize);

        return ResponseEntity.ok(responses);
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
}