package nl.tudelft.sem.yumyumnow.controller;

import java.util.List;
import nl.tudelft.sem.yumyumnow.api.VendorApi;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorController implements VendorApi {
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ResponseEntity<List<Dish>> getVendorDishes(Long vendorId, Long customerId) {
        Vendor vendor = vendorRepository.findById(vendorId)
            .orElse(null);

        if (vendor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendor.getDishes(), HttpStatus.OK);
    }
}
