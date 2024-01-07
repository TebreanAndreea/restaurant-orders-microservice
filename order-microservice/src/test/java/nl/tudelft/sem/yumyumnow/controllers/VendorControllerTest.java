package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.controller.VendorController;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import nl.tudelft.sem.yumyumnow.services.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VendorControllerTest {

    private DishService dishService;
    private VendorService vendorService;
    private AuthenticationService authenticationService;
    private VendorRepository vendorRepository;
    private VendorController vendorController;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.dishService = Mockito.mock(DishService.class);
        this.vendorService = Mockito.mock(VendorService.class);
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.vendorRepository = Mockito.mock(VendorRepository.class);
        this.vendorController = new VendorController(dishService, vendorService, authenticationService, vendorRepository);
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

    @Test
    public void unauthorizedAccessTestToGetDishes() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(false);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(100L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetVendorDishNoVendor() {

        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(true);


        Mockito.when(this.vendorRepository.findById(10L)).thenThrow(RuntimeException.class);
        Mockito.when(this.vendorService.getVendorDishes(10L)).thenReturn(null);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetVendorDishValid() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(true);
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);

        Mockito.when(this.vendorService.getVendorDishes(10L)).thenReturn(dishes);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dishes, response.getBody());
    }

    @Test
    public void testGetVendorDishValidEmptyList() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(true);
        List<Dish> dishes = new ArrayList<>();
        Mockito.when(this.vendorService.getVendorDishes(10L)).thenReturn(dishes);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        assertEquals(dishes, response.getBody());

    }

}