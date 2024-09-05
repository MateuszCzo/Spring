package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.PaymentRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.dto.response.PaymentResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Order;
import mc.project.online_store.model.Payment;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.repository.PaymentRepository;
import mc.project.online_store.service.auth.UserService;
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

class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsPaymentResponseList() {
        String name = "";
        int page = 0;
        int pageSize = 10;
        Payment payment = new Payment();
        Page<Payment> paymentPage = new PageImpl<>(List.of(payment));
        PaymentResponse response = new PaymentResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(paymentRepository.findByNameContaining(name, pageRequest)).thenReturn(paymentPage);
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        List<PaymentResponse> serviceResponse = paymentService.getPage(name, page, pageSize);

        verify(paymentRepository).findByNameContaining(name, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidId_whenGetPayment_thenReturnsPaymentResponse() {
        long id = 1;
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        PaymentResponse serviceResponse = paymentService.getPayment(id);

        verify(paymentRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetPayment_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.getPayment(id));

        verify(paymentRepository).findById(id);
    }

    @Test
    public void givenValidOrderId_whenGetPaymentByOrderId_thenReturnsPaymentResponse() {
        long orderId = 1;
        Order order = mock();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(order.getPayment()).thenReturn(payment);
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        PaymentResponse serviceResponse = paymentService.getPaymentByOrderId(orderId);

        verify(orderRepository).findById(orderId);
        verify(order).getPayment();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidOrderId_whenGetPaymentByOrderId_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.getPaymentByOrderId(orderId));

        verify(orderRepository).findById(orderId);
    }

    @Test
    public void givenValidPaymentRequest_whenPostPayment_thenReturnsPaymentResponse() {
        PaymentRequest request = new PaymentRequest();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(objectMapper.convertValue(request, Payment.class)).thenReturn(payment);
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        PaymentResponse serviceResponse = paymentService.postPayment(request);

        verify(objectMapper).convertValue(request, Payment.class);
        verify(paymentRepository).save(payment);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenValidIdAndPaymentRequest_whenPutPayment_thenReturnsPaymentResponse() throws JsonMappingException {
        long id = 1;
        PaymentRequest request = new PaymentRequest();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(objectMapper.updateValue(payment, request)).thenReturn(payment);
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        PaymentResponse serviceResponse = paymentService.putPayment(id, request);

        verify(paymentRepository).findById(id);
        verify(objectMapper).updateValue(payment, request);
        verify(paymentRepository).save(payment);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidIdAndPaymentRequest_whenPutPayment_thenThrowsEntityNotFoundException() {
        long id = 1;
        PaymentRequest request = new PaymentRequest();

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.putPayment(id, request));

        verify(paymentRepository).findById(id);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeletePayment_thenReturnsVoid() {
        long id = 1;
        Payment payment = new Payment();

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(orderRepository.countDistinctByPayment(payment)).thenReturn(0);

        paymentService.deletePayment(id);

        verify(paymentRepository).findById(id);
        verify(orderRepository).countDistinctByPayment(payment);
        verify(paymentRepository).delete(payment);
    }

    @Test
    public void givenInvalidId_whenDeletePayment_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.deletePayment(id));

        verify(paymentRepository).findById(id);
        verify(paymentRepository, never()).delete(any());
    }

    @Test
    public void givenValidIdAndInvalidOrderCount_whenDeletePayment_thenThrowsRelationConflictException() {
        long id = 1;
        Payment payment = new Payment();

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(orderRepository.countDistinctByPayment(payment)).thenReturn(1);

        assertThrows(RelationConflictException.class, () -> paymentService.deletePayment(id));

        verify(paymentRepository).findById(id);
        verify(orderRepository).countDistinctByPayment(payment);
        verify(paymentRepository, never()).delete(any());
    }

    @Test
    public void givenValidOrderId_whenGetUserOrderPayment_thenReturnsPaymentResponse() {
        long orderId = 1;
        User user = new User();
        Order order = mock();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.of(order));
        when(order.getPayment()).thenReturn(payment);
        when(objectMapper.convertValue(payment, PaymentResponse.class)).thenReturn(response);

        PaymentResponse serviceResponse = paymentService.getUserOrderPayment(orderId);

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByIdAndUser(orderId, user);
        verify(order).getPayment();

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidOrderId_whenGetUserOrderPayment_thenThrowsEntityNotFoundException() {
        long orderId = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.getUserOrderPayment(orderId));

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByIdAndUser(orderId, user);
    }

    @Test
    public void givenInvalidUser_whenGetUserOrderPayment_thenThrowsEntityNotFoundException() {
        long orderId = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.getUserOrderPayment(orderId));

        verify(userService).getLoggedInUser();
    }
}