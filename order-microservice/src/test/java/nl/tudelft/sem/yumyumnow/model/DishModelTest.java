package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DishModelTest {

    private Dish dish;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.dish = new Dish();
    }

    @Test
    public void testId() {
        this.dish.setId(12L);
        this.dish.setName("Name");
        assertEquals("Name", this.dish.id(12L).getName());
        assertEquals(this.dish, this.dish.id(12L));
    }

    @Test
    public void testGetId() {
        this.dish.setId(12L);
        this.dish.setName("Name");
        assertEquals(12L, this.dish.getId());
    }

    @Test
    public void testGetAndSetId() {
        this.dish.setId(12L);
        this.dish.setName("Name");
        assertEquals(12L, this.dish.getId());
    }

    @Test
    public void testName() {
        this.dish.setPrice(22.2);
        this.dish.setName("a");
        assertEquals(22.2, this.dish.name("a").getPrice());
        assertEquals(this.dish, this.dish.name("a"));
    }

    @Test
    public void testGetAndSetName() {
        this.dish.setName("name");
        this.dish.setPrice(22.2);
        assertEquals("name", this.dish.getName());
    }

    @Test
    public void testPrice() {
        this.dish.setPrice(22.2);
        this.dish.setName("Name");
        assertEquals("Name", this.dish.price(22.2).getName());
        assertEquals(this.dish, this.dish.price(22.2));
    }

    @Test
    public void testGetAndSetPrice() {
        this.dish.setPrice(22.2);
        this.dish.setName("Name");
        assertEquals(22.2, this.dish.getPrice());
    }

    @Test
    public void testAllergens() {

        List<String> allergens = new ArrayList<>();
        List<String> empty = new ArrayList<>();
        allergens.add("as");
        this.dish.setAllergens(allergens);
        this.dish.setName("Name");
        assertEquals("Name", this.dish.allergens(allergens).getName());
        assertEquals(this.dish, this.dish.allergens(allergens));
    }


    @Test
    public void testGetAndSetAllergens() {
        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.dish.setAllergens(allergens);
        this.dish.setName("Name");
        assertEquals(allergens, this.dish.getAllergens());
    }

    @Test
    public void testAddAllergensItem() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.dish.setAllergens(null);
        Dish check = this.dish.addAllergensItem("as");
        assertEquals(allergens, this.dish.getAllergens());
        assertEquals(check, this.dish);
        assertNotNull(this.dish.getAllergens());
    }

    @Test
    public void testAddAllergensItem2() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.dish.setAllergens(new ArrayList<>());
        this.dish.addAllergensItem("as");
        assertEquals(allergens, this.dish.getAllergens());
        assertNotNull(this.dish.getAllergens());
    }

    @Test
    public void testEqualsSameObject() {
        assertEquals(true, this.dish.equals(this.dish));
    }

    @Test
    public void testEquals() {
        Dish dish2 = new Dish();

        dish2.setName("32H");
        dish2.setId(12L);
        this.dish.setName("32H");
        this.dish.setId(12L);
        assertEquals(true, this.dish.equals(dish2));
    }

    @Test
    public void testNotEqualsNull() {
        Dish dish2 = new Dish();

        dish2.setName("32H");
        dish2.setId(12L);
        this.dish.setName("3H");
        this.dish.setId(12L);
        assertEquals(false, this.dish.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        assertEquals(false, this.dish.equals(new Object()));
    }

    @Test
    public void testNotEquals1() {
        Dish dish2 = new Dish();

        dish2.setId(3L);
        dish2.setName("12H");
        this.dish.setId(2L);
        this.dish.setName("12H");
        assertEquals(false, this.dish.equals(dish2));
    }

    @Test
    public void testNotEquals2() {
        Dish dish2 = new Dish();

        dish2.setId(3L);
        dish2.setName("12H");
        this.dish.setId(3L);
        this.dish.setName("11H");
        assertEquals(false, this.dish.equals(dish2));
    }

    @Test
    public void testNotEquals3() {
        Dish dish2 = new Dish();

        dish2.setId(3L);
        dish2.setPrice(23.0);
        this.dish.setId(3L);
        this.dish.setPrice(231.0);
        assertEquals(false, this.dish.equals(dish2));
    }

    @Test
    public void testNotEquals4() {
        Dish dish2 = new Dish();
        List<String> allergens = new ArrayList<>();
        allergens.add("fish");
        dish2.setId(3L);
        dish2.setAllergens(allergens);
        this.dish.setId(3L);
        List<String> allergens2 = new ArrayList<>();
        this.dish.setAllergens(allergens2);
        assertEquals(false, this.dish.equals(dish2));
    }

    @Test
    public void testHashcode() {
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("su4rClosed");
        dish2.setId(2L);
        dish2.setName("Open");
        this.dish.setId(2L);
        this.dish.setName("Open");
        assertEquals(this.dish.hashCode(), dish2.hashCode());
        assertEquals(this.dish, dish2);
        assertEquals(false, this.dish.hashCode() + 1 == dish2.hashCode());
        Dish dish3 = new Dish();
        assertNotEquals(dish3, dish2);
        assertNotEquals(dish3.hashCode(), dish2.hashCode());
        assertNotEquals(this.dish.hashCode(), dish3.hashCode());
    }

    @Test
    public void testToString() {
        this.dish.setId(2L);
        this.dish.setName("email");
        assertEquals("class Dish {\n"
                + "    id: 2\n"
                + "    name: email\n"
                + "    allergens: null\n"
                + "    price: null\n"
                + "}", this.dish.toString());
    }

}
