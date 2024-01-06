package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestVendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
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

}
