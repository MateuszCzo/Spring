package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.UserRequest;
import mc.project.online_store.dto.response.UserResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, mc.project.online_store.service.admin.UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<UserResponse> getPage(String name, int page, int pageSize) {
        Page<User> users = userRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return users
                .map(user -> objectMapper.convertValue(user, UserResponse.class))
                .toList();
    }

    @Override
    public UserResponse getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(user, UserResponse.class);
    }

    @Override
    public UserResponse getUserByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order.getUser(), UserResponse.class);
    }

    @Override
    public UserResponse putUser(long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            user = objectMapper.updateValue(user, request);
        } catch (JsonMappingException e) { }

        userRepository.save(user);

        return objectMapper.convertValue(user, UserResponse.class);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int orderCount = orderRepository.countDistinctByUser(user);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot delete user - user contain orders");
        }

        List<Address> addresses = addressRepository.findByUser(user);
        List<Contact> contacts = contactRepository.findByUser(user);
        Optional<Cart> cart = cartRepository.findByUser(user);

        addressRepository.deleteAll(addresses);
        contactRepository.deleteAll(contacts);
        cart.ifPresent(cartRepository::delete);
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        if (userDetails == null) return Optional.empty();

        return userRepository.findByName(userDetails.getUsername());
    }
}
