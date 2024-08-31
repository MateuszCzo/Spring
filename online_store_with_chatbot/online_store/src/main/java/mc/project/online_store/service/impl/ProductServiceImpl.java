package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ProductRequest;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.admin.ImageService;
import mc.project.online_store.service.admin.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttributeRepository attributeRepository;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;

    @Override
    public List<ProductResponse> getPage(String name, int page, int pageSize) {
        Page<Product> products = productRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return products
                .map(product -> objectMapper.convertValue(product, ProductResponse.class))
                .toList();
    }

    @Override
    public List<ProductResponse> getPageByOrderId(long orderId, int page, int pageSize) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return order.getProducts().stream()
                .map(product -> objectMapper.convertValue(product, ProductResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductResponse> getPageByCategoryId(long categoryId, int page, int pageSize) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);

        Page<Product> products = productRepository.findByCategory(
                category, PageRequest.of(page, pageSize));

        return products
                .map(product -> objectMapper.convertValue(product, ProductResponse.class))
                .toList();
    }

    @Override
    public ProductResponse getProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse postProduct(ProductRequest request) {
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        Set<Attachment> attachments = attachmentRepository.findByIdIn(request.getAttachmentsIds());
        Set<Attribute> attributes = attributeRepository.findByIdIn(request.getAttributesIds());

        Image cover = imageService.postImage(request.getCover());
        Set<Image> images = request.getImages().stream()
                .map(imageService::postImage)
                .collect(Collectors.toSet());

        Product product = objectMapper.convertValue(request, Product.class);
        product.setImage(cover);
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        product.setAttachments(attachments);
        product.setAttributes(attributes);
        product.setImages(images);

        productRepository.save(product);

        return objectMapper.convertValue(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse putProduct(long id, ProductRequest request) {
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        Set<Attachment> attachments = attachmentRepository.findByIdIn(request.getAttachmentsIds());
        Set<Attribute> attributes = attributeRepository.findByIdIn(request.getAttributesIds());
        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            product = objectMapper.updateValue(product, request);
        } catch (JsonMappingException e) { }
        product.setManufacturer(manufacturer);
        product.setCategory(category);
        product.setAttachments(attachments);
        product.setAttributes(attributes);

        productRepository.save(product);

        return objectMapper.convertValue(product, ProductResponse.class);
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int orderCount = orderRepository.countDistinctByProducts(product);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot delete product - product contain order");
        }

        imageService.deleteImage(product.getImage());
        product.getImages().forEach(imageService::deleteImage);

        productRepository.delete(product);
    }
}
