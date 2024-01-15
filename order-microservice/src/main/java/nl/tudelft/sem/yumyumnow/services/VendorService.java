package nl.tudelft.sem.yumyumnow.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;
    private final OrderService orderService;
    private final CustomerService customerService;


    /**
     * Creates a new Vendor Service.
     *
     * @param repository the DB instance where the Vendors are stored
     * @param customerService an instance of the user service
     * @param orderService an instance of the order service
     */
    @Autowired
    public VendorService(VendorRepository repository, CustomerService customerService, OrderService orderService) {
        this.vendorRepository = repository;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    /**
     * Get a vendor by his id.
     *
     * @param vendorId The id of the vendor we want to retrieve.
     * @return The vendor with the corresponding id.
     */
    public Vendor getVendorById(Long vendorId) {
        return this.vendorRepository.findById(vendorId).orElseThrow(
            () -> new NoSuchElementException("No vendor exists with id " + vendorId));
    }

    /**
     * Return a vendor's list of dished.
     *
     * @param vendorId The id of the vendor we want to retrieve dishes from.
     * @return Vendor's list of dishes.
     */
    public List<Dish> getVendorDishes(Long vendorId) {
        Optional<Vendor> vendorOptional = this.vendorRepository.findById(vendorId);
        if (vendorOptional.isPresent()) {
            return vendorOptional.get().getDishes();
        }
        return null;
    }

    /**
     * Returns a restaurant's list of dishes which a customer is not allergic to.
     *
     * @param vendorId The id of the vendor.
     * @param customerId The id of the customer.
     * @return Restaurant's dishes which contains none of the customer's allergens.
     */
    public List<Dish> getVendorDishesforCustomer(Long vendorId, Long customerId) {
        Optional<Vendor> vendorOptional = this.vendorRepository.findById(vendorId);
        Customer customer = this.customerService.getCustomer(customerId);
        if (vendorOptional.isPresent() && customer != null) {
            Vendor vendor = vendorOptional.get();
            List<Dish> vendorDish = vendor.getDishes();
            List<String> allergens = customer.getAllergens();
            List<Dish> dishesWithoutAllergens = new ArrayList<>();
            for (Dish dish : vendorDish) {
                boolean hasAllergens = false;
                for (String allergen : dish.getAllergens()) {
                    if (allergens.contains(allergen)) {
                        hasAllergens = true;
                        break;
                    }
                }
                if (!hasAllergens) {
                    dishesWithoutAllergens.add(dish);
                }
            }
            return dishesWithoutAllergens;
        }
        return null;
    }

    /**
     * Create a new vendor.
     *
     * @param vendorName The name of the vendor we want to create.
     * @return The vendor with the corresponding id.
     */
    public Vendor createNewVendor(String vendorName) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorName);
        return this.vendorRepository.save(vendor);
    }

    /**
     * Removes a dish from a vendor's catalog.
     *
     * @param dish The dish to be removed.
     * @param vendor The vendor to remove the dish from.
     * @return true if the dish was successfully removed, or if it wasn't present in the catalog to begin with.
     */
    public boolean removeDishFromVendor(Dish dish, Vendor vendor) {
        vendor.getDishes().removeIf(x -> Objects.equals(x.getId(), dish.getId()));
        Vendor savedVendor = vendorRepository.save(vendor);
        boolean removed = !(savedVendor.getDishes().contains(dish));
        return removed;
    }

    /**
     * Return the dishes from the order with orderId that vendor with vendorId needs to prepare.
     *
     * @param orderId The order's id.
     * @param vendorId The vendor's id.
     * @return The dishes that needs to be prepared.
     */

    public List<Dish> getDishesToPrepare(Long orderId, Long vendorId) {
        List<Order> orders = this.orderService.getAllOrdersForVendor(vendorId);
        Order order = null;
        for (Order o : orders) {
            if (Objects.equals(o.getOrderId(), orderId)) {
                order = o;
                break;
            }
        }
        if (order != null) {
            return order.getDishes();
        }
        return null;
    }

    /**
     * Return a list of all vendors.
     *
     * @return List of all vendors.
     */
    public List<Vendor> getAllVendors() {
        return this.vendorRepository.findAll();
    }


    /**
     * Return a list of vendors filtered by name.
     *
     * @param filter The filter to apply to the vendor's name.
     * @return List of vendors filtered by name.
     */
    public List<Vendor> findByVendorNameContaining(String filter) {
        return this.vendorRepository.findByVendorNameContaining(filter);
    }

    /**
     * Return a list of vendors filtered by address.
     *
     * @param location The address to apply to the vendor's address.
     * @param filter The filter to apply to the vendor's name.
     * @param radius The radius to apply to the vendor's address.
     * @return List of vendors filtered by address.
     */
    public List<Vendor> findByLocationWithinRadius(Location location, String filter, Integer radius) {
        if (radius == null) {
            radius = 1000;
        }
        return this.vendorRepository.findByLocationWithinRadius(location, filter, radius);
    }
}
