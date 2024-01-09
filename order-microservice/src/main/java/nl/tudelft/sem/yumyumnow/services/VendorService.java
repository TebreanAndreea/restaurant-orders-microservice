package nl.tudelft.sem.yumyumnow.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository repository) {
        this.vendorRepository = repository;
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
}
