package nl.tudelft.sem.yumyumnow.controller;

import nl.tudelft.sem.yumyumnow.api.AnalyticsApi;
import nl.tudelft.sem.yumyumnow.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController implements AnalyticsApi {

    private AnalyticsService analyticsService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param analyticsService a service managing analytics
     */
    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public ResponseEntity<Double> getAveragePrice(Long vendorId) {
        Double averagePrice = analyticsService.getAverageVendorPrice(vendorId);

        if (averagePrice != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
