package mc.project.online_store.service.front;

import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;

import java.util.List;

public interface ContactService {
    List<ContactResponse> getUserPage(int page, int pageSize);

    ContactResponse getUserContact(long id);

    ContactResponse postUserContact(ContactRequest request);

    ContactResponse putUserContact(long id, ContactRequest request);

    void deleteUserContact(long id);
}
