package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.AttributeRequest;
import mc.project.online_store.dto.response.AttributeResponse;
import mc.project.online_store.model.AttributeType;

import java.util.List;

public interface AttributeService {
    List<AttributeResponse> getAttributeListByProductId(long productId);

    List<AttributeResponse> getPageByAttributeTypeId(long attributeId, int page, int pageSize);

    AttributeResponse getAttribute(long id);

    AttributeResponse postAttribute(AttributeRequest request);

    AttributeResponse putAttribute(long id, AttributeRequest request);

    void deleteAttribute(long id);

    void deleteAttributeByAttributeType(AttributeType attributeType);
}
