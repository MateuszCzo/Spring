package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Address;
import mc.project.online_store.model.Contact;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.ContactRepository;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.admin.ContactService;
import mc.project.online_store.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService, mc.project.online_store.service.front.ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public List<ContactResponse> getPageByUser(User user, int page, int pageSize) {
        Page<Contact> contacts = contactRepository.findByUser(
                user, PageRequest.of(page, pageSize));

        return contacts
                .map(contact -> objectMapper.convertValue(contact, ContactResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ContactResponse> getPageByUserId(long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        return getPageByUser(user, page, pageSize);
    }

    @Override
    public ContactResponse getContact(long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    public ContactResponse postContact(User user, ContactRequest request) {
        Contact contact = objectMapper.convertValue(request, Contact.class);
        contact.setUser(user);

        contactRepository.save(contact);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public ContactResponse postContact(long userId, ContactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        return postContact(user, request);
    }

    public ContactResponse putContact(Contact contact, ContactRequest request) {
        int orderCount = orderRepository.countDistinctByContact(contact);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot change contact - contact contains order");
        }

        try {
            contact = objectMapper.updateValue(contact, request);
        } catch (JsonMappingException e) { }

        contactRepository.save(contact);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public ContactResponse putContact(long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return putContact(contact, request);
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    @Override
    public void deleteContact(long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        deleteContact(contact);
    }

    @Override
    public List<ContactResponse> getUserPage(int page, int pageSize) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        return getPageByUser(user, page, pageSize);
    }

    @Override
    public ContactResponse getUserContact(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public ContactResponse postUserContact(ContactRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        return postContact(user, request);
    }

    @Override
    public ContactResponse putUserContact(long id, ContactRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);


        return putContact(contact, request);
    }

    @Override
    public void deleteUserContact(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Contact contact = contactRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);


        deleteContact(contact);
    }
}
