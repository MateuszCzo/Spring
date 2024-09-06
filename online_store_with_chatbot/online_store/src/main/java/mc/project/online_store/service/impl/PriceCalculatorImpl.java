package mc.project.online_store.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.model.ProductCalculationInformation;
import mc.project.online_store.service.util.PriceCalculator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceCalculatorImpl implements PriceCalculator {

    @Override
    public float calculate(Iterable<ProductCalculationInformation> products) {
        float total = 0;

        for (ProductCalculationInformation product : products) {
            total += product.getPrice() * product.getQuantity();
        }

        return total;
    }
}
