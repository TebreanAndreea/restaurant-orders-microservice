package nl.tudelft.sem.yumyumnow.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.commons.OrderEntity;
import nl.tudelft.sem.yumyumnow.database.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {


    private final OrderRepository orderRepository;

    /**
     * Creates a new Order Service.
     *
     * @param repository the DB instance where the Orders are stored
     */
    @Autowired
    public OrderService(OrderRepository repository) {
        this.orderRepository = repository;
    }


    /**
     * Returns an Order object from the DB by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the Order object found for the given ID
     * @throws NoSuchElementException if there is no Order object for the given ID
     */
    public OrderEntity getOrderById(Long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException("No order exists with id " + orderId));
    }

    /**
     * Creates a new Order object for the given customer and vendor.
     *
     * @param customerId ID of customer creating the order
     * @param vendorId ID of vendor for which the customer is creating the order
     * @return the new Order object, stored in the DB
     */
    public OrderEntity createNewOrder(Long customerId, Long vendorId) {
        OrderEntity order = new OrderEntity();
        order.setCustomerId(customerId);
        order.setVendorId(vendorId);
        return this.orderRepository.save(order);
    }
}
