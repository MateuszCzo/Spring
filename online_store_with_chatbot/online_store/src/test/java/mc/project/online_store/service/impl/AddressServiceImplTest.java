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
import mc.project.online_store.service.auth.UserService;
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
    @Mock
    private UserService userService;

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

    @Test
    public void whenGetUserAddress_thenReturnsAddressResponse() {
        int page = 0;
        int pageSize = 10;
        User user = new User();
        Address address = new Address();
        Page<Address> addressPage = new PageImpl<>(List.of(address));
        AddressResponse response = new AddressResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByUser(user, pageRequest)).thenReturn(addressPage);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        List<AddressResponse> serviceResponse = addressService.getUserPage(page, pageSize);

        verify(userService).getLoggedInUser();
        verify(addressRepository).findByUser(user, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUser_whenGetUserAddress_thenThrowsEntityNotFoundException() {
        int page = 0;
        int pageSize = 10;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.getUserPage(page, pageSize));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenValidId_whenGetUserAddress_thenReturnsAddressResponse() {
        long id = 1;
        User user = new User();
        Address address = new Address();
        AddressResponse response = new AddressResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(address));
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.getUserAddress(id);

        verify(userService).getLoggedInUser();
        verify(addressRepository).findByIdAndUser(id, user);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidIdAndInvalidUser_whenGetUserAddress_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.getUserAddress(id));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenInvalidId_whenGetUserAddress_thenThrowsEntityNotFoundException() {
        long id = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.getUserAddress(id));

        verify(userService).getLoggedInUser();
        verify(addressRepository).findByIdAndUser(id, user);
    }

    @Test
    public void givenValidAddressRequest_whenPostUserAddress_thenReturnsAddressResponse() {
        AddressRequest request = new AddressRequest();
        User user = new User();
        Address address = new Address();
        AddressResponse response = new AddressResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(request, Address.class)).thenReturn(address);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.postUserAddress(request);

        verify(userService).getLoggedInUser();
        verify(addressRepository).save(address);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUser_whenPostUserAddress_thenThrowsEntityNotFoundException() {
        AddressRequest request = new AddressRequest();

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.postUserAddress(request));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenValidIdAndAddressRequest_whenPutAddress_thenReturnsAddressResponse() throws JsonMappingException {
        long id = 1;
        AddressRequest request = new AddressRequest();
        User user = new User();
        Address address = new Address();
        AddressResponse response = new AddressResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(address));
        when(objectMapper.updateValue(address, request)).thenReturn(address);
        when(objectMapper.convertValue(address, AddressResponse.class)).thenReturn(response);

        AddressResponse serviceResponse = addressService.putUserAddress(id, request);

        verify(userService).getLoggedInUser();
        verify(addressRepository).findByIdAndUser(id, user);
        verify(objectMapper).updateValue(address, request);
        verify(addressRepository).save(address);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUser_whenPutAddress_thenThrowsEntityNotFoundException() {
        long id = 1;
        AddressRequest request = new AddressRequest();

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.putAddress(id, request));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenInvalidIdAndAddressRequest_whenPutAddress_thenThrowsEntityNotFoundException() {
        long id = 1;
        AddressRequest request = new AddressRequest();
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.putUserAddress(id, request));

        verify(addressRepository).findByIdAndUser(id, user);
    }

    @Test
    public void givenValidId_whenDeleteUserAddress_thenReturnsVoid() {
        long id = 1;
        User user = new User();
        Address address = new Address();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(address));

        addressService.deleteUserAddress(id);

        verify(userService).getLoggedInUser();
        verify(addressRepository).findByIdAndUser(id, user);
        verify(addressRepository).delete(address);
    }

    @Test
    public void givenInvalidId_whenDeleteUserAddress_thenThrowsEntityNotFoundException() {
        long id = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(addressRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.deleteUserAddress(id));

        verify(addressRepository.findByIdAndUser(id, user));
        verify(addressRepository, never()).delete(any());
    }

    @Test
    public void givenInvalidUser_whenDeleteUserAddress_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.deleteUserAddress(id));

        verify(userService).getLoggedInUser();
    }
}