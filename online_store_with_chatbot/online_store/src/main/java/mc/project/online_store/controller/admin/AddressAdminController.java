package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;
import mc.project.online_store.service.admin.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AddressAdminController {
    private final AddressService addressService;

    @GetMapping("/user/{id}/address")
    public ResponseEntity<List<AddressResponse>> getPageByUserId(
            @PathVariable(name = "id") long userId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AddressResponse> response = addressService.getPageByUserId(userId, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<AddressResponse> getAddress(
            @PathVariable(name = "id") long id) {

        AddressResponse response = addressService.getAddress(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{id}/address")
    public ResponseEntity<AddressResponse> postAddress(
            @PathVariable(name = "id") long userId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.postAddress(userId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<AddressResponse> putAddress(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.putAddress(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/address/{id}")
    public void deleteAddress(
            @PathVariable(name = "id") long id) {

        addressService.deleteAddress(id);
    }
}
