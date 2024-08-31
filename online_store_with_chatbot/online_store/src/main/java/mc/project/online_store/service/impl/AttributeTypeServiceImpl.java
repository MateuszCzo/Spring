package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.AttributeTypeRequest;
import mc.project.online_store.dto.response.AttributeTypeResponse;
import mc.project.online_store.model.AttributeType;
import mc.project.online_store.repository.AttributeTypeRepository;
import mc.project.online_store.service.admin.AttributeService;
import mc.project.online_store.service.admin.AttributeTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeTypeServiceImpl implements AttributeTypeService {
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeService attributeService;
    private final ObjectMapper objectMapper;

    @Override
    public List<AttributeTypeResponse> getPage(String name, int page, int pageSize) {
        Page<AttributeType> response = attributeTypeRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return response
                .map(attributeType -> objectMapper.convertValue(attributeType, AttributeTypeResponse.class))
                .toList();
    }

    @Override
    public AttributeTypeResponse getAttributeType(long id) {
        AttributeType response = attributeTypeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(response, AttributeTypeResponse.class);
    }

    @Override
    public AttributeTypeResponse postAttributeType(AttributeTypeRequest request) {
        AttributeType attributeType = objectMapper.convertValue(request, AttributeType.class);

        attributeTypeRepository.save(attributeType);

        return objectMapper.convertValue(attributeType, AttributeTypeResponse.class);
    }

    @Override
    public AttributeTypeResponse putAttributeType(long id, AttributeTypeRequest request) {
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            attributeType = objectMapper.updateValue(attributeType, request);
        } catch (JsonMappingException e) { }

        attributeTypeRepository.save(attributeType);

        return objectMapper.convertValue(attributeType, AttributeTypeResponse.class);
    }

    @Override
    @Transactional
    public void deleteAttributeType(long id) {
        AttributeType attributeType = attributeTypeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        attributeService.deleteAttributeByAttributeType(attributeType);

        attributeTypeRepository.delete(attributeType);
    }
}
