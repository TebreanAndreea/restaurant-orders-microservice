package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.TestDishRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DishServiceTest {

    private TestDishRepository dishRepository;
    private DishService dishService;

    /**
     * Setup of the mocked objects before each test.
     */
    @BeforeEach
    public void setup() {
        this.dishRepository = new TestDishRepository();
        this.dishService = new DishService(dishRepository);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 112L;
        assertEquals(this.dishService.getDishById(id), Optional.empty());

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

        Optional<Dish> retrievedDish = this.dishService.getDishById(savedDish.getId());
        assertEquals(dish.getName(), retrievedDish.get().getName());
    }

    @Test
    public void testModifyExistingDish() {
        Dish modifiedDish = new Dish();
        Long id = this.dishRepository.count();
        modifiedDish.setId(id);
        modifiedDish.setName("french fries");
        modifiedDish.setPrice(13.2);

        this.dishRepository.save(modifiedDish);

        Dish dish = new Dish();
        dish.setId(id);
        dish.setName("hamburger with fries");
        dish.setPrice(20.3);
        dish.setAllergens(List.of("gluten"));

        Optional<Dish> result = dishService.modifyDish(dish);

        System.out.println("Method calls: " + this.dishRepository.getMethodCalls());

        assertEquals("save", this.dishRepository.getMethodCalls().get(1));

        assertEquals(result.get().getName(), dish.getName());
        assertEquals(result.get().getPrice(), dish.getPrice());
        assertEquals(result.get().getAllergens(), dish.getAllergens());

    }

    @Test
    public void testModifyNonExistentDish() {
        Dish dish = new Dish();
        dish.setId(10L);

        assertEquals(this.dishService.modifyDish(dish), Optional.empty());
    }

    @Test
    public void testGetDishById() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = this.dishService.createNewDish(dish);

        assertEquals(1, this.dishRepository.getMethodCalls().size());
        assertEquals("save", this.dishRepository.getMethodCalls().get(0));

        Optional<Dish> optionalDish = this.dishService.getDishById(savedDish.getId());
        Dish retrievedDish = optionalDish.get();
        assertEquals(dish.getName(), retrievedDish.getName());
    }

    @Test
    public void testGetDishByIdError() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        Dish savedDish = this.dishService.createNewDish(dish);

        Optional<Dish> retrievedDish = this.dishService.getDishById(112L);

        assertTrue(retrievedDish.isEmpty(), "No dish exists with id 112");
    }
}