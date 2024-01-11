package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.controller.VendorController;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
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
    public void unauthorizedAccessTestToGetDishesNoCustomer() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(false);
        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(true);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(100L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void unauthorizedAccessTestToGetDishes() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(false);
        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(false);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(100L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void unauthorizedAccessTestToGetDishesNoVendor() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(100L)).thenReturn(false);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(100L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetVendorDishNoVendor() {

        Mockito.when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(10L)).thenReturn(true);

        Mockito.when(this.vendorRepository.findById(10L)).thenThrow(RuntimeException.class);
        Mockito.when(this.vendorService.getVendorDishesforCustomer(10L, 13L)).thenReturn(null);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 13L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetVendorDishValid() {
        Mockito.when(this.authenticationService.isCustomer(13L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(10L)).thenReturn(true);
        Customer customer = new Customer();
        customer.setId(13L);
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);

        Mockito.when(this.vendorService.getVendorDishesforCustomer(10L, 13L)).thenReturn(dishes);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 13L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dishes, response.getBody());
    }

    @Test
    public void testGetVendorDishValidEmptyList() {
        Mockito.when(this.authenticationService.isCustomer(12L)).thenReturn(true);
        Mockito.when(this.authenticationService.isVendor(10L)).thenReturn(true);
        List<Dish> dishes = new ArrayList<>();
        Mockito.when(this.vendorService.getVendorDishes(10L)).thenReturn(dishes);

        ResponseEntity<List<Dish>> response = vendorController.getVendorDishes(10L, 12L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        assertEquals(dishes, response.getBody());

    }

    @Test
    public void testGetAllVendors() {
        Vendor v1 = new Vendor();
        v1.setName("Vendor 1");
        v1.setId(1L);
        Vendor v2 = new Vendor();
        v2.setName("Vendor 2");
        v2.setId(2L);
        List<Vendor> vendors = new ArrayList<>();
        vendors.add(v1);
        vendors.add(v2);

        Mockito.when(this.vendorService.getAllVendors()).thenReturn(vendors);
        ResponseEntity<List<Vendor>> response = this.vendorController.getAllVendors(null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals(2L, response.getBody().get(1).getId());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetAllVendorsEmpty() {
        List<Vendor> vendors = new ArrayList<>();

        Mockito.when(this.vendorService.getAllVendors()).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendors(null);

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(0, vendorsReceived.getBody().size());
    }

    @Test
    public void testGetAllVendorsFilter() {
        Vendor v1 = new Vendor();
        v1.setName("Bistro de l'Arte");
        v1.setId(1L);
        Vendor v2 = new Vendor();
        v2.setName("Bistro Aha");
        v2.setId(2L);
        Vendor v3 = new Vendor();
        List<Vendor> vendors = new ArrayList<>();
        vendors.add(v1);
        vendors.add(v2);

        Mockito.when(this.vendorService.findByVendorNameContaining("Bistro")).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendors("Bistro");

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(1L, vendorsReceived.getBody().get(0).getId());
        assertEquals("Bistro de l'Arte", vendorsReceived.getBody().get(0).getName());
        assertEquals(2L, vendorsReceived.getBody().get(1).getId());
        assertEquals("Bistro Aha", vendorsReceived.getBody().get(1).getName());
        assertEquals(2, vendorsReceived.getBody().size());
    }

    @Test
    public void testGetAllVendorsFilterEmpty() {
        List<Vendor> vendors = new ArrayList<>();

        Mockito.when(this.vendorService.findByVendorNameContaining("Bistro")).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendors("Bistro");

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(0, vendorsReceived.getBody().size());
    }

    @Test
    public void testGetVendorsAddressEmptyFilter() {
        Location customer = new Location();
        customer.setLatitude(23.0);
        customer.setLongitude(45.0);

        Vendor v1 = new Vendor();
        v1.setName("Bistro de l'Arte");
        v1.setId(1L);
        v1.setLocation(customer);

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(v1);

        Mockito.when(this.vendorService.findByLocationWithinRadius(customer, "", 1000)).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendorsAddress(customer, "", 1000);

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(1L, vendorsReceived.getBody().get(0).getId());
    }

    @Test
    public void testGetVendorsAddress() {
        Location customer = new Location();
        customer.setLatitude(23.0);
        customer.setLongitude(45.0);

        Vendor v1 = new Vendor();
        v1.setName("Bistro de l'Arte");
        v1.setId(1L);
        v1.setLocation(customer);

        Vendor v2 = new Vendor();
        v2.setName("Bistro Aha");
        v2.setId(2L);
        v2.setLocation(new Location());
        v2.getLocation().setLatitude(23.0);
        v2.getLocation().setLongitude(45.01);

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(v1);
        vendors.add(v2);

        Mockito.when(this.vendorService.findByLocationWithinRadius(customer, "bistro", 2000)).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendorsAddress(customer, "bistro", 2000);

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(1L, vendorsReceived.getBody().get(0).getId());
    }

    @Test
    public void testGetVendorsAddressNullRadius() {
        Location customer = new Location();
        customer.setLatitude(23.0);
        customer.setLongitude(45.0);

        Vendor v1 = new Vendor();
        v1.setName("Bistro de l'Arte");
        v1.setId(1L);
        v1.setLocation(customer);

        Vendor v2 = new Vendor();
        v2.setName("Bistro Aha");
        v2.setId(2L);
        v2.setLocation(new Location());
        v2.getLocation().setLatitude(23.0);
        v2.getLocation().setLongitude(45.01);

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(v1);
        vendors.add(v2);

        Mockito.when(this.vendorService.findByLocationWithinRadius(customer, "bistro", 1000)).thenReturn(vendors);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendorsAddress(customer, "bistro", null);

        assertNotNull(vendorsReceived);
        assertEquals(HttpStatus.OK, vendorsReceived.getStatusCode());
        assertEquals(1L, vendorsReceived.getBody().get(0).getId());
    }

    @Test
    public void testGetVendorsAddressInvalidAddress() {
        Location customer = new Location();
        customer.setLatitude(291.0);
        customer.setLongitude(45.0);

        Mockito.when(this.vendorService.findByLocationWithinRadius(customer, "bistro", 1000)).thenReturn(null);
        ResponseEntity<List<Vendor>> vendorsReceived = this.vendorController.getAllVendorsAddress(customer, "bistro", 1000);

        assertNotNull(vendorsReceived);
        assertNull(vendorsReceived.getBody());
        assertEquals(HttpStatus.NOT_FOUND, vendorsReceived.getStatusCode());
    }

}