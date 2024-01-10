package nl.tudelft.sem.yumyumnow.controller;

import java.util.NoSuchElementException;
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
        try {
            Dish dish = dishService.getDishById(dishId);
            return ResponseEntity.ok(dish);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
