package nl.tudelft.sem.yumyumnow.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.OrderRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdatesOrderService {
    private final OrderRepository orderRepository;
    private final CustomerService userService;
    private final OrderService orderService;


    /**
     * Creates a new Order Service.
     *
     * @param repository  the DB instance where the Orders are stored
     * @param customerService an instance of the user service
     */
    @Autowired
    public UpdatesOrderService(OrderRepository repository, CustomerService customerService, OrderService orderService) {
        this.orderRepository = repository;
        this.userService = customerService;
        this.orderService = orderService;
    }

    /**
     * Let an admin modify an order.
     *
     * @param orderId The id of the order that will be modified.
     * @param newOrder The new order the old order will be modified to.
     * @return The modified order.
     */
    public Order modifyOrderAdmin(Long orderId, Order newOrder) {
        Optional<Order> modifiedOrderOptional = this.orderRepository.findById(orderId);

        if (modifiedOrderOptional.isPresent()) {
            Order modifiedOrder = newOrder;
            modifiedOrder.setOrderId(orderId);
            this.orderRepository.save(modifiedOrder);
            return modifiedOrder;
        }
        return null;
    }

    /**
     * Updates the status of an order, and saves the changes to the DB.
     *
     * @param orderId the order to modify.
     * @param status the new order status.
     * @return true if the order was modified successfully.
     */
    public boolean setOrderStatus(Long orderId, Order.StatusEnum status) {
        Order order = this.orderService.getOrderById(orderId);
        order.setStatus(status);
        Order saved = this.orderRepository.save(order);
        return saved.getStatus() == status;
    }

    /**
     * Updates an order by ID and saves it to the DB.
     * If a parameter is null, the corresponding order attribute will not be updated.
     *
     * @param orderId the ID of the order to modify
     * @param dishes the new list of dishes
     * @param location the new delivery location
     * @param status the new order status
     * @param time the new delivery time
     * @return true if the order was successfully updated
     */
    public boolean updateOrder(Long orderId, List<Dish> dishes, Location location,
                               Order.StatusEnum status, OffsetDateTime time) {
        Order order = this.orderService.getOrderById(orderId);
        if (dishes != null) {
            order.setDishes(dishes);
        }
        if (location != null) {
            order.setLocation(location);
        }
        if (status != null) {
            order.setStatus(status);
        }
        if (time != null) {
            order.setTime(time);
        }
        order.dishes(dishes).location(location).status(status).time(time);
        Order saved = this.orderRepository.save(order);
        return Objects.equals(saved.getOrderId(), orderId)
            && (location == null || Objects.equals(saved.getLocation(), location))
            && (status == null || Objects.equals(saved.getStatus(), status))
            && (time == null || Objects.equals(saved.getTime(), time))
            && (dishes == null || Objects.equals(dishes, saved.getDishes()));
    }

    /**
     * Modifies an existing order.
     *
     * @param order the modified order
     * @return the updated order, if applicable
     */
    public Optional<Order> modifyOrderRequirements(Order order) {
        Long orderId = order.getOrderId();
        boolean exists = orderRepository.existsById(orderId);

        if (exists) {
            Order saved = orderRepository.save(order);
            return Optional.of(saved);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Removes a dish from an order.
     *
     * @param orderId the order that is modified
     * @param dish the dish to remove
     * @return true if the dish was successfully removed, or if it wasn't present at the beginning.
     */
    public boolean removeDishFromOrder(Long orderId, Dish dish) {
        Order order = this.orderService.getOrderById(orderId);
        order.getDishes().removeIf(x -> Objects.equals(x.getId(), dish.getId()));
        Order saved = this.orderRepository.save(order);
        return saved.getDishes().stream().noneMatch(x -> Objects.equals(x.getId(), dish.getId()));
    }
}
