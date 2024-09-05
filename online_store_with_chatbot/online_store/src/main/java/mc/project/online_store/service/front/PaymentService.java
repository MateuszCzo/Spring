package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getPage(String name, int page, int pageSize);

    PaymentResponse getPayment(long id);

    PaymentResponse getUserOrderPayment(long orderId);
}
