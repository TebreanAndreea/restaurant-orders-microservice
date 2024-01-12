package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnalyticsServiceTest {

    private AnalyticsService analyticsService;
    private VendorService vendorService;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.vendorService = Mockito.mock(VendorService.class);
        this.analyticsService = new AnalyticsService(vendorService);
    }

    @Test
    void testGetAverageVendorPrice() {
        Long vendorId = 1L;
        Dish dish1 = new Dish().id(2L).price(23.4);
        Dish dish2 = new Dish().id(3L).price(2.3);
        Dish dish3 = new Dish().id(4L).price(103.2);
        List<Dish> dishes = Arrays.asList(dish1, dish2, dish3);
        Double average = (23.4 + 2.3 + 103.2) / 3;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(average, analyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceOneDish() {
        Long vendorId = 1L;
        Dish dish = new Dish().id(2L).price(23.4);
        List<Dish> dishes = Arrays.asList(dish);

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(23.4, analyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceNoDishes() {
        Long vendorId = 1L;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, analyticsService.getAverageVendorPrice(vendorId));
    }

}
