package mc.project.customer.client;

import mc.project.customer.config.FraudClientConfig;
import mc.project.customer.dto.FraudCheckResponse;
import mc.project.customer.fallback.FraudFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "FRAUD",
        configuration = FraudClientConfig.class,
        fallback = FraudFallback.class
)
public interface FraudClient {
    @GetMapping("api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Long customerId);
}
