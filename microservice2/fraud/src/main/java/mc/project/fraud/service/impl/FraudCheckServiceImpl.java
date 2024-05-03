package mc.project.fraud.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.fraud.dto.FraudCheckResponse;
import mc.project.fraud.model.FraudCheckHistory;
import mc.project.fraud.repository.FraudCheckHistoryRepository;
import mc.project.fraud.service.FraudCheckService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudCheckServiceImpl implements FraudCheckService {
    private final FraudCheckHistoryRepository fraudCheckHistoryRepository;

    public FraudCheckResponse isFraudulentCustomer(Long customerId) {
        FraudCheckHistory fraudCheckHistory = FraudCheckHistory.builder()
                .customerId(customerId)
                .isFraudster(false)
                .createdAt(LocalDateTime.now())
                .build();

        fraudCheckHistoryRepository.save(fraudCheckHistory);

        return new FraudCheckResponse(false);
    }
}
