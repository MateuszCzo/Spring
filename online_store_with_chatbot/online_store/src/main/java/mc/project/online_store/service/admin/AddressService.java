package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getPageByUserId(long userId, int page, int pageSize);

    AddressResponse getAddress(long id);

    AddressResponse postAddress(long userId, AddressRequest request);

    AddressResponse putAddress(long id, AddressRequest request);

    void deleteAddress(long id);
}
