package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.yumyumnow.controller.AnalyticsController;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnalyticsControllerTest {

    private AnalyticsController analyticsController;
    private AnalyticsService analyticsService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        this.analyticsService = Mockito.mock(AnalyticsService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.analyticsController = new AnalyticsController(analyticsService, authenticationService);
    }

    @Test
    void testGetAveragePrice() {
        Long vendorId = 1L;
        Double average = 43.5;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorPrice(vendorId)).thenReturn(average);

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(average, response.getBody());
    }

    @Test
    void testGetAveragePriceNotFound() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorPrice(vendorId)).thenReturn(null);

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAveragePriceUnauthorized() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(false);

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetAveragePriceInternalServerError() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorPrice(vendorId)).thenThrow(new RuntimeException());

        ResponseEntity<Double> response = analyticsController.getAveragePrice(vendorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAverageRating() {
        Long vendorId = 1L;
        Double averageRating = 4.5;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorRating(vendorId)).thenReturn(averageRating);

        ResponseEntity<Double> response = analyticsController.getAverageRating(vendorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(averageRating, response.getBody());
    }

    @Test
    void testGetAverageRatingNotFound() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorRating(vendorId)).thenReturn(null);

        ResponseEntity<Double> response = analyticsController.getAverageRating(vendorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAverageRatingUnauthorized() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(false);

        ResponseEntity<Double> response = analyticsController.getAverageRating(vendorId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetAverageRatingInternalServerError() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getAverageVendorRating(vendorId)).thenThrow(new RuntimeException());

        ResponseEntity<Double> response = analyticsController.getAverageRating(vendorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
