package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.DeliveryRequest;
import mc.project.online_store.dto.response.DeliveryResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Delivery;
import mc.project.online_store.model.Order;
import mc.project.online_store.repository.DeliveryRepository;
import mc.project.online_store.repository.OrderRepository;
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

class DeliveryServiceImplTest {
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsDeliveryResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Delivery delivery = new Delivery();
        Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery));
        DeliveryResponse response = new DeliveryResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(deliveryRepository.findByNameContaining(name, pageRequest)).thenReturn(deliveryPage);
        when(objectMapper.convertValue(delivery, DeliveryResponse.class)).thenReturn(response);

        List<DeliveryResponse> serviceResponse = deliveryService.getPage(name, page, pageSize);

        verify(deliveryRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetDelivery_thenReturnsDeliveryResponse() {
        long id = 1;
        Delivery delivery = new Delivery();
        DeliveryResponse response = new DeliveryResponse();

        when(deliveryRepository.findById(id)).thenReturn(Optional.of(delivery));
        when(objectMapper.convertValue(delivery, DeliveryResponse.class)).thenReturn(response);

        DeliveryResponse serviceResponse = deliveryService.getDelivery(id);

        verify(deliveryRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetDelivery_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(deliveryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryService.getDelivery(id));

        verify(deliveryRepository).findById(id);
    }

    @Test
    public void givenValidOrderId_whenGetDeliveryByOrderId_thenReturnsDeliveryResponse() {
        long orderId = 1;
        Order order = mock();
        Delivery delivery = new Delivery();
        DeliveryResponse response = new DeliveryResponse();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getDelivery()).thenReturn(delivery);
        when(objectMapper.convertValue(delivery, DeliveryResponse.class)).thenReturn(response);

        DeliveryResponse serviceResponse = deliveryService.getDeliveryByOrderId(orderId);

        verify(orderRepository).findById(orderId);
        verify(order).getDelivery();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidOrderId_whenGetDeliveryByOrderId_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryService.getDeliveryByOrderId(orderId));

        verify(orderRepository).findById(orderId);
    }

    @Test
    public void givenValidDeliveryRequest_whenPostDelivery_thenReturnsDeliveryResponse() {
        DeliveryRequest request = new DeliveryRequest();
        Delivery delivery = new Delivery();
        DeliveryResponse response = new DeliveryResponse();

        when(objectMapper.convertValue(request, Delivery.class)).thenReturn(delivery);
        when(objectMapper.convertValue(delivery, DeliveryResponse.class)).thenReturn(response);

        DeliveryResponse serviceResponse = deliveryService.postDelivery(request);

        verify(deliveryRepository).save(delivery);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidIdAndDeliveryRequest_whenPutDelivery_thenReturnsDeliveryResponse() throws JsonMappingException {
        long id = 1;
        DeliveryRequest request = new DeliveryRequest();
        Delivery delivery = new Delivery();
        DeliveryResponse response = new DeliveryResponse();

        when(deliveryRepository.findById(id)).thenReturn(Optional.of(delivery));
        when(objectMapper.updateValue(delivery, request)).thenReturn(delivery);
        when(objectMapper.convertValue(delivery, DeliveryResponse.class)).thenReturn(response);

        DeliveryResponse serviceResponse = deliveryService.putDelivery(id, request);

        verify(deliveryRepository).findById(id);
        verify(objectMapper).updateValue(delivery, request);
        verify(deliveryRepository).save(delivery);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndDeliveryRequest_whenPutDelivery_thenThrowsEntityNotFoundException() throws JsonMappingException {
        long id = 1;
        DeliveryRequest request = new DeliveryRequest();

        when(deliveryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryService.putDelivery(id, request));

        verify(deliveryRepository).findById(id);
        verify(objectMapper, never()).updateValue(any(), any());
        verify(deliveryRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteDelivery_thenReturnsVoid() {
        long id = 1;
        Delivery delivery = new Delivery();

        when(deliveryRepository.findById(id)).thenReturn(Optional.of(delivery));
        when(orderRepository.countDistinctByDelivery(delivery)).thenReturn(0);

        deliveryService.deleteDelivery(id);

        verify(deliveryRepository).findById(id);
        verify(orderRepository).countDistinctByDelivery(delivery);
        verify(deliveryRepository).delete(delivery);
    }

    @Test
    public void givenInvalidId_whenDeleteDelivery_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(deliveryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryService.deleteDelivery(id));

        verify(deliveryRepository).findById(id);
        verify(deliveryRepository, never()).delete(any());
    }

    @Test
    public void givenValidIdAndInvalidOrderCount_whenDeleteDelivery_thenThrowsRelationConflictException() {
        long id = 1;
        Delivery delivery = new Delivery();

        when(deliveryRepository.findById(id)).thenReturn(Optional.of(delivery));
        when(orderRepository.countDistinctByDelivery(delivery)).thenReturn(1);

        assertThrows(RelationConflictException.class, () ->deliveryService.deleteDelivery(id));

        verify(deliveryRepository).findById(id);
        verify(orderRepository).countDistinctByDelivery(delivery);
        verify(deliveryRepository, never()).delete(any());
    }
}