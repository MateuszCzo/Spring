package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.AttributeTypeRequest;
import mc.project.online_store.dto.response.AttributeTypeResponse;

import java.util.List;

public interface AttributeTypeService {
    List<AttributeTypeResponse> getPage(String name, int page, int pageSize);

    AttributeTypeResponse getAttributeType(long id);

    AttributeTypeResponse postAttributeType(AttributeTypeRequest request);

    AttributeTypeResponse putAttributeType(long id, AttributeTypeRequest request);

    void deleteAttributeType(long id);
}
