package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.AttributeTypeRequest;
import mc.project.online_store.dto.response.AttributeTypeResponse;
import mc.project.online_store.model.AttributeType;
import mc.project.online_store.repository.AttributeTypeRepository;
import mc.project.online_store.service.admin.AttributeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttributeTypeServiceImplTest {
    @Mock
    private AttributeTypeRepository attributeTypeRepository;
    @Mock
    private AttributeService attributeService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AttributeTypeServiceImpl attributeTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsAttributeTypeResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        AttributeType attributeType = new AttributeType();
        Page<AttributeType> attributeTypePage = new PageImpl<>(List.of(attributeType));
        AttributeTypeResponse response = new AttributeTypeResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(attributeTypeRepository.findByNameContaining(name, pageRequest)).thenReturn(attributeTypePage);
        when(objectMapper.convertValue(attributeType, AttributeTypeResponse.class)).thenReturn(response);

        List<AttributeTypeResponse> serviceResponse = attributeTypeService.getPage(name, page, pageSize);

        verify(attributeTypeRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetAttributeType_thenReturnsAttributeTypeResponse() {
        long id = 1;
        AttributeType attributeType = new AttributeType();
        AttributeTypeResponse response = new AttributeTypeResponse();

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.of(attributeType));
        when(objectMapper.convertValue(attributeType, AttributeTypeResponse.class)).thenReturn(response);

        AttributeTypeResponse serviceResponse = attributeTypeService.getAttributeType(id);

        verify(attributeTypeRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetAttributeType_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeTypeService.getAttributeType(id));

        verify(attributeTypeRepository).findById(id);
    }

    @Test
    public void givenValidAttributeTypeRequest_whenPostAttributeType_thenReturnsAttributeTypeResponse() {
        AttributeTypeRequest request = new AttributeTypeRequest();
        AttributeType attributeType = new AttributeType();
        AttributeTypeResponse response = new AttributeTypeResponse();

        when(objectMapper.convertValue(request, AttributeType.class)).thenReturn(attributeType);
        when(objectMapper.convertValue(attributeType, AttributeTypeResponse.class)).thenReturn(response);

        AttributeTypeResponse serviceResponse = attributeTypeService.postAttributeType(request);

        verify(attributeTypeRepository).save(attributeType);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidIdAndAttributeTypeRequest_whenPutAttributeType_thenReturnsAttributeTypeResponse() throws JsonMappingException {
        long id = 1;
        AttributeTypeRequest request = new AttributeTypeRequest();
        AttributeType attributeType = new AttributeType();
        AttributeTypeResponse response = new AttributeTypeResponse();

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.of(attributeType));
        when(objectMapper.updateValue(attributeType, request)).thenReturn(attributeType);
        when(objectMapper.convertValue(attributeType, AttributeTypeResponse.class)).thenReturn(response);

        AttributeTypeResponse serviceResponse = attributeTypeService.putAttributeType(id, request);

        verify(objectMapper).updateValue(attributeType, request);
        verify(attributeTypeRepository).save(attributeType);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndAttributeTypeRequest_whenPutAttributeType_thenThrowsEntityNotFoundException() {
        long id = 1;
        AttributeTypeRequest request = new AttributeTypeRequest();

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeTypeService.putAttributeType(id, request));

        verify(attributeTypeRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteAttributeType_thenReturnsVoid() {
        long id = 1;
        AttributeType attributeType = new AttributeType();

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.of(attributeType));

        attributeTypeService.deleteAttributeType(id);

        verify(attributeService).deleteAttributeByAttributeType(attributeType);
        verify(attributeTypeRepository).delete(attributeType);
    }

    @Test
    public void givenInvalidId_whenDeleteAttributeType_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(attributeTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> attributeTypeService.deleteAttributeType(id));

        verify(attributeService, never()).deleteAttributeByAttributeType(any());
        verify(attributeTypeRepository, never()).delete(any());
    }
}
