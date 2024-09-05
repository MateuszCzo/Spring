package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ManufacturerRequest;
import mc.project.online_store.dto.response.ManufacturerResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Manufacturer;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.ManufacturerRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.ImageService;
import mc.project.online_store.service.admin.ManufacturerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService, mc.project.online_store.service.front.ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;

    @Override
    public List<ManufacturerResponse> getPage(String name, int page, int pageSize) {
        Page<Manufacturer> manufacturers = manufacturerRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return manufacturers
                .map(manufacturer -> objectMapper.convertValue(manufacturer, ManufacturerResponse.class))
                .toList();
    }

    @Override
    public ManufacturerResponse getManufacturer(long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(manufacturer, ManufacturerResponse.class);
    }

    @Override
    @Transactional
    public ManufacturerResponse getManufacturerByProductId(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(product.getManufacturer(), ManufacturerResponse.class);
    }

    @Override
    @Transactional
    public ManufacturerResponse postManufacturer(ManufacturerRequest request) {
        Manufacturer manufacturer = objectMapper.convertValue(request, Manufacturer.class);

        Image image = imageService.postImage(request.getFile());

        manufacturer.setImage(image);

        manufacturerRepository.save(manufacturer);

        return objectMapper.convertValue(manufacturer, ManufacturerResponse.class);
    }

    @Override
    @Transactional
    public ManufacturerResponse putManufacturer(long id, ManufacturerRequest request) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            manufacturer = objectMapper.updateValue(manufacturer, request);
        } catch (JsonMappingException e) { }

        manufacturerRepository.save(manufacturer);

        return objectMapper.convertValue(manufacturer, ManufacturerResponse.class);
    }

    @Override
    @Transactional
    public void deleteManufacturer(long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int productCount = productRepository.countDistinctByManufacturer(manufacturer);

        if (productCount > 0) {
            throw new RelationConflictException("Cannot delete manufacturer - manufacturer contain product");
        }

        imageService.deleteImage(manufacturer.getImage());

        manufacturerRepository.delete(manufacturer);
    }
}
