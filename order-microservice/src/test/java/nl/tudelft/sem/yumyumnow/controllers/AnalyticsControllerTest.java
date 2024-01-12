package nl.tudelft.sem.yumyumnow.controllers;

import nl.tudelft.sem.yumyumnow.controller.AnalyticsController;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyticsControllerTest {

    private AnalyticsService analyticsService;
    private OrderService orderService;
    private AnalyticsController analyticsController;

    @BeforeEach
    public void setup() {
        this.analyticsService = Mockito.mock(AnalyticsService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.analyticsController = new AnalyticsController(orderService, analyticsService);
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
}
