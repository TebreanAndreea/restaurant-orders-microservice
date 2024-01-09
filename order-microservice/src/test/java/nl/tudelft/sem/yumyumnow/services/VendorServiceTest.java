package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestVendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendorServiceTest {
    private TestVendorRepository vendorRepository;
    private VendorService vendorService;

    @BeforeEach
    public void setup() {
        this.vendorRepository = new TestVendorRepository();
        this.vendorService = new VendorService(this.vendorRepository);
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
        Long id = 112L;
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
    public void testCreateNewVendor() {
        Vendor vendor = this.vendorService.createNewVendor("Vendor1");
        assertEquals(1, this.vendorRepository.getMethodCalls().size());
        assertEquals("save", this.vendorRepository.getMethodCalls().get(0));
        assertEquals("Vendor1", vendor.getName());
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

}
