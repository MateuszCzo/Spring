package mc.project.online_store.service.util;

import mc.project.online_store.model.ProductCalculationInformation;

public interface PriceCalculator {
    float calculate(Iterable<ProductCalculationInformation> product);
}
