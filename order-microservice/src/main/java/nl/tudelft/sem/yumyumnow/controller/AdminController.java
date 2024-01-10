package nl.tudelft.sem.yumyumnow.controller;

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

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param orderService           a service managing Order objects
     * @param authenticationService  a service managing authentication
     */
    @Autowired
    public AdminController(OrderService orderService, AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
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
}
