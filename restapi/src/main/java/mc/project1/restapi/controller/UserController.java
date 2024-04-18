package mc.project1.restapi.controller;

import lombok.RequiredArgsConstructor;
import mc.project1.restapi.model.User;
import mc.project1.restapi.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @deprecated
 * Test only
 */
@Deprecated
@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserRepository userRepository;

    // @todo delete
    @GetMapping("/users")
    public List<User> getUsers()
    {
        return userRepository.findAll();
    }
}
