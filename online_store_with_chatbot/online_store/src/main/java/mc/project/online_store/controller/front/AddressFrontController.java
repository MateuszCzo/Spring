package mc.project.online_store.controller.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;
import mc.project.online_store.service.front.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressFrontController {
    private final AddressService addressService;

    @GetMapping("/user/address")
    public ResponseEntity<List<AddressResponse>> getUserPage(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<AddressResponse> responses = addressService.getUserPage(page, pageSize);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/address/{id}")
    public ResponseEntity<AddressResponse> getUserAddress(
            @PathVariable(name = "id") long id) {

        AddressResponse response = addressService.getUserAddress(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/address")
    public ResponseEntity<AddressResponse> postUserAddress(
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.postUserAddress(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/address/{id}")
    public ResponseEntity<AddressResponse> putUserAddress(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.putUserAddress(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/address")
    public void deleteUserAddress(
            @PathVariable(name = "id") long id) {

        addressService.deleteUserAddress(id);
    }
}
