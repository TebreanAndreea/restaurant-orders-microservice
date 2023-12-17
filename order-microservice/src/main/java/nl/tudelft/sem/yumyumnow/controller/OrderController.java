package nl.tudelft.sem.yumyumnow.controller;

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
}
