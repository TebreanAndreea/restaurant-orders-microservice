package nl.tudelft.sem.yumyumnow.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.VendorApi;
import nl.tudelft.sem.yumyumnow.database.VendorRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.DishService;
import nl.tudelft.sem.yumyumnow.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendorController implements VendorApi {

    private final DishService dishService;
    private final VendorService vendorService;
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
    public VendorController(DishService dishService, VendorService vendorService,
                            AuthenticationService authenticationService,
                            VendorRepository vendorRepository) {
        this.dishService = dishService;
        this.vendorService = vendorService;
        this.authenticationService = authenticationService;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ResponseEntity<Void> addDishToVendor(Long vendorId, Dish dish) {
        if (!this.authenticationService.isVendor(vendorId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            Dish savedDish = this.dishService.createNewDish(dish);
            vendorService.saveDishToVendor(vendorId, savedDish);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> removeDishFromVendor(Long dishId, Long vendorId) {
        try {
            Optional<Dish> optionalDish = dishService.getDishById(dishId);
            Vendor vendor = vendorService.getVendorById(vendorId);

            if (optionalDish.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            boolean removed = vendorService.removeDishFromVendor(optionalDish.get(), vendor);

            if (!removed) {
                throw new RuntimeException();
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Dish>> getVendorDishes(Long vendorId, Long customerId) {
        if (this.authenticationService.isCustomer(customerId) && this.authenticationService.isVendor(vendorId)) {
            List<Dish> dishes = vendorService.getVendorDishesforCustomer(vendorId, customerId);
            if (dishes != null)  {
                return new ResponseEntity<>(dishes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<List<Dish>> getDishesToPrepare(Long orderId, Long vendorId) {
        if (this.authenticationService.isVendor(vendorId)) {
            try {
                List<Dish> dishes = vendorService.getDishesToPrepare(orderId, vendorId);
                if (dishes != null) {
                    return new ResponseEntity<>(dishes, HttpStatus.OK);
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
    public ResponseEntity<Vendor> getVendor(Long vendorId) {
        try {
            Vendor vendor = vendorService.getVendorById(vendorId);
            return ResponseEntity.ok(vendor);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Vendor>> getAllVendors(String filter) {
        try {
            if (filter == null || filter.isEmpty()) {
                List<Vendor> vendors = this.vendorService.getAllVendors();
                return new ResponseEntity<>(vendors, HttpStatus.OK);
            } else {
                List<Vendor> vendors = this.vendorService.findByVendorNameContaining(filter);
                return new ResponseEntity<>(vendors, HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Vendor>> getAllVendorsAddress(Location location, String filter, Integer radius) {
        try {
            if (vendorService.isInvalidLocation(location)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            filter = filter == null ? "" : filter;

            if (radius == null || radius < 0) {
                radius = 1000;
            }

            List<Vendor> vendors = this.vendorService.findByLocationWithinRadius(location, filter, radius);
            return new ResponseEntity<>(vendors, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lets a vendor modify a dish from his list.
     *
     * @param dishId ID of dish which needs to be updated (required)
     * @param vendorId ID of vendor that needs to update the dish (required)
     * @param dish Update a dish (optional)
     * @return an empty response entity with an appropriate status code.
     */
    @Override
    public ResponseEntity<Void> modifyDishFromVendor(Long dishId, Long vendorId, Dish dish) {
        try {
            if (authenticationService.isVendor(vendorId)) {
                if (dishId == null || dishId < 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                Optional<Dish> existingDish = dishService.modifyDish(dish);

                if (existingDish.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
