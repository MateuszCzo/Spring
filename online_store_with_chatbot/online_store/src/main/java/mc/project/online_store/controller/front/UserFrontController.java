package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.service.front.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserFrontController {
    private UserService userService;
}