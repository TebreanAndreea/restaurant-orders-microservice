package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerAllOfModelTest {

    private CustomerAllOf customerAllOf;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.customerAllOf = new CustomerAllOf();
    }

    @Test
    public void getAllergensTest() {
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals(new ArrayList<>(), this.customerAllOf.getAllergens());
    }

    @Test
    public void getHomeAddressTest() {
        this.customerAllOf.setHomeAddress(new Location());
        assertEquals(new Location(), this.customerAllOf.getHomeAddress());
    }

    @Test
    public void getPaymentMethodTest() {
        this.customerAllOf.setPaymentMethod(CustomerAllOf.PaymentMethodEnum.CASH);
        assertEquals(CustomerAllOf.PaymentMethodEnum.CASH, this.customerAllOf.getPaymentMethod());
    }

    @Test
    public void allergensTest() {
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals(this.customerAllOf, this.customerAllOf.allergens(new ArrayList<>()));
    }

    @Test
    public void pastOrdersTest() {
        this.customerAllOf.setPastOrders(new ArrayList<>());
        assertEquals(this.customerAllOf, this.customerAllOf.pastOrders(new ArrayList<>()));
    }

    @Test
    public void savedOrdersTest() {
        this.customerAllOf.setSavedOrders(new ArrayList<>());
        assertEquals(this.customerAllOf, this.customerAllOf.savedOrders(new ArrayList<>()));
    }

    @Test
    public void favouriteRestaurantTest() {
        this.customerAllOf.setFavouriteRestaurants(new ArrayList<>());
        assertEquals(this.customerAllOf, this.customerAllOf.favouriteRestaurants(new ArrayList<>()));
    }

    @Test
    public void favouriteFoodsTest() {
        this.customerAllOf.setFavouriteFoods(new ArrayList<>());
        assertEquals(this.customerAllOf, this.customerAllOf.favouriteFoods(new ArrayList<>()));
    }

    @Test
    public void paymentMethodTest() {
        this.customerAllOf.setPaymentMethod(CustomerAllOf.PaymentMethodEnum.CASH);
        assertEquals(this.customerAllOf, this.customerAllOf.paymentMethod(CustomerAllOf.PaymentMethodEnum.CASH));
    }

    @Test
    public void homeAddressTest() {
        this.customerAllOf.setHomeAddress(new Location());
        assertEquals(this.customerAllOf, this.customerAllOf.homeAddress(new Location()));
    }


    @Test
    public void testEquals() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();

        customerAllOf2.setAllergens(new ArrayList<>());
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals(true, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEqualsNull() {
        assertEquals(false, this.customerAllOf.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        assertEquals(false, this.customerAllOf.equals(new Object()));
    }



    @Test
    public void testNotEquals1() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<String> list = new ArrayList<>();
        list.add("s");
        customerAllOf2.setAllergens(list);
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }


    @Test
    public void testNotEquals2() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<Dish> list = new ArrayList<>();
        list.add(new Dish());
        customerAllOf2.setFavouriteFoods(list);
        this.customerAllOf.setFavouriteFoods(new ArrayList<>());
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEquals3() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<String> list = new ArrayList<>();
        list.add("s");
        customerAllOf2.setHomeAddress(new Location());
        this.customerAllOf.setHomeAddress(new Location(1L, 1.1, 1.1));
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEquals4() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<Order> list = new ArrayList<>();
        list.add(new Order());
        customerAllOf2.setPastOrders(list);
        this.customerAllOf.setPastOrders(new ArrayList<>());
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEquals5() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<Order> list = new ArrayList<>();
        list.add(new Order());
        customerAllOf2.setSavedOrders(list);
        this.customerAllOf.setSavedOrders(new ArrayList<>());
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEquals9() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        customerAllOf2.setPaymentMethod(CustomerAllOf.PaymentMethodEnum.APPLEWALLET);
        this.customerAllOf.setPaymentMethod(CustomerAllOf.PaymentMethodEnum.CASH);
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }

    @Test
    public void testNotEquals11() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        List<Vendor> list = new ArrayList<>();
        list.add(new Vendor());
        customerAllOf2.setFavouriteRestaurants(list);
        this.customerAllOf.setFavouriteRestaurants(new ArrayList<>());
        assertEquals(false, this.customerAllOf.equals(customerAllOf2));
    }


    @Test
    public void testHashcode() {
        CustomerAllOf customerAllOf2 = new CustomerAllOf();
        customerAllOf2.setFavouriteFoods(new ArrayList<>());
        List<String> allergens = new ArrayList<>();
        allergens.add("fish");
        customerAllOf2.setAllergens(allergens);
        customerAllOf2.setFavouriteFoods(new ArrayList<>());
        customerAllOf2.setAllergens(new ArrayList<>());
        this.customerAllOf.setFavouriteFoods(new ArrayList<>());
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals(this.customerAllOf.hashCode(), customerAllOf2.hashCode());
        assertEquals(this.customerAllOf, customerAllOf2);
        assertEquals(false, this.customerAllOf.hashCode() + 1 == customerAllOf2.hashCode());
        CustomerAllOf customerAllOf3 = new CustomerAllOf();
        assertNotEquals(customerAllOf3, customerAllOf2);
        assertNotEquals(customerAllOf3.hashCode(), customerAllOf2.hashCode());
        assertNotEquals(this.customerAllOf.hashCode(), customerAllOf3.hashCode());
    }

    @Test
    public void testToString() {
        this.customerAllOf.setAllergens(new ArrayList<>());
        assertEquals("class CustomerAllOf {\n" + "    homeAddress: null\n"
                + "    paymentMethod: null\n"
                + "    favouriteFoods: null\n"
                + "    favouriteRestaurants: null\n"
                + "    savedOrders: null\n"
                + "    pastOrders: null\n"
                + "    allergens: []\n"
                + "}", this.customerAllOf.toString());
    }



    @Test
    public void testAddAllergensItem() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.customerAllOf.setAllergens(null);
        CustomerAllOf check = this.customerAllOf.addAllergensItem("as");
        assertEquals(allergens, this.customerAllOf.getAllergens());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getAllergens());
    }

    @Test
    public void testAddAllergensItem2() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.customerAllOf.setAllergens(allergens);
        CustomerAllOf check = this.customerAllOf.addAllergensItem("as");
        assertEquals(allergens, this.customerAllOf.getAllergens());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getAllergens());
    }

    @Test
    public void testAddFavouriteFoodsItem() {

        List<Dish> favouriteFoods = new ArrayList<>();
        favouriteFoods.add(new Dish());
        this.customerAllOf.setFavouriteFoods(null);
        CustomerAllOf check = this.customerAllOf.addFavouriteFoodsItem(new Dish());
        assertEquals(favouriteFoods, this.customerAllOf.getFavouriteFoods());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getFavouriteFoods());
    }

    @Test
    public void testAddFavouriteFoodsItem2() {

        List<Dish> favouriteFoods = new ArrayList<>();
        favouriteFoods.add(new Dish());
        this.customerAllOf.setFavouriteFoods(new ArrayList<>());
        CustomerAllOf check = this.customerAllOf.addFavouriteFoodsItem(new Dish());
        assertEquals(favouriteFoods, this.customerAllOf.getFavouriteFoods());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getFavouriteFoods());
    }

    @Test
    public void testAddFavouriteRestaurantsItem() {

        List<Vendor> favouriteRestaurants = new ArrayList<>();
        favouriteRestaurants.add(new Vendor());
        this.customerAllOf.setFavouriteRestaurants(null);
        CustomerAllOf check = this.customerAllOf.addFavouriteRestaurantsItem(new Vendor());
        assertEquals(favouriteRestaurants, this.customerAllOf.getFavouriteRestaurants());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getFavouriteRestaurants());
    }

    @Test
    public void testAddFavouriteRestaurantsItem2() {

        List<Vendor> favouriteRestaurants = new ArrayList<>();
        favouriteRestaurants.add(new Vendor());
        this.customerAllOf.setFavouriteRestaurants(new ArrayList<>());
        CustomerAllOf check = this.customerAllOf.addFavouriteRestaurantsItem(new Vendor());
        assertEquals(favouriteRestaurants, this.customerAllOf.getFavouriteRestaurants());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getFavouriteRestaurants());
    }

    @Test
    public void testAddSavedOrdersItem() {

        List<Order> savedOrder = new ArrayList<>();
        savedOrder.add(new Order());
        this.customerAllOf.setSavedOrders(null);
        CustomerAllOf check = this.customerAllOf.addSavedOrdersItem(new Order());
        assertEquals(savedOrder, this.customerAllOf.getSavedOrders());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getSavedOrders());
    }

    @Test
    public void testAddSavedOrdersItem2() {

        List<Order> savedOrder = new ArrayList<>();
        savedOrder.add(new Order());
        this.customerAllOf.setSavedOrders(new ArrayList<>());
        CustomerAllOf check = this.customerAllOf.addSavedOrdersItem(new Order());
        assertEquals(savedOrder, this.customerAllOf.getSavedOrders());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getSavedOrders());
    }

    @Test
    public void testAddPastOrdersItem() {

        List<Order> pastOrder = new ArrayList<>();
        pastOrder.add(new Order());
        this.customerAllOf.setPastOrders(null);
        CustomerAllOf check = this.customerAllOf.addPastOrdersItem(new Order());
        assertEquals(pastOrder, this.customerAllOf.getPastOrders());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getPastOrders());
    }

    @Test
    public void testAddPastOrdersItem2() {

        List<Order> pastOrder = new ArrayList<>();
        pastOrder.add(new Order());
        this.customerAllOf.setPastOrders(new ArrayList<>());
        CustomerAllOf check = this.customerAllOf.addPastOrdersItem(new Order());
        assertEquals(pastOrder, this.customerAllOf.getPastOrders());
        assertEquals(check, this.customerAllOf);
        assertNotNull(this.customerAllOf.getPastOrders());
    }

    @Test
    public void enumGetValue() {
        assertEquals("cash", CustomerAllOf.PaymentMethodEnum.CASH.getValue());
    }

    @Test
    public void enumToString() {
        assertEquals("cash", CustomerAllOf.PaymentMethodEnum.CASH.toString());
    }

    @Test
    public void enumFromValue() {
        assertEquals(CustomerAllOf.PaymentMethodEnum.CASH, CustomerAllOf.PaymentMethodEnum.fromValue("cash"));
    }

    @Test
    public void enumFromValueNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Your code that should throw IllegalArgumentException
            CustomerAllOf.PaymentMethodEnum.fromValue("caesh");
        });
    }
}
