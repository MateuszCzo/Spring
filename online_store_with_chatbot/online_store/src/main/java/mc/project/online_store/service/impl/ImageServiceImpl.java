package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ImageRequest;
import mc.project.online_store.dto.response.ImageResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.ImageRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.ImageService;
import mc.project.online_store.service.util.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${file.path.image}")
    private String imageFilePath;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @Override
    public Image postImage(MultipartFile file) {
        fileService.postFile(file, imageFilePath);

        Image image = new Image();
        image.setName(file.getName());
        image.setPath(imageFilePath + file.getOriginalFilename());
        image.setType(file.getContentType());

        imageRepository.save(image);

        return image;
    }

    @Override
    public Image putImage(Long id, MultipartFile file) {
        Image image = imageRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        fileService.postFile(file, imageFilePath);

        fileService.deleteFile(image.getPath());

        image.setName(file.getName());
        image.setPath(imageFilePath + file.getOriginalFilename());
        image.setType(file.getContentType());

        imageRepository.save(image);

        return image;
    }

    @Override
    public void deleteImage(Image image) {
        // @todo check (maybe) productCount, categoryCount, manufacturerCount and throw RelationConflictException

        fileService.deleteFile(image.getPath());

        imageRepository.delete(image);
    }

    @Override
    public void deleteImage(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        deleteImage(image);
    }

    @Override
    public ImageResponse putImage(long id, ImageRequest request) {
        Image image = putImage(id, request.getFile());

        return objectMapper.convertValue(image, ImageResponse.class);
    }

    @Override
    public ImageResponse postProductImage(long productId, ImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        Image image = postImage(request.getFile());

        product.getImages().add(image);

        productRepository.save(product);

        return objectMapper.convertValue(image, ImageResponse.class);
    }

    @Override
    public void deleteProductImage(long productId, long id) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        Image image = product.getImages()
                .stream()
                .filter(searchImage -> searchImage.getId() == id)
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        product.getImages().remove(image);

        productRepository.save(product);

        deleteImage(image);
    }
}
