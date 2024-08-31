package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.UserRequest;
import mc.project.online_store.dto.response.UserResponse;
import mc.project.online_store.service.admin.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getPage(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) @Max(100) int pageSize) {

        List<UserResponse> response = userService.getPage(name, page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable(name = "id") long id) {

        UserResponse response = userService.getUser(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{id}/user")
    public ResponseEntity<UserResponse> getUserByOrderId(
            @PathVariable(name = "id") long orderId) {

        UserResponse response = userService.getUserByOrderId(orderId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> putUser(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.putUser(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(
            @PathVariable(name = "id") long id) {

        userService.deleteUser(id);
    }
}