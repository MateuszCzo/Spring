package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttributeRequest;
import mc.project.online_store.dto.response.AttributeResponse;
import mc.project.online_store.model.Attribute;
import mc.project.online_store.model.AttributeType;
import mc.project.online_store.model.Product;
import mc.project.online_store.repository.AttributeRepository;
import mc.project.online_store.repository.AttributeTypeRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.service.admin.AttributeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<AttributeResponse> getAttributeListByProductId(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        return product.getAttributes()
                .stream()
                .map(attribute -> objectMapper.convertValue(attribute, AttributeResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<AttributeResponse> getPageByAttributeTypeId(long attributeId, int page, int pageSize) {
        AttributeType attributeType = attributeTypeRepository.findById(attributeId)
                .orElseThrow(EntityNotFoundException::new);

        Page<Attribute> response = attributeRepository
                .findByAttributeType(attributeType, PageRequest.of(page, pageSize));

        return response.stream()
                .map(attribute -> objectMapper.convertValue(attribute, AttributeResponse.class))
                .toList();
    }

    @Override
    public AttributeResponse getAttribute(long id) {
        Attribute response = attributeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(response, AttributeResponse.class);
    }

    @Override
    public AttributeResponse postAttribute(AttributeRequest request) {
        AttributeType attributeType = attributeTypeRepository
                .findById(request.getAttributeTypeId())
                .orElseThrow(EntityNotFoundException::new);

        Attribute attribute = objectMapper.convertValue(request, Attribute.class);
        attribute.setAttributeType(attributeType);

        attributeRepository.save(attribute);

        return objectMapper.convertValue(attribute, AttributeResponse.class);
    }

    @Override
    @Transactional
    public AttributeResponse putAttribute(long id, AttributeRequest request) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        AttributeType attributeType = attributeTypeRepository
                .findById(request.getAttributeTypeId())
                .orElseThrow(EntityNotFoundException::new);

        try {
            attribute = objectMapper.updateValue(attribute, request);
        } catch (JsonMappingException e) { }
        attribute.setAttributeType(attributeType);

        attributeRepository.save(attribute);

        return objectMapper.convertValue(attribute, AttributeResponse.class);
    }

    @Override
    public void deleteAttribute(long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        attributeRepository.delete(attribute);
    }

    @Override
    public void deleteAttributeByAttributeType(AttributeType attributeType) {
        List<Attribute> attributes = attributeRepository.findByAttributeType(attributeType);

        attributeRepository.deleteAll(attributes);
    }
}
