package nl.tudelft.sem.yumyumnow.services;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.yumyumnow.database.OrderRepository;
import nl.tudelft.sem.yumyumnow.database.RatingRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final RatingRepository ratingRepository;

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final CustomerService customerService;

    /**
     * Constructor for the service.
     *
     * @param customerService Instance of customerService.
     * @param ratingRepository Instance of ratingRepository.
     * @param orderService Instance of orderService.
     */

    public AnalyticsService(CustomerService customerService,
                            RatingRepository ratingRepository,
                            OrderService orderService,
                            OrderRepository orderRepository) {
        this.customerService = customerService;
        this.ratingRepository = ratingRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public Optional<Rating> getRatingById(Long ratingId) {
        return this.ratingRepository.findById(ratingId);
    }

    public Rating createNewRating(Rating rating) {
        return this.ratingRepository.save(rating);
    }

    /**
     * Calculate the average price a customer paid for an order.
     *
     * @param customerId The customer's Id.
     * @return Average price a customer paid.
     */
    public Double getCustomerAveragePrice(Long customerId) {
        Customer customer = this.customerService.getCustomer(customerId);
        if (customer != null) {
            List<Order> orderList = customer.getPastOrders();
            int numberOrders = 1;
            Double totalPrice = 0.0;
            if (orderList != null) {
                numberOrders = orderList.size();
                for (Order order : orderList) {
                    if (order == null || order.getPrice() == null) {
                        return null;
                    }
                    totalPrice += order.getPrice();
                }
                return totalPrice / numberOrders;
            }
            return null;
        }
        return null;
    }

    /**
     * Calculate the average number of order's per month for the active months.
     *
     * @param customerId The customer's id.
     * @return The average number of orders per month.
     */
    public Double getOrdersPerMonth(Long customerId) {
        Customer customer = this.customerService.getCustomer(customerId);
        if (customer != null) {
            List<Order> orderList = customer.getPastOrders();
            if (orderList != null) {
                Map<String, Long> orderPerMonth = orderList
                    .stream()
                    .collect(Collectors.groupingBy(order -> order.getTime().getMonth() + "-" + order.getTime().getYear(),
                        Collectors.counting()));
                Long nrOrder = 0L;
                for (String month : orderPerMonth.keySet()) {
                    nrOrder += orderPerMonth.get(month);
                }
                Integer nrMonths = orderPerMonth.keySet().size();
                //System.out.println(nrOrder + " " + nrMonths);
                return (double) nrOrder / nrMonths;
            }
            return null;
        }
        return null;
    }

    /**
     *  Setting a rating to an order.
     *
     * @param orderId The order's id.
     * @param rating The new rating for an order.
     */
    public void setOrderRating(Long orderId, Rating rating) {
        try {
            Order order = this.orderService.getOrderById(orderId);
            if (order != null && (rating != null && rating.getId() != null)) {
                ratingRepository.save(rating);
                order.setRatingId(rating.getId());
                orderRepository.save(order);
            }
        } catch (NoSuchElementException e) {
            return;
        }
    }
}
