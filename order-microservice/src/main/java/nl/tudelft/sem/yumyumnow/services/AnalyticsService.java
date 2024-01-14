package nl.tudelft.sem.yumyumnow.services;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final CustomerService customerService;

    public AnalyticsService(CustomerService customerService) {
        this.customerService = customerService;
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
}
