package nl.tudelft.sem.yumyumnow.controller;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.AnalyticsApi;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AnalyticsVendorService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private final OrderService orderService;
    private final AnalyticsService analyticsService;
    private final AnalyticsVendorService analyticsVendorService;
    private final AuthenticationService authenticationService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param orderService a service managing analytics
     * @param analyticsService a service managing analytics
     * @param authenticationService a service managing authentication
     */
    @Autowired
    public AnalyticsController(OrderService orderService, AnalyticsService analyticsService,
                               AuthenticationService authenticationService, AnalyticsVendorService analyticsVendorService) {
        this.orderService = orderService;
        this.analyticsService = analyticsService;
        this.authenticationService = authenticationService;
        this.analyticsVendorService = analyticsVendorService;
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
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a vendor's average price.
     *
     * @param vendorId ID of the vendor (required)
     * @return a Response Entity containing the average price, or an error code
     */
    @Override
    public ResponseEntity<Double> getAveragePrice(Long vendorId) {
        if (this.authenticationService.isVendor(vendorId)) {
            try {
                Double averagePrice = analyticsVendorService.getAverageVendorPrice(vendorId);

                if (averagePrice != null) {
                    return ResponseEntity.ok(averagePrice);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Gets a vendor's average rating.
     *
     * @param vendorId ID of the vendor (required)
     * @return a Response Entity containing the average rating, or an error code
     */
    @Override
    public ResponseEntity<Double> getAverageRating(Long vendorId) {
        if (this.authenticationService.isVendor(vendorId)) {
            try {
                Double averageRating = analyticsVendorService.getAverageVendorRating(vendorId);

                if (averageRating != null) {
                    return ResponseEntity.ok(averageRating);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Gets a vendor's most popular dish.
     *
     * @param vendorId ID of the vendor (required)
     * @return a Response Entity containing the popular dish, or an error code
     */
    @Override
    public ResponseEntity<Dish> getPopularDish(Long vendorId) {
        if (authenticationService.isVendor(vendorId)) {
            try {
                Dish popular = analyticsVendorService.getPopularDish(vendorId);

                if (popular == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return ResponseEntity.ok(popular);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Gets an average of orders per day for a vendor.
     *
     * @param vendorId ID of vendor (required)
     * @return a Response Entity containing the average orders, or an error code
     */
    @Override
    public ResponseEntity<Double> getOrdersPerDay(Long vendorId) {
        if (authenticationService.isVendor(vendorId)) {
            try {
                Double average = analyticsVendorService.averageOrdersPerDay(vendorId);

                if (average != null) {
                    return ResponseEntity.ok(average);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Double> getCustomerAveragePrice(Long customerId) {
        if (this.authenticationService.isCustomer(customerId)) {
            try {
                Double averagePrice = this.analyticsService.getCustomerAveragePrice(customerId);
                if (averagePrice != null) {
                    return ResponseEntity.ok(averagePrice);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    public ResponseEntity<Double> getOrdersPerMonth(Long customerId) {
        if (this.authenticationService.isCustomer(customerId)) {
            try {
                Double averageNrOrder = this.analyticsService.getOrdersPerMonth(customerId);
                if (averageNrOrder != null) {
                    return ResponseEntity.ok(averageNrOrder);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> setOrderRating(Long orderId, Rating rating) {
        try {
            Order order = this.orderService.getOrderById(orderId);
            if (order != null && rating != null) {
                this.analyticsService.setOrderRating(orderId, rating);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
