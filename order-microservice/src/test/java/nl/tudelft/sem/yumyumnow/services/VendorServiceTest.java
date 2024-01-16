package nl.tudelft.sem.yumyumnow.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestVendorRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendorServiceTest {
    private TestVendorRepository vendorRepository;
    private VendorService vendorService;
    private OrderService orderService;
    private CustomerService customerService;

    /**
     * Setup for the tests.
     *
     */
    @BeforeEach
    public void setup() {
        this.vendorRepository = new TestVendorRepository();
        this.orderService = mock(OrderService.class);
        this.customerService = mock(CustomerService.class);
        this.vendorService = new VendorService(this.vendorRepository, customerService, orderService);
    }

    @Test
    public void testEmpty() {
        Long id = 112L;
        assertThrows(NoSuchElementException.class, () -> this.vendorService.getVendorById(id),
            "No vendor exists with id 112");

        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("findById", this.vendorRepository.getMethodCalls().get(0));
    }

    @Test
    public void testGetVendorById() {
        Vendor vendor = new Vendor();
        vendor.setName("Vendor1");
        this.vendorRepository.save(vendor);

        Vendor retriveVendor = this.vendorService.getVendorById(0L);
        assertEquals(vendor.getName(), retriveVendor.getName());
    }

    @Test
    public void testGetVendorDishNoVendor() {
        assertNull(this.vendorService.getVendorDishes(112L));
    }

    @Test
    public void testGetVendorDishes() {
        Long id = 112L;
        Vendor vendor = new Vendor();
        vendor.setId(id);
        Dish dish1 = new Dish();
        Dish dish2 = new Dish();
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        vendor.setName("Vendor1");
        vendor.setDishes(dishes);
        this.vendorRepository.save(vendor);

        List<Dish> dishesRetrive = this.vendorService.getVendorDishes(0L);
        assertEquals(dishes.size(), dishesRetrive.size());
        assertEquals(dishes.get(0), dishesRetrive.get(0));
        assertEquals(dishes.get(1), dishesRetrive.get(1));
    }

    @Test
    public void testGetDishesToPrepareNullOrders() {
        when(this.orderService.getAllOrdersForVendor(1L)).thenReturn(null);
        Vendor vendor = new Vendor();
        Long id = this.vendorRepository.count();
        vendor.setId(id);
        this.vendorRepository.save(vendor);
        assertNull(this.vendorService.getDishesToPrepare(1L, id));
    }

    @Test
    public void testGetDishesToPrepareNoOrderFound() {
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        Dish dish3 = new Dish();
        dish3.setId(3L);
        List<Dish> dishes1 = new ArrayList<>();
        dishes1.add(dish1);
        dishes1.add(dish2);
        List<Dish> dishes2 = new ArrayList<>();
        dishes2.add(dish3);
        Order order1 = new Order();
        order1.setOrderId(12L);
        order1.setDishes(dishes1);
        Order order2 = new Order();
        order2.setOrderId(13L);
        order2.setDishes(dishes2);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Vendor vendor = new Vendor();
        Long id = this.vendorRepository.count();
        vendor.setId(id);
        this.vendorRepository.save(vendor);
        when(this.orderService.getAllOrdersForVendor(id)).thenReturn(orders);
        assertNull(this.vendorService.getDishesToPrepare(1234L, id));
    }

    @Test
    public void testGetDishesToPrepareValid() {
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        Dish dish3 = new Dish();
        dish3.setId(3L);
        List<Dish> dishes1 = new ArrayList<>();
        dishes1.add(dish1);
        dishes1.add(dish2);
        List<Dish> dishes2 = new ArrayList<>();
        dishes2.add(dish3);
        Order order1 = new Order();
        order1.setOrderId(12L);
        order1.setDishes(dishes1);
        Order order2 = new Order();
        order2.setOrderId(13L);
        order2.setDishes(dishes2);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Vendor vendor = new Vendor();
        Long id = this.vendorRepository.count();
        vendor.setId(id);
        this.vendorRepository.save(vendor);
        when(this.orderService.getAllOrdersForVendor(id)).thenReturn(orders);
        assertEquals(dishes2, this.vendorService.getDishesToPrepare(13L, id));
    }

    @Test
    public void testGetVendorDishesForCustomerNoAllergies() {
        Long id = this.vendorRepository.count();
        Vendor vendor = new Vendor();
        vendor.setId(id);
        Dish dish1 = new Dish();
        List<String> allergens1 = List.of("Milk", "Sugar");
        List<String> allergens2 = List.of("Salt");
        dish1.setAllergens(allergens1);
        Dish dish2 = new Dish();
        dish2.setAllergens(allergens2);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        vendor.setName("Vendor1");
        vendor.setDishes(dishes);
        this.vendorRepository.save(vendor);
        Customer customer = new Customer();
        customer.setId(13L);
        customer.setAllergens(new ArrayList<>());
        when(this.customerService.getCustomer(13L)).thenReturn(customer);
        List<Dish> dishesRetrive = this.vendorService.getVendorDishesforCustomer(id, 13L);
        assertEquals(dishes.size(), dishesRetrive.size());
        assertEquals(dishes.get(0), dishesRetrive.get(0));
        assertEquals(dishes.get(1), dishesRetrive.get(1));
    }

    @Test
    public void testGetVendorDishesForCustomerNoDishes() {
        Long id = this.vendorRepository.count();
        Vendor vendor = new Vendor();
        vendor.setId(id);
        Dish dish1 = new Dish();
        List<String> allergens1 = List.of("Milk", "Sugar");
        List<String> allergens2 = List.of("Salt");
        dish1.setAllergens(allergens1);
        Dish dish2 = new Dish();
        dish2.setAllergens(allergens2);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        vendor.setName("Vendor1");
        vendor.setDishes(dishes);
        this.vendorRepository.save(vendor);
        Customer customer = new Customer();
        customer.setId(13L);
        List<String> customerAllergens = List.of("Milk", "Salt");
        customer.setAllergens(customerAllergens);
        when(this.customerService.getCustomer(13L)).thenReturn(customer);

        List<Dish> dishesRetrive = this.vendorService.getVendorDishesforCustomer(id, 13L);
        assertEquals(0, dishesRetrive.size());
    }

    @Test
    public void testGetVendorDishesForCustomerSelectiveDishes() {
        Long id = this.vendorRepository.count();
        Vendor vendor = new Vendor();
        vendor.setId(id);
        Dish dish1 = new Dish();
        List<String> allergens1 = List.of("Milk", "Sugar");
        List<String> allergens2 = List.of("Salt");
        dish1.setAllergens(allergens1);
        Dish dish2 = new Dish();
        dish2.setAllergens(allergens2);
        List<Dish> dishes = new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        vendor.setName("Vendor1");
        vendor.setDishes(dishes);
        this.vendorRepository.save(vendor);
        Customer customer = new Customer();
        customer.setId(13L);
        List<String> customerAllergens = List.of("Milk");
        customer.setAllergens(customerAllergens);
        when(this.customerService.getCustomer(13L)).thenReturn(customer);

        List<Dish> dishesRetrive = this.vendorService.getVendorDishesforCustomer(id, 13L);
        assertEquals(1, dishesRetrive.size());
        assertEquals(dish2, dishesRetrive.get(0));
    }

    @Test
    public void testGetVendorDishesForCustomerNoVendor() {
        Customer customer = new Customer();
        customer.setId(13L);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.vendorService.getVendorDishesforCustomer(112L, 13L));
    }

    @Test
    public void testGetVendorDishesForCustomerNoCustomer() {
        Long id = 112L;
        Vendor vendor = new Vendor();
        vendor.setId(id);
        vendor.setName("Vendor1");
        this.vendorRepository.save(vendor);
        when(customerService.getCustomer(13L)).thenReturn(null);
        List<Dish> dishesRetrive = this.vendorService.getVendorDishesforCustomer(112L, 13L);
        assertNull(dishesRetrive);
    }

    @Test
    public void testGetVendorDishesForCustomerNoCustomerNoVendor() {
        when(customerService.getCustomer(13L)).thenReturn(null);
        List<Dish> dishesRetrive = this.vendorService.getVendorDishesforCustomer(112L, 13L);
        assertNull(dishesRetrive);
    }

    @Test
    public void testCreateNewVendor() {
        Vendor vendor = this.vendorService.createNewVendor("Vendor1");
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("save", this.vendorRepository.getMethodCalls().get(0));
        assertEquals("Vendor1", vendor.getName());
    }

    @Test
    public void testRemoveDish() {
        Vendor vendor = new Vendor();
        vendor.setId(10L);

        Dish d1 = new Dish().id(1L).name("Dumplings");
        Dish d2 = new Dish().id(2L).name("Schnitzel");
        List<Dish> dishes = new ArrayList<>(List.of(d1, d2));

        vendor.setDishes(dishes);

        boolean result = vendorService.removeDishFromVendor(d1, vendor);

        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("save", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(true, result);
    }

    @Test
    public void testRemoveDishNotPresent() {
        Vendor vendor = new Vendor();
        vendor.setId(10L);

        Dish d1 = new Dish().id(1L).name("Dumplings");
        Dish d2 = new Dish().id(2L).name("Schnitzel");
        List<Dish> dishes = new ArrayList<>(List.of(d2));

        vendor.setDishes(dishes);

        boolean result = vendorService.removeDishFromVendor(d1, vendor);

        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("save", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(true, result);
    }

    @Test
    public void testGetAllVendors() {
        Vendor vendor1 = this.vendorService.createNewVendor("Vendor1");
        Vendor vendor2 = this.vendorService.createNewVendor("Vendor2");

        List<Vendor> vendors = this.vendorService.getAllVendors();
        assertEquals(3, this.vendorRepository.getMethodCalls().size());

        assertEquals(vendor1, vendors.get(0));
        assertEquals(vendor2, vendors.get(1));
        assertEquals(2, vendors.size());
    }

    @Test
    public void testGetAllVendorsEmpty() {
        List<Vendor> vendors = this.vendorService.getAllVendors();
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("findAll", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(0, vendors.size());
    }

    @Test
    public void testFindByVendorNameContaining() {
        Vendor vendor1 = this.vendorService.createNewVendor("Bistro");
        Vendor vendor2 = this.vendorService.createNewVendor("Restaurant");

        List<Vendor> vendors = this.vendorService.findByVendorNameContaining("Bistro");
        assertEquals(3, this.vendorRepository.getMethodCalls().size());

        assertEquals(vendor1, vendors.get(0));
        assertEquals(1, vendors.size());
    }

    @Test
    public void testFindByVendorNameContainingEmpty() {
        List<Vendor> vendors = this.vendorService.findByVendorNameContaining("Bistro");
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("findByVendorNameContaining", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(0, vendors.size());
    }

    @Test
    public void testFindByLocationWithinRadius() {
        Vendor vendor1 = this.vendorService.createNewVendor("Bistro");
        Location location1 = new Location();
        location1.setLatitude(23.01);
        location1.setLongitude(23.0);
        vendor1.setLocation(location1);

        Vendor vendor2 = this.vendorService.createNewVendor("Restaurant");
        Location location2 = new Location();
        location2.setLatitude(43.0);
        location2.setLongitude(23.0);
        vendor2.setLocation(location2);

        List<Vendor> vendors = this.vendorService.findByLocationWithinRadius(location1, "Bistro", 4000);
        assertEquals(3, this.vendorRepository.getMethodCalls().size());
        assertEquals("findByLocationWithinRadius", this.vendorRepository.getMethodCalls().get(2));
        assertEquals(vendor1, vendors.get(0));
        assertEquals(1, vendors.size());
    }

    @Test
    public void testFindByLocationEmpty() {
        Location location1 = new Location();
        location1.setLatitude(23.01);
        location1.setLongitude(23.0);

        List<Vendor> vendors = this.vendorService.findByLocationWithinRadius(location1, "Bistro", 4000);
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("findByLocationWithinRadius", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(0, vendors.size());
    }

    @Test
    public void testFindByLocationNull() {
        List<Vendor> vendors = this.vendorService.findByLocationWithinRadius(null, "Bistro", 4000);
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("findByLocationWithinRadius", this.vendorRepository.getMethodCalls().get(0));
        assertEquals(0, vendors.size());
    }

    @Test
    public void testFindByLocationNoRadius() {
        Vendor vendor1 = this.vendorService.createNewVendor("Bistro");
        Location location1 = new Location();
        location1.setLatitude(23.01);
        location1.setLongitude(23.0);
        vendor1.setLocation(location1);

        Vendor vendor2 = this.vendorService.createNewVendor("Restaurant");
        Location location2 = new Location();
        location2.setLatitude(43.0);
        location2.setLongitude(23.0);
        vendor2.setLocation(location2);

        List<Vendor> vendors = this.vendorService.findByLocationWithinRadius(location1, "Bistro", null);
        assertEquals(3, this.vendorRepository.getMethodCalls().size());
        assertEquals("findByLocationWithinRadius", this.vendorRepository.getMethodCalls().get(2));
        assertEquals(vendor1, vendors.get(0));
        assertEquals(1, vendors.size());
    }

    @Test
    public void testSaveDishToVendor() {
        Vendor vendor = new Vendor();
        Long id = this.vendorRepository.count();
        vendor.setId(id);
        this.vendorRepository.save(vendor);

        Dish savedDish = new Dish();

        vendorService.saveDishToVendor(id, savedDish);

        assertEquals("save", this.vendorRepository.getMethodCalls().get(1));
        assertTrue(vendor.getDishes().contains(savedDish));
    }

    @Test
    public void testSaveDishToVendorNotVendor() {
        Vendor vendor = new Vendor();
        Long id = this.vendorRepository.count();
        vendor.setId(id);

        Dish savedDish = new Dish();

        vendorService.saveDishToVendor(id, savedDish);

        assertNotEquals("save", this.vendorRepository.getMethodCalls().get(0));
    }

    @Test
    public void isInvalidLocationTrue() {
        Location validLocation = new Location(2L, -91.7128, -74.0060);

        boolean result = vendorService.isInvalidLocation(validLocation);

        assertTrue(result);
    }

    @Test
    public void isInvalidLocation90() {
        Location invalidLatitude = new Location(1L, -90.0, -74.0060);

        boolean result = vendorService.isInvalidLocation(invalidLatitude);

        assertFalse(result);
    }

    @Test
    public void isInvalidLocationNull() {
        Location invalid = null;

        boolean result = vendorService.isInvalidLocation(invalid);

        assertTrue(result);
    }

    @Test
    public void isInvalidLocationMore90() {
        Location invalidLatitude = new Location(1L, 90.0, -74.0060);

        boolean result = vendorService.isInvalidLocation(invalidLatitude);

        assertFalse(result);
    }

    @Test
    public void isInvalidLocationLess180() {
        Location invalidLongitude = new Location(1L, 80.0, -180.0);

        boolean result = vendorService.isInvalidLocation(invalidLongitude);

        assertFalse(result);
    }

    @Test
    public void isInvalidLocationMore180() {
        Location invalidLongitude = new Location(1L, 80.0, 180.0);

        boolean result = vendorService.isInvalidLocation(invalidLongitude);

        assertFalse(result);
    }

}
