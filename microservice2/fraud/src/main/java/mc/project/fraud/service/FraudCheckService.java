package mc.project.fraud.service;

import mc.project.fraud.dto.FraudCheckResponse;

public interface FraudCheckService {
    FraudCheckResponse isFraudulentCustomer(Long customerId);
}
