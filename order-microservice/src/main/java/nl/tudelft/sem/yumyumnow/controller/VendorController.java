package nl.tudelft.sem.yumyumnow.controller;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.VendorApi;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorController implements VendorApi {

    private final DishService dishService;
    private final AuthenticationService authenticationService;
    private final VendorRepository vendorRepository;

    /**
     * Creates an instance of the controller with its required services.
     *
     * @param dishService a service managing Dish objects
     * @param authenticationService a service managing authentication
     * @param vendorRepository a repository handling Vendor data access and operations
     */
    @Autowired
    public VendorController(DishService dishService, AuthenticationService authenticationService,
                            VendorRepository vendorRepository) {
        this.dishService = dishService;
        this.authenticationService = authenticationService;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ResponseEntity<Void> addDishToVendor(Long vendorId, Dish dish) {
        if (this.authenticationService.isVendor(vendorId)) {
            try {
                Dish savedDish = this.dishService.createNewDish(dish);

                Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);
                if (vendorOptional.isPresent()) {
                    Vendor vendor = vendorOptional.get();
                    vendor.addDishesItem(savedDish);
                    vendorRepository.save(vendor);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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
