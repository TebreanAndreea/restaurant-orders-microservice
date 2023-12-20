package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import nl.tudelft.sem.yumyumnow.controller.VendorController;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VendorControllerTest {

    private DishService dishService;
    private AuthenticationService authenticationService;
    private VendorRepository vendorRepository;
    private VendorController vendorController;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.dishService = Mockito.mock(DishService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.vendorRepository = Mockito.mock(VendorRepository.class);
        this.vendorController = new VendorController(dishService, authenticationService, vendorRepository);
    }

    @Test
    public void validVendorAndDishTest() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        Vendor vendor = new Vendor();
        vendor.setId(100L);
        Mockito.when(this.vendorRepository.findById(100L)).thenReturn(Optional.of(vendor));

        Mockito.when(this.dishService.createNewDish(dish)).thenReturn(dish);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void unauthorizedAccessTest() {
        Dish dish = new Dish();
        dish.setName("Unauthorized Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(false);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void vendorNotFoundTest() {
        Dish dish = new Dish();
        dish.setName("Non-existent Vendor Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);
        Mockito.when(this.vendorRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void dishServiceFailureTest() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        Vendor vendor = new Vendor();
        vendor.setId(100L);
        Mockito.when(this.vendorRepository.findById(100L)).thenReturn(Optional.of(vendor));

        Mockito.when(this.dishService.createNewDish(dish)).thenThrow(RuntimeException.class);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void vendorRepositoryFailureTest() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        Mockito.when(this.vendorRepository.findById(100L)).thenThrow(RuntimeException.class);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void addDishToVendorSuccessTest() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        Vendor vendor = new Vendor();
        vendor.setId(100L);
        Mockito.when(this.vendorRepository.findById(100L)).thenReturn(Optional.of(vendor));

        Mockito.when(this.dishService.createNewDish(dish)).thenReturn(dish);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the vendor repository is called to save the updated vendor
        Mockito.verify(this.vendorRepository, Mockito.times(1)).save(vendor);
    }

    @Test
    public void addDishToVendorFailureTest() {
        Dish dish = new Dish();
        dish.setName("Test Dish");

        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        Vendor vendor = new Vendor();
        vendor.setId(100L);
        Mockito.when(this.vendorRepository.findById(100L)).thenReturn(Optional.of(vendor));

        Mockito.when(this.dishService.createNewDish(dish)).thenReturn(dish);

        // Simulate a failure when saving the vendor after adding a dish
        Mockito.doThrow(RuntimeException.class).when(this.vendorRepository).save(vendor);

        ResponseEntity<Void> response = vendorController.addDishToVendor(100L, dish);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}