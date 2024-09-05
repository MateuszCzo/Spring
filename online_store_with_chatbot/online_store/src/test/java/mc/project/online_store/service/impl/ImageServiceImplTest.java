package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.ImageRequest;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.dto.response.ImageResponse;
import mc.project.online_store.model.Category;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Manufacturer;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.CategoryRepository;
import mc.project.online_store.repository.ImageRepository;
import mc.project.online_store.repository.ManufacturerRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.util.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {
    @Value("${file.path.image}")
    private String imageFilePath;

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private FileService fileService;

    @Spy
    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidMultipartFile_whenPostImage_thenReturnsImage() {
        MultipartFile file = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                new byte[]{(byte)0x1});

        Image serviceResponse = imageService.postImage(file);

        verify(fileService).postFile(file, imageFilePath);
        verify(imageRepository).save(serviceResponse);

        assertEquals(file.getName(), serviceResponse.getName());
        assertEquals(imageFilePath + file.getOriginalFilename(), serviceResponse.getPath());
        assertEquals(file.getContentType(), serviceResponse.getType());
    }

    @Test
    public void givenValidIdAndMultipartFile_whenPutImage_thenReturnsImage() {
        long id = 1;
        MultipartFile file = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                new byte[]{(byte)0x1});
        String originalPath = "original path";
        Image image = mock();

        when(image.getPath()).thenReturn(originalPath);
        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        Image serviceResponse = imageService.putImage(id, file);

        verify(imageRepository).findById(id);
        verify(fileService).postFile(file, imageFilePath);
        verify(fileService).deleteFile(originalPath);
        verify(image).setName(file.getName());
        verify(image).setPath(imageFilePath + file.getOriginalFilename());
        verify(image).setType(file.getContentType());
        verify(imageRepository).save(image);

        assertEquals(image, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndMultipartFile_whenPutImage_thenThrowsEntityNotFoundException() {
        long id = 1;
        MultipartFile file = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                new byte[]{(byte)0x1});

        when(imageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.putImage(id, file));

        verify(imageRepository).findById(id);
        verify(fileService, never()).postFile(any(), any());
        verify(fileService, never()).deleteFile(any());
        verify(imageRepository, never()).save(any());
    }

    @Test
    public void givenValidImage_whenDeleteImage_thenReturnsVoid() {
        Image image = new Image();

        imageService.deleteImage(image);

        verify(fileService).deleteFile(image.getPath());
        verify(imageRepository).delete(image);
    }

    @Test
    public void givenValidId_whenDeleteImage_thenReturnsVoid() {
        long id = 1;
        Image image = new Image();

        when(imageRepository.findById(id)).thenReturn(Optional.of(image));

        imageService.deleteImage(id);

        verify(imageService).deleteImage(image);
    }

    @Test
    public void givenInvalidId_whenDeleteImage_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(imageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.deleteImage(id));

        verify(imageService, never()).deleteImage(any());
    }

    @Test
    public void givenValidIdAndImageRequest_whenPutImage_thenReturnsImageResponse() {
        long id = 1;
        MultipartFile file = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                new byte[]{(byte)0x1});
        ImageRequest request = new ImageRequest();
        request.setFile(file);
        Image image = mock();
        image.setPath("old path");
        ImageResponse response = new ImageResponse();


        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        ImageResponse serviceResponse = imageService.putImage(id, request);

        verify(imageRepository).findById(id);
        verify(fileService).postFile(request.getFile(), imageFilePath);
        verify(fileService).deleteFile(image.getPath());
        verify(image).setName(file.getName());
        verify(image).setPath(imageFilePath + file.getOriginalFilename());
        verify(image).setType(file.getContentType());
        verify(imageRepository).save(image);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidProductIdAndImageRequest_whenPostProductImage_thenImageResponse() {
        long productId = 1;
        ImageRequest request = new ImageRequest();
        MultipartFile file = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                new byte[]{(byte)0x1});
        request.setFile(file);
        Product product = mock();
        Set<Image> imageSet = mock();
        Image image = new Image();
        ImageResponse response = new ImageResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doReturn(image).when(imageService).postImage(request.getFile());
        when(product.getImages()).thenReturn(imageSet);
        when(imageSet.add(image)).thenReturn(true);
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        ImageResponse serviceResponse = imageService.postProductImage(productId, request);

        verify(productRepository).findById(productId);
        verify(imageService).postImage(request.getFile());
        verify(imageSet).add(image);
        verify(productRepository).save(product);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidProductIdAndImageRequest_thenPostProductImage_thenThrowsEntityNotFoundException() {
        long productId = 1;
        ImageRequest request = new ImageRequest();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.postProductImage(productId, request));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidProductIdAndId_whenDeleteProductImage_thenReturnsVoid() {
        long productId = 1;
        long id = 2;
        Image image = new Image();
        image.setPath("path");
        image.setId(id);
        Set<Image> imageSet = new HashSet<>(List.of(image));
        Product product = mock();
        product.setImages(imageSet);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getImages()).thenReturn(imageSet);

        imageService.deleteProductImage(productId, id);

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
        verify(fileService).deleteFile(image.getPath());
        verify(imageRepository).delete(image);

        assertEquals(0, imageSet.size());
    }

    @Test
    public void givenInvalidProductIdAndId_whenDeleteProductImage_thenThrowsEntityNotFoundException() {
        long productId = 1;
        long id = 2;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.deleteProductImage(productId, id));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
        verify(fileService, never()).deleteFile(any());
        verify(imageRepository, never()).delete(any());
    }

    @Test
    public void givenValidProductIdAndInvalidId_whenDeleteProductImage_thenThrowsEntityNotFoundException() {
        long productId = 1;
        long id = 2;
        Set<Image> imageSet = new HashSet<>();
        Product product = mock();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getImages()).thenReturn(imageSet);

        assertThrows(EntityNotFoundException.class, () -> imageService.deleteProductImage(productId, id));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
        verify(fileService, never()).deleteFile(any());
        verify(imageRepository, never()).delete(any());
    }

    @Test
    public void givenValidCategoryId_whenGetCategoryImage_thenReturnsImageResponse() {
        long categoryId = 1;
        Category category = mock();
        Image image = new Image();
        ImageResponse response = new ImageResponse();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(category.getImage()).thenReturn(image);
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        ImageResponse serviceResponse = imageService.getCategoryImage(categoryId);

        verify(categoryRepository).findById(categoryId);
        verify(category).getImage();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidCategoryId_whenGetCategoryImage_thenThrowsEntityNotFoundException() {
        long categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.getCategoryImage(categoryId));

        verify(categoryRepository).findById(categoryId);
    }

    @Test
    public void givenValidManufacturerId_whenGetManufacturerImage_thenReturnsImageResponse() {
        long manufacturerId = 1;
        Manufacturer manufacturer = mock();
        Image image = new Image();
        ImageResponse response = new ImageResponse();

        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.of(manufacturer));
        when(manufacturer.getImage()).thenReturn(image);
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        ImageResponse serviceResponse = imageService.getCategoryImage(manufacturerId);

        verify(manufacturerRepository).findById(manufacturerId);
        verify(manufacturer).getImage();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidManufacturerId_whenGetManufacturerImage_thenThrowsEntityNotFoundException() {
        long manufacturerId = 1;

        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.getCategoryImage(manufacturerId));

        verify(manufacturerRepository).findById(manufacturerId);
    }

    @Test
    public void givenValidProductId_whenGetProductImage_thenReturnsImageResponse() {
        long productId = 1;
        Product product = mock();
        Image image = new Image();
        ImageResponse response = new ImageResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getImage()).thenReturn(image);
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        ImageResponse serviceResponse = imageService.getProductImage(productId);

        verify(productRepository).findById(productId);
        verify(product).getImage();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidProductId_whenGetProductImage_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.getProductImage(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void givenValidProductId_whenGetProductImages_thenReturnsImageResponseList() {
        long productId = 1;
        Product product = mock();
        Image image = new Image();
        Set<Image> imageSet = new HashSet<>(List.of(image));
        ImageResponse response = new ImageResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.getImages()).thenReturn(imageSet);
        when(objectMapper.convertValue(image, ImageResponse.class)).thenReturn(response);

        List<ImageResponse> serviceResponse = imageService.getProductImages(productId);

        verify(productRepository).findById(productId);
        verify(product).getImages();

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidProductId_whenGetProductImages_thenThrowsEntityNotFoundException() {
        long productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> imageService.getProductImages(productId));

        verify(productRepository).findById(productId);
    }
}