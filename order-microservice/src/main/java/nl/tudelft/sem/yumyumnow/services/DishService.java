package nl.tudelft.sem.yumyumnow.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.DishRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    private final DishRepository dishRepository;

    /**
     * Creates a new Dish Service.
     *
     * @param repository the DB instance where the Dishes are stored
     */
    @Autowired
    public DishService(DishRepository repository) {
        this.dishRepository = repository;
    }

    /**
     * Returns a Dish object from the DB by its ID.
     *
     * @param dishId the ID of the dish to retrieve
     * @return the Dish object found for the given ID
     * @throws NoSuchElementException if there is no Dish object for the given ID in the DB
     */
    public Optional<Dish> getDishById(Long dishId) {
        return dishRepository.findById(dishId);
    }

    /**
     * Creates a new Dish object.
     *
     * @param dish the new Dish object
     * @return the new Dish object, stored in the DB
     */
    public Dish createNewDish(Dish dish) {
        if (dish.getId() == null) {
            dish.setId(System.currentTimeMillis());
        }
        return this.dishRepository.save(dish);
    }

    /**
     * Modifies an existing dish.
     *
     * @param dish the new dish
     * @return the updated dish
     */
    public Optional<Dish> modifyDish(Dish dish) {
        Long dishId = dish.getId();
        boolean exists = dishRepository.existsById(dishId);

        if (exists) {
            Dish saved = dishRepository.save(dish);

            return Optional.of(saved);
        } else {
            return Optional.empty();
        }
    }

}
