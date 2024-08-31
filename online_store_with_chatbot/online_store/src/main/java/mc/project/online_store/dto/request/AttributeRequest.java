package mc.project.online_store.dto.request;

import lombok.Data;

@Data
public class AttributeRequest {
    private long attributeTypeId;
    private String value;
}
