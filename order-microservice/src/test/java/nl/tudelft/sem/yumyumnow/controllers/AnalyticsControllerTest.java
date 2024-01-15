package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.controller.AnalyticsController;
import nl.tudelft.sem.yumyumnow.controller.VendorController;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnalyticsControllerTest {

    private OrderService orderService;
    private AnalyticsService analyticsService;
    private AuthenticationService authenticationService;
    private AnalyticsController analyticsController;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.analyticsService = Mockito.mock(AnalyticsService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.analyticsController = new AnalyticsController(orderService, analyticsService, authenticationService);
    }

    @Test
    public void testGetOrderRating() {
        Order order = new Order();
        order.setOrderId(11L);
        order.setRatingId(10L);

        Rating rating = new Rating();
        rating.setComment("Tuna, no crust.");
        rating.setId(10L);

        Mockito.when(orderService.getOrderById(11L)).thenReturn(order);
        Mockito.when(analyticsService.getRatingById(10L)).thenReturn(Optional.of(rating));

        ResponseEntity<Rating> response = analyticsController.getOrderRating(11L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rating, response.getBody());
    }

    @Test
    public void testGetOrderRatingNotFound() {
        Order order = new Order();
        order.setOrderId(11L);
        order.setRatingId(10L);

        Mockito.when(orderService.getOrderById(11L)).thenReturn(order);
        Mockito.when(analyticsService.getRatingById(10L)).thenReturn(Optional.empty());

        ResponseEntity<Rating> response = analyticsController.getOrderRating(11L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderRatingInternalError() {
        Order order = new Order();
        order.setOrderId(11L);
        order.setRatingId(10L);

        Mockito.when(orderService.getOrderById(11L)).thenReturn(order);
        Mockito.when(analyticsService.getRatingById(10L)).thenThrow(new RuntimeException());

        ResponseEntity<Rating> response = analyticsController.getOrderRating(11L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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

    @Test
    void getPopularDish() {
        Long vendorId = 1L;

        Dish dish = new Dish().name("fries").id(12L);

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getPopularDish(vendorId)).thenReturn(dish);

        ResponseEntity<Dish> response = analyticsController.getPopularDish(vendorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dish, response.getBody());

    }

    @Test
    void getPopularDishUnauthorized() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(false);

        ResponseEntity<Dish> response = analyticsController.getPopularDish(vendorId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void getPopularDishNotFound() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getPopularDish(vendorId)).thenReturn(null);

        ResponseEntity<Dish> response = analyticsController.getPopularDish(vendorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void getPopularDishInternalServerError() {
        Long vendorId = 1L;

        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.getPopularDish(vendorId)).thenThrow(new RuntimeException());

        ResponseEntity<Dish> response = analyticsController.getPopularDish(vendorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    void testGetOrdersPerDay() {
        Long vendorId = 1L;
        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.averageOrdersPerDay(vendorId)).thenReturn(5.0);

        ResponseEntity<Double> response = analyticsController.getOrdersPerDay(vendorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5.0, response.getBody());
    }

    @Test
    void testGetOrdersPerDayNotFound() {
        Long vendorId = 1L;
        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.averageOrdersPerDay(vendorId)).thenReturn(null);

        ResponseEntity<Double> response = analyticsController.getOrdersPerDay(vendorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetOrdersPerDayUnauthorized() {
        Long vendorId = 1L;
        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(false);

        ResponseEntity<Double> response = analyticsController.getOrdersPerDay(vendorId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetOrdersPerDayInternalServerError() {
        Long vendorId = 1L;
        Mockito.when(authenticationService.isVendor(vendorId)).thenReturn(true);
        Mockito.when(analyticsService.averageOrdersPerDay(vendorId)).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<Double> response = analyticsController.getOrdersPerDay(vendorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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

    @Test
    public void getOrdersPerMonthTestUnauthorized() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(false);
        ResponseEntity<Double> response = analyticsController.getCustomerAveragePrice(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getOrdersPerMonthTestError() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getOrdersPerMonth(13L)).thenThrow(RuntimeException.class);
        ResponseEntity<Double> response = analyticsController.getOrdersPerMonth(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void getOrdersPerMonthTestNotFound() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getOrdersPerMonth(13L)).thenReturn(null);
        ResponseEntity<Double> response = analyticsController.getOrdersPerMonth(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersPerMonthTestValid() {
        when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        when(this.analyticsService.getOrdersPerMonth(13L)).thenReturn(12.36);
        ResponseEntity<Double> response = analyticsController.getOrdersPerMonth(13L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.36, response.getBody());
    }






    @Test
    public void setOrderRatingTestError() {
        Rating rating = new Rating();
        when(this.orderService.getOrderById(13L)).thenThrow(NoSuchElementException.class);
        ResponseEntity<Void> response = analyticsController.setOrderRating(13L, rating);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void setOrderRatingTestNotFound1() {
        Rating rating = new Rating();
        when(this.orderService.getOrderById(13L)).thenReturn(null);
        ResponseEntity<Void> response = analyticsController.setOrderRating(13L, rating);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void setOrderRatingTestNotFound2() {
        Order order = new Order();
        when(this.orderService.getOrderById(13L)).thenReturn(order);
        ResponseEntity<Void> response = analyticsController.setOrderRating(13L, null);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void setOrderRatingTestValid() {
        Order order = new Order();
        Rating rating = new Rating();
        rating.setId(2L);
        when(this.orderService.getOrderById(13L)).thenReturn(order);
        ResponseEntity<Void> response = analyticsController.setOrderRating(13L, rating);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.analyticsService, times(1)).setOrderRating(13L, rating);
    }


}
