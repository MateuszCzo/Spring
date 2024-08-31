package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;
import mc.project.online_store.model.Address;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.AddressRepository;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.admin.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService, mc.project.online_store.service.front.AddressService {
    final AddressRepository addressRepository;
    final UserRepository userRepository;
    final ObjectMapper objectMapper;

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

    @Override
    public AddressResponse postAddress(long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Address address = objectMapper.convertValue(request, Address.class);
        address.setUser(user);

        addressRepository.save(address);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    @Override
    public AddressResponse putAddress(long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            address = objectMapper.updateValue(address, request);
        } catch (JsonMappingException e) { }

        addressRepository.save(address);

        return objectMapper.convertValue(address, AddressResponse.class);
    }

    @Override
    public void deleteAddress(long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        addressRepository.delete(address);
    }
}
