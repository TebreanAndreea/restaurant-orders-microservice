package nl.tudelft.sem.yumyumnow.controller;

import nl.tudelft.sem.yumyumnow.api.AnalyticsApi;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private final AnalyticsService analyticsService;
    private final AuthenticationService authenticationService;

    public AnalyticsController(AnalyticsService analyticsService, AuthenticationService authenticationService) {
        this.analyticsService = analyticsService;
        this.authenticationService = authenticationService;
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
}
