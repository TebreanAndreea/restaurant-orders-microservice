package nl.tudelft.sem.yumyumnow.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.AdminApi;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.CustomerService;
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
    private final CustomerService customerService;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param orderService           a service managing Order objects
     * @param authenticationService  a service managing authentication
     * @param orderCompletionService a service creating the handlers for an order completion
     */
    @Autowired
    public AdminController(OrderService orderService, AuthenticationService authenticationService,
                           CompletionFactory orderCompletionService, CustomerService customerService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
        this.orderCompletionService = orderCompletionService;
        this.customerService = customerService;
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
        }
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
     * An admin can view any order in the system.
     *
     * @param orderId ID of order to be retrieved
     * @param adminId ID of admin viewing the order
     * @return the order to be viewed
     */
    @Override
    public ResponseEntity<Order> getOrderAdmin(Long orderId, Long adminId) {
        try {
            if (authenticationService.isAdmin(adminId)) {
                Order order = orderService.getOrderById(orderId);
                return ResponseEntity.ok(order);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        Order modifiedOrder = this.orderService.modifyOrderAdmin(orderId, newOrder);

        if (modifiedOrder == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Order>> getListOfOrdersForVendorForClient(Long vendorId, Long customerId, Long adminId) {
        if (!this.authenticationService.isAdmin(adminId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!this.authenticationService.isVendor(vendorId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!this.authenticationService.isCustomer(customerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            Customer customer = this.customerService.getCustomer(customerId);
            if (customer != null) {
                List<Order> orders = customer.getPastOrders();
                List<Order> orderFromVendor = new ArrayList<>();
                if (orders == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                for (Order order : orders) {
                    if (Objects.equals(order.getVendorId(), vendorId)) {
                        orderFromVendor.add(order);
                    }
                }
                return ResponseEntity.of(Optional.of(orderFromVendor));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
