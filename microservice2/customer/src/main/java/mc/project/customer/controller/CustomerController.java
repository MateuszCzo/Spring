package mc.project.customer.controller;

import lombok.extern.slf4j.Slf4j;
import mc.project.customer.dto.CustomerRegistrationRequest;
import mc.project.customer.dto.CustomerRegistrationResponse;
import mc.project.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public CustomerRegistrationResponse registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration {}", customerRegistrationRequest);
        return customerService.registerCustomer(customerRegistrationRequest);
    }
}
