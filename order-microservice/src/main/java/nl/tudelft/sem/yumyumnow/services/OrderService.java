package nl.tudelft.sem.yumyumnow.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.OrderRepository;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final UserService userService;


    /**
     * Creates a new Order Service.
     *
     * @param repository  the DB instance where the Orders are stored
     * @param userService an instance of the user service
     */
    @Autowired
    public OrderService(OrderRepository repository, UserService userService) {
        this.orderRepository = repository;
        this.userService = userService;
    }


    /**
     * Returns an Order object from the DB by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the Order object found for the given ID
     * @throws NoSuchElementException if there is no Order object for the given ID
     */
    public Order getOrderById(Long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(
            () -> new NoSuchElementException("No order exists with id " + orderId));
    }

    /**
     * Creates a new Order object for the given customer and vendor.
     *
     * @param customerId ID of customer creating the order
     * @param vendorId   ID of vendor for which the customer is creating the order
     * @return the new Order object, stored in the DB
     */
    public Order createNewOrder(Long customerId, Long vendorId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setVendorId(vendorId);
        order.setLocation(userService.getDefaultHomeAddress(customerId));
        return this.orderRepository.save(order);
    }
}
