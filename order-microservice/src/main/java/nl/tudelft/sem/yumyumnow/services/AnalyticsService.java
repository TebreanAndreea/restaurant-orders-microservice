package nl.tudelft.sem.yumyumnow.services;

import java.util.List;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private VendorService vendorService;

    @Autowired
    public AnalyticsService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    /**
     * Calculates the average price of a vendor's dishes.
     *
     * @param vendorId the id of the vendor.
     * @return the average price (double)
     */
    public Double getAverageVendorPrice(Long vendorId) {
        List<Dish> vendorDishes = vendorService.getVendorDishes(vendorId);

        if (vendorDishes != null && !vendorDishes.isEmpty()) {
            double total = 0;
            for (Dish dish : vendorDishes) {
                total += dish.getPrice();
            }
            return total / vendorDishes.size();
        }

        return null;
    }
}
