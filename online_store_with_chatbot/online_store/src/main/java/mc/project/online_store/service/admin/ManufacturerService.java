package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.ManufacturerRequest;
import mc.project.online_store.dto.response.ManufacturerResponse;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerResponse> getPage(String name, int page, int pageSize);

    ManufacturerResponse getManufacturer(long id);

    ManufacturerResponse getManufacturerByProductId(long productId);

    ManufacturerResponse postManufacturer(ManufacturerRequest request);

    ManufacturerResponse putManufacturer(long id, ManufacturerRequest request);

    void deleteManufacturer(long id);
}
