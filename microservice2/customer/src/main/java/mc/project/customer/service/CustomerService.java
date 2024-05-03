package mc.project.customer.service;

import mc.project.customer.dto.CustomerRegistrationRequest;
import mc.project.customer.dto.CustomerRegistrationResponse;

public interface CustomerService {
    CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest customerRegistrationRequest);
}
