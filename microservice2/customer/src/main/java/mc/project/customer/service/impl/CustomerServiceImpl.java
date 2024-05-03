package mc.project.customer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mc.project.customer.dto.CustomerRegistrationRequest;
import mc.project.customer.dto.CustomerRegistrationResponse;
import mc.project.customer.model.Customer;
import mc.project.customer.repository.CustomerRepository;
import mc.project.customer.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstname(customerRegistrationRequest.firstname())
                .lastname(customerRegistrationRequest.lastname())
                .email(customerRegistrationRequest.email())
                .build();

        customerRepository.save(customer);

        return objectMapper.convertValue(customer, CustomerRegistrationResponse.class);
    }
}
