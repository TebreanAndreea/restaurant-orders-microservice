package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;
import nl.tudelft.sem.yumyumnow.controller.DishController;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DishControllerTest {

    private DishService dishService;
    private DishController dishController;

    @BeforeEach
    void setUp() {
        dishService = Mockito.mock(DishService.class);
        dishController = new DishController(dishService);
    }

    @Test
    public void testGetDish() {
        Mockito.when(dishService.getDishById(1L)).thenThrow(new NoSuchElementException());

        ResponseEntity<Dish> response = dishController.getDish(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetDish2() {
        Dish dish = new Dish();
        dish.setName("Pizza");
        dish.setId(1L);
        Mockito.when(dishService.getDishById(1L)).thenReturn(dish);

        ResponseEntity<Dish> response = dishController.getDish(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dish, response.getBody());
    }
}
