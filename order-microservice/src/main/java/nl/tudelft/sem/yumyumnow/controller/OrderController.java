package nl.tudelft.sem.yumyumnow.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.Valid;
import nl.tudelft.sem.yumyumnow.api.OrderApi;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.UpdatesOrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import nl.tudelft.sem.yumyumnow.services.completion.OrderCompletionHandler;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderApi {

    private final OrderService orderService;
    private final UpdatesOrderService updatesOrderService;
    private final AuthenticationService authenticationService;
    private final CompletionFactory orderCompletionService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param orderService           a service managing Order objects
     * @param authenticationService  a service managing authentication
     * @param orderCompletionService a service creating the handlers for an order completion
     */
    @Autowired
    public OrderController(OrderService orderService,
                           UpdatesOrderService updatesOrderService,
                           AuthenticationService authenticationService,
                           CompletionFactory orderCompletionService) {
        this.orderService = orderService;
        this.updatesOrderService = updatesOrderService;
        this.authenticationService = authenticationService;
        this.orderCompletionService = orderCompletionService;
    }

    /**
     * Creates a new order by the given customer at the given vendor.
     *
     * @param customerId ID of customer creating the order (required)
     * @param vendorId   ID of vendor for which the customer is creating the order (required)
     * @return a Response Entity containing the order created, or an error code
     */
    @Override
    public ResponseEntity<Order> createOrder(Long customerId, Long vendorId) {
        if (!this.authenticationService.isCustomer(customerId)
                || !this.authenticationService.isVendor(vendorId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Order createdOrder = this.orderService.createNewOrder(customerId, vendorId);
            return ResponseEntity.of(Optional.of(createdOrder));
        }
    }

    /**
     * Get a list of all orders for a customer.
     *
     * @param customerId ID of customer viewing all the orders (required)
     * @return a Response Entity containing the list of orders, or error code
     */
    @Override
    public ResponseEntity<List<Order>> getListOfOrdersForCustomers(Long customerId) {
        if (!this.authenticationService.isCustomer(customerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            List<Order> allOrders = this.orderService.getAllOrdersForCustomer(customerId);
            return ResponseEntity.ok(allOrders);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A vendor can view all orders in the system.
     *
     * @param vendorId ID of vendor viewing all the orders (required)
     * @return a Response Entity containing the order created, or an error code
     */
    @Override
    public ResponseEntity<List<Order>> getListOfOrdersForVendor(Long vendorId) {
        if (!this.authenticationService.isVendor(vendorId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            List<Order> allOrders = this.orderService.getAllOrdersForVendor(vendorId);
            return ResponseEntity.ok(allOrders);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A customer can complete an order, that is triggering the payment process, and the order is then sent for
     * preparation and delivery.
     *
     * @param orderId ID of the order that is completed (required)
     * @param userId  ID of user who made the order (required)
     * @return the order status.
     */
    @Override
    public ResponseEntity<String> completeOrder(Long orderId, Long userId) {
        if (!this.authenticationService.isCustomer(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!this.orderService.existsAtId(orderId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Order order = this.orderService.getOrderById(orderId);
            IntegrationService integration = this.authenticationService.getIntegrationService();
            OrderCompletionHandler firstHandler = this.orderCompletionService
                .createCompletionResponsibilityChain(integration);
            Order.StatusEnum status = firstHandler.handleOrderCompletion(order);
            boolean saved = this.updatesOrderService.setOrderStatus(orderId, status);
            if (!saved) {
                throw new RuntimeException("Save operation failed");
            }
            return ResponseEntity.ok(status.getValue());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add one dishes to order.
     *
     * @param orderId     The id of the order we want to add dishes to.
     * @param customerId  The id of the customer.
     * @param dish The dish we want to add to the order.
     * @return The list of all dishes of the order.
     */
    @Override
    public ResponseEntity<Void> addDishToOrder(Long orderId, Long customerId, Dish dish) {
        if (!this.authenticationService.isCustomer(customerId) || !this.orderService.existsAtId(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!this.orderService.getOrderById(orderId).getCustomerId().equals(customerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (dish == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {

            List<Dish> savedDish = this.orderService.addDishToOrder(orderId, dish);
            if (savedDish == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an order and saves it to the DB.
     * If a parameter is null, the corresponding order attribute will not be updated.
     *
     * @param orderId  ID of order that needs to be updated (required)
     * @param userId   ID of user that wants to update (required)
     * @param dishes   The list of dishes that needs to be updated (required)
     * @param location The location that needs to be updated (required)
     * @param status   The status of the order that needs to be updated (required)
     * @param time     The time of the order that needs to be updated (required)
     * @return an empty response entity with an appropriate status code.
     */
    @Override
    public ResponseEntity<Void> modifyOrder(Long orderId, Long userId, List<@Valid Dish> dishes, Location location,
                                            String status, OffsetDateTime time) {
        if (!this.orderService.existsAtId(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order = this.orderService.getOrderById(orderId);
        if (!(this.authenticationService.isAdmin(userId) || order.getCustomerId().equals(userId))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Order.StatusEnum orderStatus;
        try {
            orderStatus = (status == null || status.isEmpty()) ? null : Order.StatusEnum.fromValue(status);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            boolean updated = this.updatesOrderService.updateOrder(orderId, dishes, location, orderStatus, time);
            return updated ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves an order by its ID for a specified user.
     *
     * @param orderId ID of the Order (required).
     * @param userId ID of user who made the order (required).
     * @return the order requested by the user.
     */
    @Override
    public ResponseEntity<Order> getOrder(Long orderId, Long userId) {
        if (!this.orderService.isUserAssociatedWithOrder(orderId, userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Order order = this.orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Remove an orders by its id.
     *
     * @param orderId ID of the order being removed (required)
     * @param userId ID of user removing the order (required)
     * @return a Response Entity containing the order created, or an error code
     */
    @Override
    public ResponseEntity<Void> deleteOrder(Long orderId, Long userId) {
        if (!this.authenticationService.isAdmin(userId) && !this.authenticationService.isCustomer(userId)
                && !this.authenticationService.isVendor(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            boolean deleted = this.orderService.deleteOrder(orderId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Removes a dish from an order, and saves the changes to the DB.
     *
     * @param orderId ID of the order from which the dish is removed (required)
     * @param userId ID of user who made the order (required)
     * @param dish The dish to remove (optional)
     * @return only an HTTP status
     */
    @Override
    public ResponseEntity<Void> removeDishFromOrder(Long orderId, Long userId, Dish dish) {
        if (!this.orderService.existsAtId(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!this.orderService.isUserAssociatedWithOrder(orderId, userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            boolean status = this.updatesOrderService.removeDishFromOrder(orderId, dish);
            if (!status) {
                throw new RuntimeException();
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> setOrderRequirements(Long orderId, Long userId, String body) {
        try {
            if (authenticationService.isCustomer(userId) && orderService.isUserAssociatedWithOrder(orderId, userId)) {
                Order order = orderService.getOrderById(orderId);
                order.setSpecialRequirenments(body);
                Optional<Order> updatedOrder = updatesOrderService.modifyOrderRequirements(order);

                if (updatedOrder.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId ID of order that needs to be handled (required)
     * @param userId ID of user who made the order (required)
     * @param body Update an order&#39;s status (optional)
     * @return only an HTTP status
     */
    @Override
    public ResponseEntity<Void> setOrderStatus(Long orderId, Long userId, String body) {
        if (this.authenticationService.isVendor(userId)) {
            try {
                Order.StatusEnum newStatus = Order.StatusEnum.fromValue(body);

                boolean saved = this.updatesOrderService.setOrderStatus(orderId, newStatus);

                if (saved) {
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    throw new RuntimeException("Save operation failed");
                }
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Returns the status of an order.
     *
     * @param orderId ID of order of which to retrieve status from
     * @param userId ID of user who made the order
     * @return the current status of the order
     */
    @Override
    public ResponseEntity<String> getOrderStatus(Long orderId, Long userId) {
        try {
            if (authenticationService.isCustomer(userId) && orderService.isUserAssociatedWithOrder(orderId, userId)) {
                Order order = orderService.getOrderById(orderId);

                if (order.getStatus() != null) {
                    String status = order.getStatus().getValue();
                    return ResponseEntity.ok(status);
                }

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
