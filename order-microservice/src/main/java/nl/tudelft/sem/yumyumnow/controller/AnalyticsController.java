package nl.tudelft.sem.yumyumnow.controller;

import nl.tudelft.sem.yumyumnow.api.AnalyticsApi;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private final AnalyticsService analyticsService;
    private final AuthenticationService authenticationService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param analyticsService a service managing analytics
     */
    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, AuthenticationService authenticationService) {
        this.analyticsService = analyticsService;
        this.authenticationService = authenticationService;
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
                Double averagePrice = analyticsService.getAverageVendorPrice(vendorId);

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
                Double averageRating = analyticsService.getAverageVendorRating(vendorId);

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
}
