package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.PaymentRequest;
import mc.project.online_store.dto.response.PaymentResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Order;
import mc.project.online_store.model.Payment;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.repository.PaymentRepository;
import mc.project.online_store.service.admin.PaymentService;
import mc.project.online_store.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService, mc.project.online_store.service.front.PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Override
    public List<PaymentResponse> getPage(String name, int page, int pageSize) {
        Page<Payment> payments = paymentRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return payments
                .map(payment -> objectMapper.convertValue(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public PaymentResponse getPayment(long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(payment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order.getPayment(), PaymentResponse.class);
    }

    @Override
    public PaymentResponse postPayment(PaymentRequest request) {
        Payment payment = objectMapper.convertValue(request, Payment.class);

        paymentRepository.save(payment);

        return objectMapper.convertValue(payment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse putPayment(long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            payment = objectMapper.updateValue(payment, request);
        } catch (JsonMappingException e) { }

        paymentRepository.save(payment);

        return objectMapper.convertValue(payment, PaymentResponse.class);
    }

    @Override
    @Transactional
    public void deletePayment(long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int orderCount = orderRepository.countDistinctByPayment(payment);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot delete payment - payment contain orders");
        }

        paymentRepository.delete(payment);
    }

    @Override
    public PaymentResponse getUserOrderPayment(long orderId) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order.getPayment(), PaymentResponse.class);
    }
}
