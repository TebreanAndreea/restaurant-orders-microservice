package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerModelTest {

    private Customer customer;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.customer = new Customer();
    }

    @Test
    public void testId() {
        this.customer.setId(12L);
        this.customer.setName("12.2");
        assertEquals("12.2", this.customer.id(12L).getName());
        assertEquals(this.customer, this.customer.id(12L));
    }
    
    @Test
    public void testGetAndSetId() {
        this.customer.setId(12L);
        assertEquals(12L, this.customer.getId());
    }

    @Test
    public void allergensTest() {
        this.customer.setAllergens(new ArrayList<>());
        assertEquals(this.customer, this.customer.allergens(new ArrayList<>()));
    }

    @Test
    public void pastOrdersTest() {
        this.customer.setPastOrders(new ArrayList<>());
        assertEquals(this.customer, this.customer.pastOrders(new ArrayList<>()));
    }

    @Test
    public void savedOrdersTest() {
        this.customer.setSavedOrders(new ArrayList<>());
        assertEquals(this.customer, this.customer.savedOrders(new ArrayList<>()));
    }

    @Test
    public void favouriteRestaurantTest() {
        this.customer.setFavouriteRestaurants(new ArrayList<>());
        assertEquals(this.customer, this.customer.favouriteRestaurants(new ArrayList<>()));
    }

    @Test
    public void favouriteFoodsTest() {
        this.customer.setFavouriteFoods(new ArrayList<>());
        assertEquals(this.customer, this.customer.favouriteFoods(new ArrayList<>()));
    }

    @Test
    public void paymentMethodTest() {
        this.customer.setPaymentMethod(Customer.PaymentMethodEnum.CASH);
        assertEquals(this.customer, this.customer.paymentMethod(Customer.PaymentMethodEnum.CASH));
    }

    @Test
    public void homeAddressTest() {
        this.customer.setHomeAddress(new Location());
        assertEquals(this.customer, this.customer.homeAddress(new Location()));
    }

    @Test
    public void emailTest() {
        this.customer.setEmail("a");
        assertEquals(this.customer, this.customer.email("a"));
    }

    @Test
    public void surnameTest() {
        this.customer.setSurname("a");
        assertEquals(this.customer, this.customer.surname("a"));
    }

    @Test
    public void nameTest() {
        this.customer.setName("a");
        assertEquals(this.customer, this.customer.name("a"));
    }


    @Test
    public void testEqualsSameObject() {
        assertEquals(true, this.customer.equals(this.customer));
    }

    @Test
    public void testEquals() {
        Customer customer2 = new Customer();

        customer2.setEmail("12.1");
        customer2.setId(12L);
        this.customer.setEmail("12.1");
        this.customer.setId(12L);
        assertEquals(true, this.customer.equals(customer2));
    }

    @Test
    public void testNotEqualsNull() {
        assertEquals(false, this.customer.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        assertEquals(false, this.customer.equals(new Object()));
    }

    @Test
    public void testNotEquals1() {
        Customer customer2 = new Customer();

        customer2.setId(3L);
        customer2.setEmail("12.2");
        this.customer.setId(2L);
        this.customer.setEmail("12.2");
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals2() {
        Customer customer2 = new Customer();
        List<String> list = new ArrayList<>();
        list.add("s");
        customer2.setId(3L);
        customer2.setAllergens(list);
        this.customer.setId(3L);
        this.customer.setAllergens(new ArrayList<>());
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals3() {
        Customer customer2 = new Customer();

        customer2.setId(3L);
        customer2.setName("name");
        this.customer.setId(3L);
        this.customer.setName("a");
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals4() {
        Customer customer2 = new Customer();

        customer2.setId(3L);
        customer2.setSurname("name");
        this.customer.setId(3L);
        this.customer.setSurname("a");
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals5() {
        Customer customer2 = new Customer();
        List<Dish> list = new ArrayList<>();
        list.add(new Dish());
        customer2.setId(3L);
        customer2.setFavouriteFoods(list);
        this.customer.setId(3L);
        this.customer.setFavouriteFoods(new ArrayList<>());
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals6() {
        Customer customer2 = new Customer();
        List<String> list = new ArrayList<>();
        list.add("s");
        customer2.setId(3L);
        customer2.setHomeAddress(new Location());
        this.customer.setId(3L);
        this.customer.setHomeAddress(new Location(1L, 1.1, 1.1));
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals7() {
        Customer customer2 = new Customer();
        List<nl.tudelft.sem.yumyumnow.model.Order> list = new ArrayList<>();
        list.add(new nl.tudelft.sem.yumyumnow.model.Order());
        customer2.setId(3L);
        customer2.setPastOrders(list);
        this.customer.setId(3L);
        this.customer.setPastOrders(new ArrayList<>());
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals8() {
        Customer customer2 = new Customer();
        List<nl.tudelft.sem.yumyumnow.model.Order> list = new ArrayList<>();
        list.add(new nl.tudelft.sem.yumyumnow.model.Order());
        customer2.setId(3L);
        customer2.setSavedOrders(list);
        this.customer.setId(3L);
        this.customer.setSavedOrders(new ArrayList<>());
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals9() {
        Customer customer2 = new Customer();
        customer2.setId(3L);
        customer2.setPaymentMethod(Customer.PaymentMethodEnum.APPLEWALLET);
        this.customer.setId(3L);
        this.customer.setPaymentMethod(Customer.PaymentMethodEnum.CASH);
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals10() {
        Customer customer2 = new Customer();

        customer2.setId(2L);
        customer2.setEmail("12.2");
        this.customer.setId(2L);
        this.customer.setEmail("12.24");
        assertEquals(false, this.customer.equals(customer2));
    }

    @Test
    public void testNotEquals11() {
        Customer customer2 = new Customer();
        List<Vendor> list = new ArrayList<>();
        list.add(new Vendor());
        customer2.setId(2L);
        customer2.setFavouriteRestaurants(list);
        this.customer.setId(2L);
        this.customer.setFavouriteRestaurants(new ArrayList<>());
        assertEquals(false, this.customer.equals(customer2));
    }


    @Test
    public void testHashcode() {
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setEmail("12.2");
        customer2.setId(2L);
        customer2.setEmail("12.1");
        this.customer.setId(2L);
        this.customer.setEmail("12.1");
        assertEquals(this.customer.hashCode(), customer2.hashCode());
        assertEquals(this.customer, customer2);
        assertEquals(false, this.customer.hashCode() + 1 == customer2.hashCode());
        Customer customer3 = new Customer();
        assertNotEquals(customer3, customer2);
        assertNotEquals(customer3.hashCode(), customer2.hashCode());
        assertNotEquals(this.customer.hashCode(), customer3.hashCode());
    }

    @Test
    public void testToString() {
        this.customer.setId(2L);
        assertEquals("class Customer {\n" + "    id: 2\n"
                + "    name: null\n"
                + "    surname: null\n"
                + "    email: null\n"
                + "    homeAddress: null\n"
                + "    paymentMethod: null\n"
                + "    favouriteFoods: null\n"
                + "    favouriteRestaurants: null\n"
                + "    savedOrders: null\n"
                + "    pastOrders: null\n"
                + "    allergens: null\n"
                + "}", this.customer.toString());
    }

    @Test
    public void testConstructor() {
        Long id = 2L;
        String name = "name";
        String surname = "surname";
        String email = "email";
        Location homeAddress = new Location();
        Customer.PaymentMethodEnum paymentMethod = Customer.PaymentMethodEnum.CASH;
        List<Dish> favouriteFoods = new ArrayList<>();
        List<Vendor> favouriteRestaurants = new ArrayList<>();
        List<nl.tudelft.sem.yumyumnow.model.Order> savedOrders = new ArrayList<>();
        List<Order> pastOrders = new ArrayList<>();
        List<String> allergens = new ArrayList<>();
        Customer customer = new Customer(id, name, surname, email,
                homeAddress, paymentMethod, favouriteFoods, favouriteRestaurants,
                savedOrders, pastOrders, allergens);
        assertEquals(customer.getId(), 2L);
        assertEquals(customer.getName(), "name");
        assertEquals(customer.getAllergens(), new ArrayList<>());
        assertEquals(customer.getEmail(), "email");
        assertEquals(customer.getSurname(), "surname");
        assertEquals(customer.getFavouriteFoods(), new ArrayList<>());
        assertEquals(customer.getPastOrders(), new ArrayList<>());
        assertEquals(customer.getPaymentMethod(), Customer.PaymentMethodEnum.CASH);
        assertEquals(customer.getHomeAddress(), new Location());
        assertEquals(customer.getSavedOrders(), new ArrayList<>());
        assertEquals(customer.getFavouriteRestaurants(), new ArrayList<>());
        assertEquals(customer.getPastOrders(), new ArrayList<>());
    }

    @Test
    public void testAddAllergensItem() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.customer.setAllergens(null);
        Customer check = this.customer.addAllergensItem("as");
        assertEquals(allergens, this.customer.getAllergens());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getAllergens());
    }

    @Test
    public void testAddAllergensItem2() {

        List<String> allergens = new ArrayList<>();
        allergens.add("as");
        this.customer.setAllergens(allergens);
        Customer check = this.customer.addAllergensItem("as");
        assertEquals(allergens, this.customer.getAllergens());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getAllergens());
    }

    @Test
    public void testAddFavouriteFoodsItem() {

        List<Dish> favouriteFoods = new ArrayList<>();
        favouriteFoods.add(new Dish());
        this.customer.setFavouriteFoods(null);
        Customer check = this.customer.addFavouriteFoodsItem(new Dish());
        assertEquals(favouriteFoods, this.customer.getFavouriteFoods());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getFavouriteFoods());
    }

    @Test
    public void testAddFavouriteFoodsItem2() {

        List<Dish> favouriteFoods = new ArrayList<>();
        favouriteFoods.add(new Dish());
        this.customer.setFavouriteFoods(new ArrayList<>());
        Customer check = this.customer.addFavouriteFoodsItem(new Dish());
        assertEquals(favouriteFoods, this.customer.getFavouriteFoods());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getFavouriteFoods());
    }

    @Test
    public void testAddFavouriteRestaurantsItem() {

        List<Vendor> favouriteRestaurants = new ArrayList<>();
        favouriteRestaurants.add(new Vendor());
        this.customer.setFavouriteRestaurants(null);
        Customer check = this.customer.addFavouriteRestaurantsItem(new Vendor());
        assertEquals(favouriteRestaurants, this.customer.getFavouriteRestaurants());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getFavouriteRestaurants());
    }

    @Test
    public void testAddFavouriteRestaurantsItem2() {

        List<Vendor> favouriteRestaurants = new ArrayList<>();
        favouriteRestaurants.add(new Vendor());
        this.customer.setFavouriteRestaurants(new ArrayList<>());
        Customer check = this.customer.addFavouriteRestaurantsItem(new Vendor());
        assertEquals(favouriteRestaurants, this.customer.getFavouriteRestaurants());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getFavouriteRestaurants());
    }

    @Test
    public void testAddSavedOrdersItem() {

        List<Order> savedOrder = new ArrayList<>();
        savedOrder.add(new Order());
        this.customer.setSavedOrders(null);
        Customer check = this.customer.addSavedOrdersItem(new Order());
        assertEquals(savedOrder, this.customer.getSavedOrders());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getSavedOrders());
    }

    @Test
    public void testAddSavedOrdersItem2() {

        List<Order> savedOrder = new ArrayList<>();
        savedOrder.add(new Order());
        this.customer.setSavedOrders(new ArrayList<>());
        Customer check = this.customer.addSavedOrdersItem(new Order());
        assertEquals(savedOrder, this.customer.getSavedOrders());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getSavedOrders());
    }

    @Test
    public void testAddPastOrdersItem() {

        List<Order> pastOrder = new ArrayList<>();
        pastOrder.add(new Order());
        this.customer.setPastOrders(null);
        Customer check = this.customer.addPastOrdersItem(new Order());
        assertEquals(pastOrder, this.customer.getPastOrders());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getPastOrders());
    }

    @Test
    public void testAddPastOrdersItem2() {

        List<Order> pastOrder = new ArrayList<>();
        pastOrder.add(new Order());
        this.customer.setPastOrders(new ArrayList<>());
        Customer check = this.customer.addPastOrdersItem(new Order());
        assertEquals(pastOrder, this.customer.getPastOrders());
        assertEquals(check, this.customer);
        assertNotNull(this.customer.getPastOrders());
    }

    @Test
    public void enumGetValue() {
        assertEquals("cash", Customer.PaymentMethodEnum.CASH.getValue());
    }

    @Test
    public void enumToString() {
        assertEquals("cash", Customer.PaymentMethodEnum.CASH.toString());
    }

    @Test
    public void enumFromValue() {
        assertEquals(Customer.PaymentMethodEnum.CASH, Customer.PaymentMethodEnum.fromValue("cash"));
    }

    @Test
    public void enumFromValueNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Your code that should throw IllegalArgumentException
            Customer.PaymentMethodEnum.fromValue("caesh");
        });
    }
}
