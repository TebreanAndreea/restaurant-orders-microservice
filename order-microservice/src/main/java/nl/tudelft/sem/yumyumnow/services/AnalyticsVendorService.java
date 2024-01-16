package nl.tudelft.sem.yumyumnow.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.RatingRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsVendorService {
    private final RatingRepository ratingRepository;

    private VendorService vendorService;
    private OrderService orderService;

    /**
     * Constructor for the service.
     *
     * @param vendorService Instance of vendorService.
     * @param ratingRepository Instance of ratingRepository.
     * @param orderService Instance of orderService.
     */

    public AnalyticsVendorService(
                            VendorService vendorService,
                            RatingRepository ratingRepository,
                            OrderService orderService) {
        this.vendorService = vendorService;
        this.ratingRepository = ratingRepository;
        this.orderService = orderService;
    }

    public Optional<Rating> getRatingById(Long ratingId) {
        return this.ratingRepository.findById(ratingId);
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

    /**
     * Get the popular dish for a specific vendor.
     *
     * @param vendorId the id of the vendor
     * @return the popular dish if it exists, null otherwise
     */
    public Dish getPopularDish(Long vendorId) {
        List<Order> vendorOrders = orderService.getAllOrdersForVendor(vendorId);

        if (vendorOrders != null && !vendorOrders.isEmpty()) {
            Map<Dish, Integer> dishCount = new HashMap<>();

            for (Order order : vendorOrders) {
                List<Dish> orderDishes = order.getDishes();
                for (Dish dish : orderDishes) {
                    dishCount.put(dish, dishCount.getOrDefault(dish, 0) + 1);
                }
            }

            Dish popular = null;
            int maxi = 0;

            for (Map.Entry<Dish, Integer> entry : dishCount.entrySet()) {
                if (entry.getValue() > maxi) {
                    maxi = entry.getValue();
                    popular = entry.getKey();
                }
            }

            return popular;
        }

        return null;
    }

    /**
     * Calculate an average of orders per day.
     *
     * @param vendorId the id of the vendor
     * @return the average or null
     */
    public Double averageOrdersPerDay(Long vendorId) {
        List<Order> orders = orderService.getAllOrdersForVendor(vendorId);

        if (orders != null && !orders.isEmpty()) {
            Map<LocalDate, Long> ordersPerDay = new HashMap<>();

            for (Order order : orders) {
                LocalDate date = order.getTime().toLocalDate();
                ordersPerDay.put(date, ordersPerDay.getOrDefault(date, 0L) + 1);
            }

            long totalOrders = 0;
            for (Long order : ordersPerDay.values()) {
                totalOrders += order;
            }

            return  totalOrders / (double) ordersPerDay.size();
        } else {
            return null;
        }
    }

}
