package nl.tudelft.sem.yumyumnow.services;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.OrderRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final CustomerService userService;


    /**
     * Creates a new Order Service.
     *
     * @param repository  the DB instance where the Orders are stored
     * @param customerService an instance of the user service
     */
    @Autowired
    public OrderService(OrderRepository repository, CustomerService customerService) {
        this.orderRepository = repository;
        this.userService = customerService;
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

    /** Get all orders in the system.
     *
     * @return all orders in the system.
     */
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    /** Get all orders in the system for a customer.
     *
     * @param customerId The id of the customer.
     * @return The list of all orders for the customer.
     */
    public List<Order> getAllOrdersForCustomer(Long customerId) {
        List<Order> allOrders = this.orderRepository.findAll();
        List<Order> allOrdersForCustomer = new ArrayList<>();

        for (Order o : allOrders) {
            if (Objects.equals(o.getCustomerId(), customerId)) {
                allOrdersForCustomer.add(o);
            }
        }

        return allOrdersForCustomer;
    }

    /** Get all orders in the system for a vendor.
     *
     * @param vendorId The id of the vendor.
     * @return The list of all orders for the vendor.
     */
    public List<Order> getAllOrdersForVendor(Long vendorId) {
        List<Order> allOrders = this.orderRepository.findAll();
        List<Order> allOrdersForVendor = new ArrayList<>();

        for (Order o : allOrders) {
            if (Objects.equals(o.getVendorId(), vendorId)) {
                allOrdersForVendor.add(o);
            }
        }

        return allOrdersForVendor;
    }

    /**
     * Get all rating in the system for a vendor's orders.
     *
     * @param vendorId the id of the vendor
     * @return a list with all the ratings' ids
     */
    public List<Long> getAllRatingsForVendor(Long vendorId) {
        List<Order> orders = getAllOrdersForVendor(vendorId);
        List<Long> ratingsIds = new ArrayList<>();

        if (orders != null && !orders.isEmpty()) {
            for (Order o : orders) {
                ratingsIds.add(o.getRatingId());
            }
            return ratingsIds;
        } else {
            return null;
        }
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
        Order order = getOrderById(orderId);
        order.setStatus(status);
        Order saved = this.orderRepository.save(order);
        return saved.getStatus() == status;
    }

    /**
     * Checks if an order belongs to the specified id in the DB.
     *
     * @param orderId the order id to check.
     * @return true if an order is associated to the ID in the DB.
     */
    public boolean existsAtId(Long orderId) {
        return this.orderRepository.existsById(orderId);
    }

    /**
     * Add dish to order.
     *
     * @param orderId The id of the order we want to add dishes to.
     * @param dish The list of dishes that will be added to the order.
     * @return The list of dishes in the order after adding them.
     */
    public List<Dish> addDishToOrder(Long orderId, Dish dish) {
        Optional<Order> modifiedOrderOptional = this.orderRepository.findById(orderId);
        if (modifiedOrderOptional.isPresent()) {
            Order modifiedOrder = modifiedOrderOptional.get();
            List<Dish> allDishes = modifiedOrder.getDishes();
            if (allDishes == null) {
                allDishes = new ArrayList<>();
            }
            allDishes.add(dish);
            modifiedOrder.setDishes(allDishes);
            this.orderRepository.save(modifiedOrder);
            return allDishes;
        }
        return null;
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
        Order order = this.getOrderById(orderId);
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
     * Check if a user is the vendor or the customer of a specific order.
     *
     * @param orderId the id of the order.
     * @param userId the id of the user.
     * @return true if the user is a vendor or customer for the order, false otherwise.
     */
    public boolean isUserAssociatedWithOrder(Long orderId, Long userId) {
        try {
            Order order = getOrderById(orderId);
            return order.getCustomerId().equals(userId) || order.getVendorId().equals(userId);
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Deletes an order by id.
     *
     * @param orderId the id of the order to delete
     * @return A boolean specifying whether the order is deleted or not
     */
    public Boolean deleteOrder(Long orderId) {
        Optional<Order> optionalOrder = this.orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order orderToDelete = optionalOrder.get();
            this.orderRepository.delete(orderToDelete);
            return true;
        } else {
            return false;
        }
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

}
