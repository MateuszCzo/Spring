package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.AttributeTypeResponse;

public interface AttributeTypeService {
    AttributeTypeResponse getAttributeTypeByAttributeId(long attributeId);
}
