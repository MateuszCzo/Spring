package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.PaymentRequest;
import mc.project.online_store.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getPage(String name, int page, int pageSize);

    PaymentResponse getPayment(long id);

    PaymentResponse getPaymentByOrderId(long orderId);

    PaymentResponse postPayment(PaymentRequest request);

    PaymentResponse putPayment(long id, PaymentRequest request);

    void deletePayment(long id);
}
