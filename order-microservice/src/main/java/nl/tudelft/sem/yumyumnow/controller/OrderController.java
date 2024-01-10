package nl.tudelft.sem.yumyumnow.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import nl.tudelft.sem.yumyumnow.api.OrderApi;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import nl.tudelft.sem.yumyumnow.services.completion.OrderCompletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderApi {

    private final OrderService orderService;
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
    public OrderController(OrderService orderService, AuthenticationService authenticationService,
                           CompletionFactory orderCompletionService) {
        this.orderService = orderService;
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
     * A customer can view all orders in the system.
     *
     * @param customerId ID of customer viewing all the orders (required)
     * @return a Response Entity containing the order created, or an error code
     */
    //@Override
    public ResponseEntity<List<Order>> getAllOrdersCustomer(Long customerId) {
        if (!this.authenticationService.isCustomer(customerId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Order> allOrders = this.orderService.getAllOrdersForCustomer(customerId);
            return ResponseEntity.of(Optional.of(allOrders));
        }
    }

    /**
     * A vendor can view all orders in the system.
     *
     * @param vendorId ID of vendor viewing all the orders (required)
     * @return a Response Entity containing the order created, or an error code
     */
    //@Override
    public ResponseEntity<List<Order>> getAllOrdersVendor(Long vendorId) {
        if (!this.authenticationService.isVendor(vendorId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Order> allOrders = this.orderService.getAllOrdersForVendor(vendorId);
            return ResponseEntity.of(Optional.of(allOrders));
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
        try {
            if (!this.authenticationService.isCustomer(userId)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else if (!this.orderService.existsAtId(orderId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Order order = this.orderService.getOrderById(orderId);
                IntegrationService integration = this.authenticationService.getIntegrationService();
                OrderCompletionHandler firstHandler = this.orderCompletionService
                        .createCompletionResponsibilityChain(integration);
                Order.StatusEnum status = firstHandler.handleOrderCompletion(order);
                boolean saved = this.orderService.setOrderStatus(orderId, status);
                if (!saved) {
                    throw new RuntimeException("Save operation failed");
                }
                return ResponseEntity.ok(status.getValue());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add multiple dishes to order.
     *
     * @param orderId     The id of the order we want to add dishes to.
     * @param customerId  The id of the customer.
     * @param dishesToAdd The list of dishes that will be added to the order
     * @return The list of all dishes of the order.
     */
    public ResponseEntity<List<Dish>> addDishesToOrder(Long orderId, Long customerId, List<Dish> dishesToAdd) {
        if (!this.authenticationService.isCustomer(customerId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (dishesToAdd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Dish> allDishesFromOrder = this.orderService.addDishesToOrder(orderId, dishesToAdd);

            if (allDishesFromOrder == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.of(Optional.of(allDishesFromOrder));
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
            boolean updated = this.orderService.updateOrder(orderId, dishes, location, orderStatus, time);
            return updated ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
