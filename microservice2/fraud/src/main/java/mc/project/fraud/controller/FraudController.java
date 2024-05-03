package mc.project.fraud.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.project.fraud.dto.FraudCheckResponse;
import mc.project.fraud.service.FraudCheckService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/fraud-check")
@RequiredArgsConstructor
public class FraudController {
    private final FraudCheckService fraudCheckService;

    @GetMapping("/{customerId}")
    public FraudCheckResponse isFraud(@PathVariable Long customerId) {
        log.info("check is fraudulent customer {}", customerId);
        return fraudCheckService.isFraudulentCustomer(customerId);
    }
}
