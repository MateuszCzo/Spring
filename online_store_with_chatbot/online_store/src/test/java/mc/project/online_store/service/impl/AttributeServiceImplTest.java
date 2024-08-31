package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.AttributeRequest;
import mc.project.online_store.dto.response.AttributeResponse;
import mc.project.online_store.model.Attribute;
import mc.project.online_store.model.AttributeType;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.AttributeRepository;
import mc.project.online_store.repository.AttributeTypeRepository;
import mc.project.online_store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttributeServiceImplTest {
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private AttributeTypeRepository attributeTypeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidProductId_whenGetPageProductId_thenReturnsAttributeResponseList() {
        long productId = 1;
        Product product = mock();
        Attribute attribute = new Attribute();
        Set<Attribute> attributeSet = new HashSet<>(List.of(attribute));
        AttributeResponse response = new AttributeResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getAttributes()).thenReturn(attributeSet);
        when(objectMapper.convertValue(attribute, AttributeResponse.class)).thenReturn(response);

        List<AttributeResponse> serviceResponse = attributeService.getAttributeListByProductId(productId);

        verify(productRepository).findById(productId);
        verify(product).getAttributes();

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidProductId_whenGetPageProductId_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.getAttributeListByProductId(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidAttributeTypeId_whenGetPageByAttributeTypeId_thenReturnsAttributeResponseList() {
        long attributeTypeId = 1;
        int page = 0;
        int pageSize = 10;
        AttributeType attributeType = new AttributeType();
        Attribute attribute = new Attribute();
        Page<Attribute> attributePage = new PageImpl<>(List.of(attribute));
        AttributeResponse response = new AttributeResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(attributeTypeRepository.findById(attributeTypeId)).thenReturn(Optional.of(attributeType));
        when(attributeRepository.findByAttributeType(attributeType, pageRequest)).thenReturn(attributePage);
        when(objectMapper.convertValue(attribute, AttributeResponse.class)).thenReturn(response);

        List<AttributeResponse> serviceResponse = attributeService.getPageByAttributeTypeId(attributeTypeId, page, pageSize);

        verify(attributeTypeRepository).findById(attributeTypeId);
        verify(attributeRepository).findByAttributeType(attributeType, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidAttributeTypeId_whenGetPageByAttributeTypeId_thenThrowsEntityNotFoundException() {
        long attributeTypeId = 1;
        int page = 0;
        int pageSize = 10;

        when(attributeTypeRepository.findById(attributeTypeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.getPageByAttributeTypeId(attributeTypeId, page, pageSize));

        verify(attributeTypeRepository).findById(attributeTypeId);
    }

    @Test
    public void givenValidId_whenGetAttribute_thenReturnsAttributeResponse() {
        long id = 1;
        Attribute attribute = new Attribute();
        AttributeResponse response = new AttributeResponse();

        when(attributeRepository.findById(id)).thenReturn(Optional.of(attribute));
        when(objectMapper.convertValue(attribute, AttributeResponse.class)).thenReturn(response);

        AttributeResponse serviceResponse = attributeService.getAttribute(id);

        verify(attributeRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetAttribute_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attributeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.getAttribute(id));

        verify(attributeRepository).findById(id);
    }

    @Test
    public void givenValidAttributeRequest_whenPostAttribute_thenReturnsAttributeResponse() {
        AttributeRequest request = new AttributeRequest();
        request.setAttributeTypeId(1);
        AttributeType attributeType = new AttributeType();
        Attribute attribute = mock();
        AttributeResponse response = new AttributeResponse();

        when(attributeTypeRepository.findById(request.getAttributeTypeId())).thenReturn(Optional.of(attributeType));
        when(objectMapper.convertValue(request, Attribute.class)).thenReturn(attribute);
        when(objectMapper.convertValue(attribute, AttributeResponse.class)).thenReturn(response);

        AttributeResponse serviceResponse = attributeService.postAttribute(request);

        verify(attributeTypeRepository).findById(request.getAttributeTypeId());
        verify(attribute).setAttributeType(attributeType);
        verify(attributeRepository).save(attribute);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidAttributeRequest_whenPostAttribute_thenThrowsEntityNotFoundException() {
        AttributeRequest request = new AttributeRequest();
        request.setAttributeTypeId(1);

        when(attributeTypeRepository.findById(request.getAttributeTypeId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.postAttribute(request));

        verify(attributeTypeRepository).findById(request.getAttributeTypeId());
        verify(attributeRepository, never()).save(any());
    }

    @Test
    public void givenValidIdAndAttributeRequest_whenPutAttribute_thenReturnsAttributeResponse() throws JsonMappingException {
        long id = 1;
        AttributeRequest request = new AttributeRequest();
        request.setAttributeTypeId(2);
        AttributeType attributeType = new AttributeType();
        Attribute attribute = mock();
        AttributeResponse response = new AttributeResponse();

        when(attributeRepository.findById(id)).thenReturn(Optional.of(attribute));
        when(attributeTypeRepository.findById(request.getAttributeTypeId())).thenReturn(Optional.of(attributeType));
        when(objectMapper.updateValue(attribute, request)).thenReturn(attribute);
        when(objectMapper.convertValue(attribute, AttributeResponse.class)).thenReturn(response);

        AttributeResponse serviceResponse = attributeService.putAttribute(id, request);

        verify(attributeRepository).findById(id);
        verify(attributeTypeRepository).findById(request.getAttributeTypeId());
        verify(attribute).setAttributeType(attributeType);
        verify(attributeRepository).save(attribute);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndAttributeRequest_whenPutAttribute_thenThrowsEntityNotFoundException() {
        long id = 1;
        AttributeRequest request = new AttributeRequest();

        when(attributeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.putAttribute(id, request));

        verify(attributeRepository).findById(id);
        verify(attributeRepository, never()).save(any());
    }

    @Test
    public void givenValidIdAndInvalidAttributeRequest_whenPutAttribute_thenThrowsEntityNotFoundException() {
        long id = 1;
        AttributeRequest request = new AttributeRequest();
        request.setAttributeTypeId(2);
        Attribute attribute = mock();

        when(attributeRepository.findById(id)).thenReturn(Optional.of(attribute));
        when(attributeTypeRepository.findById(request.getAttributeTypeId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.putAttribute(id, request));

        verify(attributeTypeRepository).findById(request.getAttributeTypeId());
        verify(attributeRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteAttribute_thenReturnsVoid() {
        long id = 1;
        Attribute attribute = new Attribute();

        when(attributeRepository.findById(id)).thenReturn(Optional.of(attribute));

        attributeService.deleteAttribute(id);

        verify(attributeRepository).findById(id);
        verify(attributeRepository).delete(attribute);
    }

    @Test
    public void givenInvalidId_whenDeleteAttribute_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attributeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeService.deleteAttribute(id));

        verify(attributeRepository).findById(id);
        verify(attributeRepository, never()).delete(any());
    }

    @Test
    public void givenValidAttributeType_whenDeleteAttributeByAttributeType_thenReturnsVoid() {
        AttributeType attributeType = new AttributeType();
        Attribute attribute = new Attribute();
        List<Attribute> attributeList = List.of(attribute);

        when(attributeRepository.findByAttributeType(attributeType)).thenReturn(attributeList);

        attributeService.deleteAttributeByAttributeType(attributeType);

        verify(attributeRepository).findByAttributeType(attributeType);
        verify(attributeRepository).deleteAll(attributeList);
    }
}