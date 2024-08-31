package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;
import mc.project.online_store.model.Contact;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.ContactRepository;
import mc.project.online_store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void givenValidUserId_whenGetPage_thenReturnsContactResponseList() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;
        User user = new User();
        Contact contact = new Contact();
        Page<Contact> contactPage = new PageImpl<>(List.of(contact));
        ContactResponse response = new ContactResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(contactRepository.findByUser(user, pageRequest)).thenReturn(contactPage);
        when(objectMapper.convertValue(contact, ContactResponse.class)).thenReturn(response);

        List<ContactResponse> serviceResponse = contactService.getPageByUserId(userId, page, pageSize);

        verify(userRepository).findById(userId);
        verify(contactRepository).findByUser(user, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUserId_whenGetPage_thenThrowsEntityExistsException() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.getPageByUserId(userId, page, pageSize));

        verify(userRepository).findById(userId);
    }

    @Test
    public void givenValidId_whenGetContact_thenReturnsContactResponse() {
        long id = 1;
        Contact contact = new Contact();
        ContactResponse response = new ContactResponse();

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));
        when(objectMapper.convertValue(contact, ContactResponse.class)).thenReturn(response);

        ContactResponse serviceResponse = contactService.getContact(id);

        verify(contactRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetContact_thenThrowsEntityExistsException() {
        long id = 1;

        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.getContact(id));

        verify(contactRepository).findById(id);
    }

    @Test
    public void givenValidUserIdAndContactRequest_whenPostContact_thenReturnsContactResponse() {
        long userId = 1;
        User user = new User();
        ContactRequest request = new ContactRequest();
        Contact contact = mock();
        ContactResponse response = new ContactResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(request, Contact.class)).thenReturn(contact);
        when(objectMapper.convertValue(contact, ContactResponse.class)).thenReturn(response);

        ContactResponse serviceResponse = contactService.postContact(userId, request);

        verify(userRepository).findById(userId);
        verify(objectMapper).convertValue(request, Contact.class);
        verify(contact).setUser(user);
        verify(contactRepository).save(contact);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidUserIdAndContactRequest_whenPostContact_thenThrowsEntityExistsException() {
        long userId = 1;
        ContactRequest request = new ContactRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.postContact(userId, request));

        verify(userRepository).findById(userId);
        verify(contactRepository, never()).save(any());
    }

    @Test
    public void givenValidIdAndContactRequest_whenPutContact_thenReturnsContactResponse() throws JsonMappingException {
        long id = 1;
        ContactRequest request = new ContactRequest();
        Contact contact = new Contact();
        ContactResponse response = new ContactResponse();

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));
        when(objectMapper.updateValue(contact, request)).thenReturn(contact);
        when(objectMapper.convertValue(contact, ContactResponse.class)).thenReturn(response);

        ContactResponse serviceResponse = contactService.putContact(id, request);

        verify(contactRepository).findById(id);
        verify(objectMapper).updateValue(contact, request);
        verify(contactRepository).save(contact);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndContactRequest_whenPutContact_thenThrowsEntityExistsException() {
        long id = 1;
        ContactRequest request = new ContactRequest();

        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.putContact(id, request));

        verify(contactRepository).findById(id);
        verify(contactRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteContact_thenReturnsVoid() {
        long id = 1;
        Contact contact = new Contact();

        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        contactService.deleteContact(id);

        verify(contactRepository).findById(id);
        verify(contactRepository).delete(contact);
    }

    @Test
    public void givenInvalidId_whenDeleteContact_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.deleteContact(id));

        verify(contactRepository).findById(id);
        verify(contactRepository, never()).delete(any());
    }
}