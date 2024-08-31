package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.AttachmentRequest;
import mc.project.online_store.dto.response.AttachmentContentResponse;
import mc.project.online_store.dto.response.AttachmentResponse;
import mc.project.online_store.exception.FileException;
import mc.project.online_store.model.Attachment;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.AttachmentRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.util.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttachmentServiceImplTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private FileService fileService;

    @Value("${file.path.attachment}")
    private String attachmentFilePath;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsAttachmentResponsePage() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Attachment attachment = new Attachment();
        AttachmentResponse response = new AttachmentResponse();
        Page<Attachment> attachmentPage = new PageImpl<>(List.of(attachment));
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(attachmentRepository.findByNameContaining(name, pageRequest)).thenReturn(attachmentPage);
        when(objectMapper.convertValue(attachment, AttachmentResponse.class)).thenReturn(response);

        List<AttachmentResponse> serviceResponse = attachmentService.getPage(name, page, pageSize);

        verify(attachmentRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidProductId_whenGetByProductId_thenReturnsAttachmentResponseList() {
        long productId = 1;
        Product product = mock();
        Attachment attachment = new Attachment();
        Set<Attachment> attachmentSet = new HashSet<>(List.of(attachment));
        AttachmentResponse response = new AttachmentResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getAttachments()).thenReturn(attachmentSet);
        when(objectMapper.convertValue(attachment, AttachmentResponse.class)).thenReturn(response);

        List<AttachmentResponse> serviceResponse = attachmentService.getAttachmentListByProductId(productId);

        verify(productRepository).findById(productId);
        verify(product).getAttachments();

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidProductId_whenGetPageByProductId_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attachmentService.getAttachmentListByProductId(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidId_whenGetAttachment_thenReturnsAttachmentResponse() {
        long id = 1;
        Attachment attachment = new Attachment();
        AttachmentResponse response = new AttachmentResponse();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));
        when(objectMapper.convertValue(attachment, AttachmentResponse.class)).thenReturn(response);

        AttachmentResponse serviceResponse = attachmentService.getAttachment(id);

        verify(attachmentRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetAttachment_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attachmentService.getAttachment(id));

        verify(attachmentRepository).findById(id);
    }

    @Test
    public void givenValidId_whenGetAttachmentContent_thenReturnsAttachmentContentResponse() {
        long id = 1;
        Attachment attachment = new Attachment();
        attachment.setName("filename");
        attachment.setType("text/plain");
        Resource resource = mock();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));
        when(fileService.getFile(attachment.getPath())).thenReturn(resource);

        AttachmentContentResponse serviceResponse = attachmentService.getAttachmentContent(id);

        verify(attachmentRepository).findById(id);
        verify(fileService).getFile(attachment.getPath());

        assertNotNull(serviceResponse);
        assertEquals(attachment.getName(), serviceResponse.getFileName());
        assertEquals(MediaType.parseMediaType(attachment.getType()), serviceResponse.getContentType());
        assertEquals(resource, serviceResponse.getContent());
    }

    @Test
    public void givenInvalidId_whenGetAttachmentContent_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attachmentService.getAttachmentContent(id));

        verify(attachmentRepository).findById(id);
    }

    @Test
    public void givenValidAttachmentRequest_whenPostAttachment_thenReturnsAttachmentResponse() {
        AttachmentRequest request = mock();
        MultipartFile file = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes());
        Attachment attachment = mock();
        AttachmentResponse response = new AttachmentResponse();

        when(objectMapper.convertValue(request, Attachment.class)).thenReturn(attachment);
        when(request.getFile()).thenReturn(file);
        when(objectMapper.convertValue(attachment, AttachmentResponse.class)).thenReturn(response);

        AttachmentResponse serviceResponse = attachmentService.postAttachment(request);

        verify(attachment).setPath(attachmentFilePath + file.getOriginalFilename());
        verify(attachment).setType(file.getContentType());
        verify(fileService).postFile(file, attachmentFilePath);
        verify(attachmentRepository).save(attachment);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidAttachmentRequest_whenPostAttachment_thenThrowsFileException() {
        AttachmentRequest request = mock();
        MultipartFile file = mock();
        Attachment attachment = new Attachment();

        when(objectMapper.convertValue(request, Attachment.class)).thenReturn(attachment);
        when(request.getFile()).thenReturn(file);
        doThrow(FileException.class).when(fileService).postFile(file, attachmentFilePath);

        assertThrows(FileException.class, () -> attachmentService.postAttachment(request));

        verify(attachmentRepository, never()).save(attachment);
    }

    @Test
    public void givenValidIdAndAttachmentRequest_whenPutAttachment_thenReturnsAttachmentResponse() throws JsonMappingException {
        long id = 1;
        AttachmentRequest request = new AttachmentRequest();
        Attachment attachment = new Attachment();
        AttachmentResponse response = new AttachmentResponse();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));
        when(objectMapper.updateValue(attachment, request)).thenReturn(attachment);
        when(objectMapper.convertValue(attachment, AttachmentResponse.class)).thenReturn(response);

        AttachmentResponse serviceResponse = attachmentService.putAttachment(id, request);

        verify(attachmentRepository).findById(id);
        verify(objectMapper).updateValue(attachment, request);
        verify(attachmentRepository).save(attachment);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenPutAttachment_thenThrowsEntityNotFoundException() {
        long id = 1;
        AttachmentRequest request = new AttachmentRequest();

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attachmentService.putAttachment(id, request));

        verify(attachmentRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteAttachment_thenReturnsVoid() {
        long id = 1;
        Attachment attachment = new Attachment();

        when(attachmentRepository.findById(id)).thenReturn(Optional.of(attachment));

        attachmentService.deleteAttachment(id);

        verify(attachmentRepository).findById(id);
        verify(fileService).deleteFile(attachment.getPath());
        verify(attachmentRepository).delete(attachment);
    }

    @Test
    public void givenInvalidId_whenDeleteAttachment_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attachmentService.deleteAttachment(id));

        verify(attachmentRepository).findById(id);
        verify(fileService, never()).deleteFile(any());
        verify(attachmentRepository, never()).delete(any());
    }
}