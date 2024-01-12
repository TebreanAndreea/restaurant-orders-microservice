package nl.tudelft.sem.yumyumnow.controller;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.AnalyticsApi;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private final OrderService orderService;
    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(OrderService orderService, AnalyticsService analyticsService) {
        this.orderService = orderService;
        this.analyticsService = analyticsService;
    }

    @Override
    public ResponseEntity<Rating> getOrderRating(Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            Optional<Rating> ratingOptional = analyticsService.getRatingById(order.getRatingId());
            if (ratingOptional.isPresent()) {
                Rating rating = ratingOptional.get();
                return ResponseEntity.ok(rating);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
