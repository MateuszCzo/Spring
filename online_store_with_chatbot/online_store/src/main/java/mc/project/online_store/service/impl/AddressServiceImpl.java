package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Address;
import mc.project.online_store.model.Order;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.AddressRepository;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.admin.AddressService;
import mc.project.online_store.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService, mc.project.online_store.service.front.AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<AddressResponse> getPageByUserId(long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Page<Address> response = addressRepository
                .findByUser(user, PageRequest.of(page, pageSize));

        return response
                .map(address -> objectMapper.convertValue(address, AddressResponse.class))
                .toList();
    }

    @Override
    public AddressResponse getAddress(long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    public AddressResponse postAddress(User user, AddressRequest request) {
        Address address = objectMapper.convertValue(request, Address.class);
        address.setUser(user);

        addressRepository.save(address);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    @Override
    public AddressResponse postAddress(long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        return postAddress(user, request);
    }

    public AddressResponse putAddress(Address address, AddressRequest request) {
        int orderCount = orderRepository.countDistinctByAddress(address);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot change address - address contains order");
        }

        try {
            address = objectMapper.updateValue(address, request);
        } catch (JsonMappingException e) { }

        addressRepository.save(address);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    @Override
    public AddressResponse putAddress(long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return putAddress(address, request);
    }

    public void deleteAddress(Address address) {
        addressRepository.delete(address);
    }

    @Override
    public void deleteAddress(long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        deleteAddress(address);
    }

    @Override
    public List<AddressResponse> getUserPage(int page, int pageSize) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Page<Address> addresses = addressRepository.findByUser(
                user, PageRequest.of(page, pageSize));

        return addresses
                .map(address -> objectMapper.convertValue(address, AddressResponse.class))
                .toList();
    }

    @Override
    public AddressResponse getUserAddress(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Address address = addressRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    @Override
    public AddressResponse postUserAddress(AddressRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        return postAddress(user, request);
    }

    @Override
    public AddressResponse putUserAddress(long id, AddressRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Address address = addressRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);

        return putAddress(address, request);
    }

    @Override
    public void deleteUserAddress(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Address address = addressRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);

        deleteAddress(address);
    }
}
