package nl.tudelft.sem.yumyumnow.controller;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.AdminApi;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController implements AdminApi {
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
    public AdminController(OrderService orderService, AuthenticationService authenticationService,
        CompletionFactory orderCompletionService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
        this.orderCompletionService = orderCompletionService;
    }


    /**
     * An admin can remove an orders by its id.
     *
     * @param orderId ID of the order being removed (required)
     * @param adminId ID of admin removing the order (required)
     * @return a Response Entity containing the order created, or an error code
     */
    @Override
    public ResponseEntity<Void> removeOrder(Long orderId, Long adminId) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            boolean deleted = this.orderService.deleteOrder(orderId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    /**
     * An admin can view all orders in the system.
     *
     * @param adminId ID of admin viewing all the orders (required)
     * @return a Response Entity containing the order created, or an error code
     */
    @Override
    public ResponseEntity<List<Order>> getAllOrders(Long adminId) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Order> allOrders = this.orderService.getAllOrders();
            return ResponseEntity.of(Optional.of(allOrders));
        }
    }

    /**
     * Let an admin modify an order.
     *
     * @param orderId  The order to be modified.
     * @param adminId  The id of the admin modifying the order.
     * @param newOrder The new order the old order will be modified to.
     * @return The modified order
     */
    @Override
    public ResponseEntity<Void> updateOrder(Long orderId, Long adminId, Order newOrder) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!this.orderService.existsAtId(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Order modifiedOrder = this.orderService.modifyOrderAdmin(orderId, newOrder);

            if (modifiedOrder == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
