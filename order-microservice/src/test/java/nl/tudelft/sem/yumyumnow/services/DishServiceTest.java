package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.database.TestDishRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DishServiceTest {

    private TestDishRepository dishRepository;
    private DishService dishService;

    @BeforeEach
    public void setup() {
        this.dishRepository = new TestDishRepository();
        this.dishService = new DishService(this.dishRepository);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 112L;
        assertThrows(NoSuchElementException.class, () -> this.dishService.getDishById(id),
                "No dish exists with id 112");

        assertEquals(1, this.dishRepository.getMethodCalls().size());
        assertEquals("findById", this.dishRepository.getMethodCalls().get(0));
    }

    @Test
    public void testAddDish() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = this.dishService.createNewDish(dish);

        assertEquals(1, this.dishRepository.getMethodCalls().size());
        assertEquals("save", this.dishRepository.getMethodCalls().get(0));

        Dish retrievedDish = this.dishService.getDishById(savedDish.getId());
        assertEquals(dish.getName(), retrievedDish.getName());
    }

    @Test
    public void testGetDishById() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = this.dishService.createNewDish(dish);

        assertEquals(1, this.dishRepository.getMethodCalls().size());
        assertEquals("save", this.dishRepository.getMethodCalls().get(0));

        Dish retrievedDish = this.dishService.getDishById(savedDish.getId());
        assertEquals(dish.getName(), retrievedDish.getName());
    }

    @Test
    public void testGetDishByIdError() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = this.dishService.createNewDish(dish);

        assertThrows(NoSuchElementException.class, () -> this.dishService.getDishById(112L),
                "No dish exists with id 112");
    }
}