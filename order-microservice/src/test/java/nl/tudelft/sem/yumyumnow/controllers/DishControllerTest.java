package nl.tudelft.sem.yumyumnow.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
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
        Mockito.when(dishService.getDishById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Dish> response = dishController.getDish(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetDish2() {
        Dish dish = new Dish();
        dish.setName("Pizza");
        dish.setId(1L);
        Mockito.when(dishService.getDishById(1L)).thenReturn(Optional.of(dish));

        ResponseEntity<Dish> response = dishController.getDish(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dish, response.getBody());
    }

    @Test
    public void testUpdateDish() {
        Dish dish = new Dish();
        dish.setName("Pizza");
        dish.setId(1L);
        Mockito.when(dishService.getDishById(1L)).thenReturn(Optional.of(dish));
        Mockito.when(dishService.modifyDish(dish)).thenReturn(Optional.of(dish));

        ResponseEntity<Dish> response = dishController.updateDish(1L, dish);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateDishNotFound() {
        Mockito.when(dishService.getDishById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Dish> response = dishController.updateDish(1L, new Dish());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateDishInternalServerError() {
        Dish dish = new Dish();
        dish.setName("Pizza");
        dish.setId(1L);
        Mockito.when(dishService.getDishById(1L)).thenReturn(Optional.of(dish));
        Mockito.when(dishService.modifyDish(dish)).thenReturn(Optional.empty());

        ResponseEntity<Dish> response = dishController.updateDish(1L, dish);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
