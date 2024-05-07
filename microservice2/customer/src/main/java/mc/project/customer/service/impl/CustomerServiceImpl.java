package mc.project.customer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mc.project.customer.amqp.AmqpNotificationProducer;
import mc.project.customer.client.FraudClient;
import mc.project.customer.client.NotificationClient;
import mc.project.customer.dto.CustomerRegistrationRequest;
import mc.project.customer.dto.CustomerRegistrationResponse;
import mc.project.customer.dto.FraudCheckResponse;
import mc.project.customer.dto.NotificationRequest;
import mc.project.customer.model.Customer;
import mc.project.customer.repository.CustomerRepository;
import mc.project.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;
    private final AmqpNotificationProducer amqpNotificationProducer;

    @Override
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstname(customerRegistrationRequest.firstname())
                .lastname(customerRegistrationRequest.lastname())
                .email(customerRegistrationRequest.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) throw new IllegalThreadStateException("Fraudster");

        String message = String.format("Message to client [id/email] [%d/%s]", customer.getId(), customer.getEmail());

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                "Hello World!"
        );

//        notificationClient.sendNotification(notificationRequest);

        amqpNotificationProducer.publish(message, exchange, routingKey);

        amqpNotificationProducer.publish(notificationRequest, exchange, jsonRoutingKey);

        return objectMapper.convertValue(customer, CustomerRegistrationResponse.class);
    }
}
