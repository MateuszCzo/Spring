package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.AttachmentRequest;
import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;

import java.util.List;

public interface AttachmentService {
    List<AttachmentResponse> getPage(String name, int page, int pageSize);

    List<AttachmentResponse> getAttachmentListByProductId(long productId);

    AttachmentResponse getAttachment(long id);

    AttachmentContentResponse getAttachmentContent(long id);

    AttachmentResponse postAttachment(AttachmentRequest attachmentRequest);

    AttachmentResponse putAttachment(long id, AttachmentRequest request);

    void deleteAttachment(long id);
}
