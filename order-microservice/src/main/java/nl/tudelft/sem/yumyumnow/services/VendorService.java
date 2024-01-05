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
}
