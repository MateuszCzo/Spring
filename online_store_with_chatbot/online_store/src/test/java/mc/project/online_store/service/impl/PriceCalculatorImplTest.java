package mc.project.online_store.service.impl;

import mc.project.online_store.model.ProductCalculationInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorImplTest {
    @InjectMocks
    private PriceCalculatorImpl priceCalculator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenProductCalculationInformationIterable_whenCalculate_thenReturnsFloat() {
        ProductCalculationInformation product = new ProductCalculationInformation() {
            @Override
            public long getProductId() {
                return 1;
            }

            @Override
            public int getQuantity() {
                return 2;
            }

            @Override
            public float getPrice() {
                return 3;
            }
        };
        List<ProductCalculationInformation> productList = List.of(product);

        float serviceResponse = priceCalculator.calculate(productList);

        assertEquals(product.getPrice() * product.getQuantity(), serviceResponse);
    }
}