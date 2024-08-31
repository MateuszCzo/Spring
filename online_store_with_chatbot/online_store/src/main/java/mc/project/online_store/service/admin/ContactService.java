package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.ContactRequest;
import mc.project.online_store.dto.response.ContactResponse;

import java.util.List;

public interface ContactService {

    List<ContactResponse> getPageByUserId(long userId, int page, int pageSize);

    ContactResponse getContact(long id);

    ContactResponse postContact(long userId, ContactRequest request);

    ContactResponse putContact(long id, ContactRequest request);

    void deleteContact(long id);
}
