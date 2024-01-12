package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.yumyumnow.controller.AnalyticsController;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnalyticsControllerTest {

    private AnalyticsController analyticsController;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        this.analyticsService = Mockito.mock(AnalyticsService.class);
        this.analyticsController = new AnalyticsController(analyticsService);
    }

    @Test
    void testGetAveragePrice() {
        Long vendorId = 1L;
        Double average = 43.5;

        Mockito.when(analyticsService.getAverageVendorPrice(vendorId)).thenReturn(average);

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAveragePriceNotFound() {
        Long vendorId = 1L;

        Mockito.when(analyticsService.getAverageVendorPrice(vendorId)).thenReturn(null);

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
