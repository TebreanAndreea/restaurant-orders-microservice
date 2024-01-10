package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.controller.AdminController;
import nl.tudelft.sem.yumyumnow.controller.OrderController;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.OrderService;
import nl.tudelft.sem.yumyumnow.services.completion.CompletionFactory;
import nl.tudelft.sem.yumyumnow.services.completion.OrderCompletionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AdminControllerTest {

    private OrderService orderService;
    private AuthenticationService authenticationService;
    private AdminController adminController;


    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.orderService = Mockito.mock(OrderService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.adminController = new AdminController(orderService, authenticationService);
    }

    /**
     * Tests the removeOrder method.
     */
    @Test
    public void removeOrder() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * Tests the removeOrder method when the admin is unauthorized.
     */
    @Test
    public void removeOrderNotAuthorized() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(false);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(true);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    /**
     * Tests the removeOrder method when the order is not found.
     */
    @Test
    public void removeOrderNotFound() {
        Mockito.when(this.authenticationService.isAdmin(100L)).thenReturn(true);
        Mockito.when(this.orderService.deleteOrder(Mockito.anyLong())).thenReturn(false);

        ResponseEntity<Void> statusCode = adminController.removeOrder(2L, 100L);
        assertEquals(statusCode, new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
