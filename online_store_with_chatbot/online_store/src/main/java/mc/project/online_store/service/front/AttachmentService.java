package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;

import java.util.List;

public interface AttachmentService {
    List<AttachmentResponse> getPage(String name, int page, int pageSize);

    List<AttachmentResponse> getAttachmentListByProductId(long productId);

    AttachmentResponse getAttachment(long id);

    AttachmentContentResponse getAttachmentContent(long id);
}
