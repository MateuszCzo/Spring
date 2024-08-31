package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;
import mc.project.online_store.model.Contact;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.ContactRepository;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.admin.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<ContactResponse> getPageByUserId(long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Page<Contact> contacts = contactRepository.findByUser(
                user, PageRequest.of(page, pageSize));

        return contacts
                .map(contact -> objectMapper.convertValue(contact, ContactResponse.class))
                .toList();
    }

    @Override
    public ContactResponse getContact(long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public ContactResponse postContact(long userId, ContactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Contact contact = objectMapper.convertValue(request, Contact.class);
        contact.setUser(user);

        contactRepository.save(contact);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public ContactResponse putContact(long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            contact = objectMapper.updateValue(contact, request);
        } catch (JsonMappingException e) { }

        contactRepository.save(contact);

        return objectMapper.convertValue(contact, ContactResponse.class);
    }

    @Override
    public void deleteContact(long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        contactRepository.delete(contact);
    }
}
