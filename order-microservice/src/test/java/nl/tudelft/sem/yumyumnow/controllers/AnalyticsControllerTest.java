package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.yumyumnow.controller.AnalyticsController;
import nl.tudelft.sem.yumyumnow.controller.VendorController;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import nl.tudelft.sem.yumyumnow.services.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnalyticsControllerTest {
    private AnalyticsService analyticsService;
    private AuthenticationService authenticationService;
    private AnalyticsController analyticsController;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.analyticsService = Mockito.mock(AnalyticsService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.analyticsController = new AnalyticsController(analyticsService, authenticationService);
    }

    @Test
    public void getCustomerAveragePriceTestUnauthorized() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(false);
        ResponseEntity<Double> response = analyticsController.getCustomerAveragePrice(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getCustomerAveragePriceTestError() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getCustomerAveragePrice(13L)).thenThrow(RuntimeException.class);
        ResponseEntity<Double> response = analyticsController.getCustomerAveragePrice(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void getCustomerAveragePriceTestNotFound() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getCustomerAveragePrice(13L)).thenReturn(null);
        ResponseEntity<Double> response = analyticsController.getCustomerAveragePrice(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getCustomerAveragePriceTestValid() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getCustomerAveragePrice(13L)).thenReturn(12.36);
        ResponseEntity<Double> response = analyticsController.getCustomerAveragePrice(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.36, response.getBody());
    }

}
