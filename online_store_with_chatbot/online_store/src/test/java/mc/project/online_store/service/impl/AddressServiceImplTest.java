package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;
import mc.project.online_store.model.Address;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.AddressRepository;
import mc.project.online_store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidUserId_whenGetPageByUserId_thenReturnsAddressResponseList() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;
        User user = new User();
        user.setId(userId);
        Address address = new Address();
        Page<Address> pageResponse = new PageImpl<>(List.of(address));
        AddressResponse response = new AddressResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findByUser(user, pageRequest)).thenReturn(pageResponse);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        List<AddressResponse> serviceResponse = addressService.getPageByUserId(userId, page, pageSize);

        verify(userRepository).findById(userId);
        verify(addressRepository).findByUser(user, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUserId_whenGetPageByUserId_thenThrowsEntityNotFoundException() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.getPageByUserId(userId, page, pageSize));

        verify(userRepository).findById(userId);
    }

    @Test
    public void givenValidId_whenGetAddress_thenReturnsAddressResponse() {
        long id = 1;
        Address address = new Address();
        AddressResponse response = new AddressResponse();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.getAddress(id);

        verify(addressRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetAddress_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->addressService.getAddress(id));

        verify(addressRepository).findById(id);
    }

    @Test
    public void givenValidUserIdAndAddressRequest_whenPostAddress_thenReturnsAddressResponse() {
        long userId = 1;
        User user = new User();
        AddressRequest request = new AddressRequest();
        Address address = mock();
        AddressResponse response = new AddressResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(request, Address.class)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.postAddress(userId, request);

        verify(userRepository).findById(userId);
        verify(address).setUser(user);
        verify(addressRepository).save(address);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUserIdAndAddressRequest_whenPostAddress_thenThrowsEntityNotFoundException() {
        long userId = 1;
        AddressRequest request = new AddressRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.postAddress(userId, request));

        verify(userRepository).findById(userId);
        verify(addressRepository, never()).save(any());
    }

    @Test
    public void givenValidUserId_whenPutUserAddress_thenReturnsAddressResponse() throws JsonMappingException {
        long id = 1;
        AddressRequest request = new AddressRequest();
        Address address = new Address();
        AddressResponse response = new AddressResponse();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        when(objectMapper.updateValue(address, request)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.putAddress(id, request);

        verify(addressRepository).findById(id);
        verify(objectMapper).updateValue(address, request);
        verify(addressRepository).save(address);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUserId_whenPutUserAddress_thenThrowsEntityNotFoundException() {
        long id = 1;
        AddressRequest request = new AddressRequest();

        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.putAddress(id, request));

        verify(addressRepository).findById(id);
        verify(addressRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteAddress_thenReturnsVoid() {
        long id = 1;
        Address address = new Address();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        addressService.deleteAddress(id);

        verify(addressRepository).findById(id);
        verify(addressRepository).delete(address);
    }

    @Test
    public void givenInvalidId_whenDeleteAddress_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.deleteAddress(id));

        verify(addressRepository).findById(id);
        verify(addressRepository, never()).delete(any());
    }
}