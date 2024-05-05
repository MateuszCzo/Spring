package mc.project.customer.fallback;

import mc.project.customer.client.FraudClient;
import mc.project.customer.dto.FraudCheckResponse;

public class FraudFallback implements FraudClient {
    @Override
    public FraudCheckResponse isFraudster(Long customerId) {
        return new FraudCheckResponse(false);
    }
}
