package nl.tudelft.sem.yumyumnow.controller;

import java.util.Optional;
import nl.tudelft.sem.yumyumnow.api.DishApi;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.services.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DishController implements DishApi {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * Returns a Dish object from the DB by its ID.
     *
     * @param dishId ID of the Dish (required)
     * @return the Dish object found for the given ID
     */
    @Override
    public ResponseEntity<Dish> getDish(Long dishId) {
        Optional<Dish> dishOptional = dishService.getDishById(dishId);

        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();
            return ResponseEntity.ok(dish);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates a Dish object.
     *
     * @param dishId ID of the Dish to be updated (required)
     * @param dish the new Dish object
     * @return the new Dish object, stored in the DB
     */
    @Override
    public ResponseEntity<Dish> updateDish(Long dishId, Dish dish) {
        Optional<Dish> dishOptional = dishService.getDishById(dishId);
        if (dishOptional.isPresent()) {
            Optional<Dish> updated = this.dishService.modifyDish(dish);
            if (updated.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
