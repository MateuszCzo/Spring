package mc.project.online_store.service.front;

import mc.project.online_store.dto.request.AddressRequest;
import mc.project.online_store.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getUserPage(int page, int pageSize);

    AddressResponse getUserAddress(long id);

    AddressResponse postUserAddress(AddressRequest request);

    AddressResponse putUserAddress(long id, AddressRequest request);

    void deleteUserAddress(long id);
}
