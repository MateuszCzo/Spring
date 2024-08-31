package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.UserRequest;
import mc.project.online_store.dto.response.UserResponse;
import mc.project.online_store.enums.UserRole;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void whenGetPage_thenReturnsUserResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        User user = new User();
        Page<User> userPage = new PageImpl<>(List.of(user));
        UserResponse response = new UserResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userRepository.findByNameContaining(name, pageRequest)).thenReturn(userPage);
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(response);

        List<UserResponse> serviceResponse = userService.getPage(name, page, pageSize);

        verify(userRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetUser_thenReturnsUserResponse() {
        long id = 1;
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(response);

        UserResponse serviceResponse = userService.getUser(id);

        verify(userRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetUser_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(id));

        verify(userRepository).findById(id);
    }

    @Test
    public void givenValidOrderId_whenGetUserByOrderId_thenReturnsUserResponse() {
        long orderId = 1;
        Order order = mock();
        User user = new User();
        UserResponse response = new UserResponse();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getUser()).thenReturn(user);
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(response);

        UserResponse serviceResponse = userService.getUserByOrderId(orderId);

        verify(orderRepository).findById(orderId);
        verify(order).getUser();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidOrderId_whenGetUserByOrderId_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByOrderId(orderId));

        verify(orderRepository).findById(orderId);
    }

    @Test
    public void givenValidIdAndUserRequest_whenPutUser_thenReturnsUserResponse() throws JsonMappingException {
        long id = 1;
        UserRequest request = new UserRequest();
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(objectMapper.updateValue(user, request)).thenReturn(user);
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(response);

        UserResponse serviceResponse = userService.putUser(id, request);

        verify(userRepository).findById(id);
        verify(objectMapper).updateValue(user, request);
        verify(userRepository).save(user);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndUserRequest_whenPutUser_thenThrowsEntityNotFoundException() {
        long id = 1;
        UserRequest request = new UserRequest();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.putUser(id, request));

        verify(userRepository).findById(id);
    }

    @Test
    public void givenValidId_whenDeleteUser_thenReturnsVoid() {
        long id = 1;
        User user = new User();
        List<Address> addressList = List.of(new Address());
        List<Contact> contactList = List.of(new Contact());
        Cart cart = new Cart();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(orderRepository.countDistinctByUser(user)).thenReturn(0);
        when(addressRepository.findByUser(user)).thenReturn(addressList);
        when(contactRepository.findByUser(user)).thenReturn(contactList);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        userService.deleteUser(id);

        verify(userRepository).findById(id);
        verify(orderRepository).countDistinctByUser(user);
        verify(addressRepository).findByUser(user);
        verify(contactRepository).findByUser(user);
        verify(cartRepository).findByUser(user);
        verify(addressRepository).deleteAll(addressList);
        verify(contactRepository).deleteAll(contactList);
        verify(cartRepository).delete(cart);
        verify(userRepository).delete(user);
    }

    @Test
    public void givenInvalidId_whenDeleteUser_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));

        verify(userRepository).findById(id);
        verify(addressRepository, never()).deleteAll(any());
        verify(contactRepository, never()).deleteAll(any());
        verify(cartRepository, never()).delete(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void givenNoUserInSecurityContextHolder_whenGetLoggedInUser_thenReturnsEmptyOptional() {
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return null;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isEmpty());
    }

    @Test
    public void givenNoUserInDatabase_whenGetLoggedInUser_thenReturnsEmptyOptional() {
        String username = "username_example";

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public String getPassword() {
                return "";
            }
            @Override
            public String getUsername() {
                return username;
            }
            @Override
            public boolean isAccountNonExpired() {
                return false;
            }
            @Override
            public boolean isAccountNonLocked() {
                return false;
            }
            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }
            @Override
            public boolean isEnabled() {
                return false;
            }
        };

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return userDetails;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByName(username)).thenReturn(Optional.empty());

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isEmpty());
    }

    @Test
    public void givenUser_whenGetLoggedInUser_thenReturnsOptionalWithUser() {
        User user = new User();
        user.setId(1L);
        user.setName("name_example");
        user.setPassword("password_example");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setRole(UserRole.ROLE_USER);

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return user;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
            @Override
            public String getName() {
                return "";
            }
        };

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByName(user.getUsername())).thenReturn(Optional.of(user));

        Optional<User> response = userService.getLoggedInUser();

        assertTrue(response.isPresent());
        assertEquals(user, response.get());
    }
}