package nl.tudelft.sem.yumyumnow.controller;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.OrderApi;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderApi {

    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param orderService          a service managing Order objects
     * @param authenticationService a service managing authentication
     */
    @Autowired
    public OrderController(OrderService orderService, AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
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
     * An admin can view all orders in the system.
     *
     * @param adminId ID of admin viewing all the orders (required)
     * @return a Response Entity containing the order created, or an error code
     */
    //@Override
    public ResponseEntity<List<Order>> getAllOrdersAdmin(Long adminId) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Order> allOrders = this.orderService.getAllOrders();
            return ResponseEntity.of(Optional.of(allOrders));
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
     * Let an admin modify an order.
     *
     * @param orderId The order to be modified.
     * @param adminId The id of the admin modifying the order.
     * @param newOrder The new order the old order will be modified to.
     * @return The modified order
     */
    public ResponseEntity<Order> modifyOrderAdmin(Long orderId, Long adminId, Order newOrder) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Order modifiedOrder = this.orderService.modifyOrderAdmin(orderId, newOrder);

            if (modifiedOrder == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.of(Optional.of(modifiedOrder));
        }
    }
}
