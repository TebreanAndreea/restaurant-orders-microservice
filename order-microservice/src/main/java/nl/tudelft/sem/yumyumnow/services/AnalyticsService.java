package nl.tudelft.sem.yumyumnow.services;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.RatingRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final RatingRepository ratingRepository;
    private VendorService vendorService;
    private OrderService orderService;

    /**
     * Creates a new AnalyticsService.
     *
     * @param vendorService an instance of the vendor service
     * @param ratingRepository the DB instance where the ratings are stored
     * @param orderService an instance of the order service
     */
    @Autowired
    public AnalyticsService(VendorService vendorService,
                            RatingRepository ratingRepository,
                            OrderService orderService) {
        this.vendorService = vendorService;
        this.ratingRepository = ratingRepository;
        this.orderService = orderService;
    }

    public Optional<Rating> getRatingById(Long ratingId) {
        return this.ratingRepository.findById(ratingId);
    }

    public Rating createNewRating(Rating rating) {
        return this.ratingRepository.save(rating);
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

    /**
     * Calculates the average rating of a vendor's orders.
     *
     * @param vendorId the id of the vendor
     * @return the average rating
     */
    public Double getAverageVendorRating(Long vendorId) {
        List<Long> ratingIds = orderService.getAllRatingsForVendor(vendorId);

        if (ratingIds != null && !ratingIds.isEmpty()) {
            double total = 0;
            for (Long id : ratingIds) {
                total += getRatingById(id).get().getGrade();
            }

            return total / ratingIds.size();
        }

        return null;
    }

}
