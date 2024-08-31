package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.ManufacturerRequest;
import mc.project.online_store.dto.response.ManufacturerResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Manufacturer;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.ManufacturerRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManufacturerServiceImplTest {
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsManufacturerResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Manufacturer manufacturer = new Manufacturer();
        Page<Manufacturer> manufacturerPage = new PageImpl<>(List.of(manufacturer));
        ManufacturerResponse response = new ManufacturerResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(manufacturerRepository.findByNameContaining(name, pageRequest)).thenReturn(manufacturerPage);
        when(objectMapper.convertValue(manufacturer, ManufacturerResponse.class)).thenReturn(response);

        List<ManufacturerResponse> serviceResponse = manufacturerService.getPage(name, page, pageSize);

        verify(manufacturerRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetManufacturer_thenReturnsManufacturerResponse() {
        long id = 1;
        Manufacturer manufacturer = new Manufacturer();
        ManufacturerResponse response = new ManufacturerResponse();

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));
        when(objectMapper.convertValue(manufacturer, ManufacturerResponse.class)).thenReturn(response);

        ManufacturerResponse serviceResponse = manufacturerService.getManufacturer(id);

        verify(manufacturerRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetManufacturer_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manufacturerService.getManufacturer(id));

        verify(manufacturerRepository).findById(id);
    }

    @Test
    public void givenValidProductId_whenGetManufacturerByProductId_thenReturnsManufacturerResponse() {
        long productId = 1;
        Product product = mock();
        Manufacturer manufacturer = new Manufacturer();
        ManufacturerResponse response = new ManufacturerResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getManufacturer()).thenReturn(manufacturer);
        when(objectMapper.convertValue(manufacturer, ManufacturerResponse.class)).thenReturn(response);

        ManufacturerResponse serviceResponse = manufacturerService.getManufacturerByProductId(productId);

        verify(productRepository).findById(productId);
        verify(product).getManufacturer();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidProductId_whenGetManufacturerByProductId_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manufacturerService.getManufacturerByProductId(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidManufacturerRequest_whenPostManufacturer_thenReturnsManufacturerResponse() {
        ManufacturerRequest request = new ManufacturerRequest();
        MultipartFile file = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes());
        request.setFile(file);
        Manufacturer manufacturer = mock();
        Image image = new Image();
        ManufacturerResponse response = new ManufacturerResponse();

        when(objectMapper.convertValue(request, Manufacturer.class)).thenReturn(manufacturer);
        when(imageService.postImage(request.getFile())).thenReturn(image);
        when(objectMapper.convertValue(manufacturer, ManufacturerResponse.class)).thenReturn(response);

        ManufacturerResponse serviceResponse = manufacturerService.postManufacturer(request);

        verify(objectMapper).convertValue(request, Manufacturer.class);
        verify(imageService).postImage(request.getFile());
        verify(manufacturer).setImage(image);
        verify(manufacturerRepository).save(manufacturer);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidManufacturerRequest_whenPutManufacturer_thenReturnsManufacturerResponse() throws JsonMappingException {
        long id = 1;
        ManufacturerRequest request = new ManufacturerRequest();
        Manufacturer manufacturer = mock();
        ManufacturerResponse response = new ManufacturerResponse();

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));
        when(objectMapper.updateValue(manufacturer, request)).thenReturn(manufacturer);
        when(objectMapper.convertValue(manufacturer, ManufacturerResponse.class)).thenReturn(response);

        ManufacturerResponse serviceResponse = manufacturerService.putManufacturer(id, request);

        verify(manufacturerRepository).findById(id);
        verify(objectMapper).updateValue(manufacturer, request);
        verify(manufacturerRepository).save(manufacturer);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidManufacturerRequest_whenPutManufacturer_thenThrowsEntityNotFoundException() {
        long id = 1;
        ManufacturerRequest request = new ManufacturerRequest();

        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manufacturerService.putManufacturer(id, request));

        verify(manufacturerRepository).findById(id);
        verify(manufacturerRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteManufacturer_thenReturnsVoid() {
        long id = 1;
        Manufacturer manufacturer = new Manufacturer();

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));
        when(productRepository.countDistinctByManufacturer(manufacturer)).thenReturn(0);

        manufacturerService.deleteManufacturer(id);

        verify(manufacturerRepository).findById(id);
        verify(productRepository).countDistinctByManufacturer(manufacturer);
        verify(imageService).deleteImage(manufacturer.getImage());
        verify(manufacturerRepository).delete(manufacturer);
    }

    @Test
    public void givenInvalidId_whenDeleteManufacturer_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manufacturerService.deleteManufacturer(id));

        verify(manufacturerRepository).findById(id);
        verify(imageService, never()).deleteImage(any());
        verify(manufacturerRepository, never()).delete(any());
    }

    @Test
    public void givenValidIdAndInvalidProductCount_whenDeleteManufacturer_thenThrowsEntityNotFoundException() {
        long id = 1;
        Manufacturer manufacturer = new Manufacturer();

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));
        when(productRepository.countDistinctByManufacturer(manufacturer)).thenReturn(1);

        assertThrows(RelationConflictException.class, () -> manufacturerService.deleteManufacturer(id));

        verify(manufacturerRepository).findById(id);
        verify(productRepository).countDistinctByManufacturer(manufacturer);
        verify(imageService, never()).deleteImage(any());
        verify(manufacturerRepository, never()).delete(any());
    }
}