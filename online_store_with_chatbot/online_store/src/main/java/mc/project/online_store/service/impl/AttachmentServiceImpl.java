package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttachmentRequest;
import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;
import mc.project.online_store.model.Attachment;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.AttachmentRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.AttachmentService;
import mc.project.online_store.service.util.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService, mc.project.online_store.service.front.AttachmentService {
    @Value("${file.path.attachment}")
    private String attachmentFilePath;
    private final AttachmentRepository attachmentRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final FileService fileService;

    @Override
    public List<AttachmentResponse> getPage(String name, int page, int pageSize) {
        Page<Attachment> response = attachmentRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return response
                .map(attachment -> objectMapper.convertValue(attachment, AttachmentResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<AttachmentResponse> getAttachmentListByProductId(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        return product.getAttachments()
                .stream()
                .map(attachment -> objectMapper.convertValue(attachment, AttachmentResponse.class))
                .toList();
    }

    @Override
    public AttachmentResponse getAttachment(long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(attachment, AttachmentResponse.class);
    }

    @Override
    public AttachmentContentResponse getAttachmentContent(long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Resource resource = fileService.getFile(attachment.getPath());

        AttachmentContentResponse response = new AttachmentContentResponse();
        response.setFileName(attachment.getName());
        response.setContentType(MediaType.parseMediaType(attachment.getType()));
        response.setContent(resource);

        return response;
    }

    @Override
    public AttachmentResponse postAttachment(AttachmentRequest request) {
        Attachment attachment = objectMapper.convertValue(request, Attachment.class);
        attachment.setPath(attachmentFilePath + request.getFile().getOriginalFilename());
        attachment.setType(request.getFile().getContentType());

        fileService.postFile(request.getFile(), attachmentFilePath);

        attachmentRepository.save(attachment);

        return objectMapper.convertValue(attachment, AttachmentResponse.class);
    }

    @Override
    public AttachmentResponse putAttachment(long id, AttachmentRequest request) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            attachment = objectMapper.updateValue(attachment, request);
        } catch (JsonMappingException e) { }

        attachmentRepository.save(attachment);

        return objectMapper.convertValue(attachment, AttachmentResponse.class);
    }

    @Override
    public void deleteAttachment(long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        fileService.deleteFile(attachment.getPath());

        attachmentRepository.delete(attachment);
    }
}
