package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.AttributeResponse;

import java.util.List;

public interface AttributeService {
    List<AttributeResponse> getAttributeListByProductId(long productId);
}
