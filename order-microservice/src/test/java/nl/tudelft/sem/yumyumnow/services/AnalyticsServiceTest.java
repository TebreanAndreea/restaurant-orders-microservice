package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnalyticsServiceTest {

    private TestRatingRepository testRatingRepository;
    private CustomerService customerService;
    private AnalyticsService analyticsService;
    private VendorService vendorService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        this.vendorService = Mockito.mock(VendorService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.testRatingRepository = new TestRatingRepository();
        this.analyticsService = new AnalyticsService(customerService, vendorService, testRatingRepository, orderService);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 420L;
        assertEquals(this.analyticsService.getRatingById(id), Optional.empty());

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("findById", this.testRatingRepository.getMethodCalls().get(0));
    }

    @Test
    public void testAddDish() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("save", this.testRatingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
    }

    @Test
    public void testGetRatingById() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("save", this.testRatingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(2, this.testRatingRepository.getMethodCalls().size());
        assertEquals("findById", this.testRatingRepository.getMethodCalls().get(1));
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
    }

    @Test
    void testGetAverageVendorPrice() {
        Long vendorId = 1L;
        Dish dish1 = new Dish().id(2L).price(23.4);
        Dish dish2 = new Dish().id(3L).price(2.3);
        Dish dish3 = new Dish().id(4L).price(103.2);
        List<Dish> dishes = Arrays.asList(dish1, dish2, dish3);
        Double average = (23.4 + 2.3 + 103.2) / 3;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(average, analyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceOneDish() {
        Long vendorId = 1L;
        Dish dish = new Dish().id(2L).price(23.4);
        List<Dish> dishes = Collections.singletonList(dish);

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(23.4, analyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceNoDishes() {
        Long vendorId = 1L;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, analyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorRating() {
        Long vendorId = 1L;
        Long id = testRatingRepository.count();
        Rating rating1 = new Rating().id(id).grade(2L);
        Rating rating2 = new Rating().id(id + 1).grade(3L);
        Rating rating3 = new Rating().id(id + 2).grade(5L);

        List<Rating> ratings = Arrays.asList(rating1, rating2, rating3);
        List<Long> ratingsIds = Arrays.asList(rating1.getId(), rating2.getId(), rating3.getId());

        testRatingRepository.saveAll(ratings);

        Mockito.when(orderService.getAllRatingsForVendor(vendorId)).thenReturn(ratingsIds);

        Double average = (double) (rating1.getGrade() + rating2.getGrade() + rating3.getGrade()) / 3;
        assertEquals(average, analyticsService.getAverageVendorRating(vendorId));
    }

    @Test
    void testGetAverageVendorRatingOneRating() {
        Long vendorId = 1L;
        Long id = testRatingRepository.count();
        Rating rating = new Rating().id(id).grade(2L);

        List<Rating> ratings = Collections.singletonList(rating);
        List<Long> ratingsIds = Collections.singletonList(rating.getId());

        testRatingRepository.saveAll(ratings);

        Mockito.when(orderService.getAllRatingsForVendor(vendorId)).thenReturn(ratingsIds);

        assertEquals((double) rating.getGrade(), analyticsService.getAverageVendorRating(vendorId));
    }

    @Test
    void testGetAverageVendorRatingNoRatings() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllRatingsForVendor(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, analyticsService.getAverageVendorRating(vendorId));
    }

    @Test
    void testGetPopularDishOneDish() {
        Long vendorId = 1L;

        Dish dish = new Dish().id(1L).name("fries");

        Order order = new Order().orderId(11L).dishes(Collections.singletonList(dish));
        List<Order> orders = List.of(order);

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Dish popular = analyticsService.getPopularDish(vendorId);

        assertNotNull(popular);
        assertEquals(dish.getName(), popular.getName());
    }

    @Test
    void testGetPopularDishMoreDishes() {
        Long vendorId = 1L;

        Dish dish1 = new Dish().id(1L).name("fries");
        Dish dish2 = new Dish().id(2L).name("burger");
        Dish dish4 = new Dish().id(2L).name("donut");
        Dish dish5 = new Dish().id(2L).name("crepes");

        Order order1 = new Order().orderId(11L).dishes(List.of(dish1, dish2));
        Order order2 = new Order().orderId(11L).dishes(Collections.singletonList(dish1));
        Order order3 = new Order().orderId(11L).dishes(List.of(dish4, dish5));
        Order order4 = new Order().orderId(11L).dishes(List.of(dish4, dish4));
        List<Order> orders = List.of(order1, order2, order3, order4);

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Dish popular = analyticsService.getPopularDish(vendorId);

        assertNotNull(popular);
        assertEquals(dish4.getName(), popular.getName());
    }

    @Test
    void testGetPopularDishNull() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(null);

        Dish popular = analyticsService.getPopularDish(vendorId);

        assertEquals(null, popular);

    }

    @Test
    void testAverageOrdersPerDayOneOrder() {
        OffsetDateTime now = OffsetDateTime.now();

        Order order = new Order().orderId(11L).time(now);
        List<Order> orders = List.of(order);
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Double average = analyticsService.averageOrdersPerDay(vendorId);

        assertEquals(1, average);
    }

    @Test
    void testAverageOrdersPerDayMoreOrders() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime yesterday = now.minusDays(1);
        OffsetDateTime twoDaysAgo = now.minusDays(2);

        Order order1 = new Order().orderId(11L).time(now);
        Order order2 = new Order().orderId(11L).time(yesterday);
        Order order3 = new Order().orderId(11L).time(yesterday);
        Order order4 = new Order().orderId(11L).time(twoDaysAgo);
        List<Order> orders = List.of(order1, order2, order3, order4);
        Long vendorId = 1L;

        Double expected = (1.0 + 2.0 + 1.0) / 3.0;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Double average = analyticsService.averageOrdersPerDay(vendorId);

        assertEquals(expected, average);
    }

    @Test
    void testAverageOrdersPerDayNull() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(null);

        Double average = analyticsService.averageOrdersPerDay(vendorId);

        assertEquals(null, average);
    }


    @Test
    public void getCustomerAveragePriceTestValid() {
        Customer customer = new Customer();
        customer.setId(13L);
        Order order1 = new Order();
        order1.setPrice(10.1);
        Order order2 = new Order();
        order2.setPrice(12.3);
        Order order3 = new Order();
        order3.setPrice(50.67);
        Order order4 = new Order();
        order4.setPrice(10.9);
        Order order5 = new Order();
        order5.setPrice(34.78);
        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(23.75, this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullCustomer() {
        when(customerService.getCustomer(13L)).thenReturn(null);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullOrders() {
        Customer customer = new Customer();
        List<Order> orderList = null;
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullPrice() {
        Customer customer = new Customer();
        customer.setId(13L);
        Order order1 = new Order();
        order1.setPrice(10.1);
        Order order2 = new Order();
        order2.setPrice(12.3);
        Order order3 = new Order();
        order3.setPrice(50.67);
        Order order4 = new Order();
        order4.setPrice(null);
        Order order5 = new Order();
        order5.setPrice(34.78);
        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getOrdersPerMonthTestNullCustomer() {
        when(customerService.getCustomer(13L)).thenReturn(null);
        assertNull(this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestNullOrders() {
        Customer customer = new Customer();
        List<Order> orderList = null;
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestTestAllMonths() {
        String time1 = "2022-01-01T12:00:00+02:00";
        Order order1 = new Order();
        order1.setTime(OffsetDateTime.parse(time1));

        String time2 = "2022-02-01T12:00:00+02:00";
        Order order2 = new Order();
        order2.setTime(OffsetDateTime.parse(time2));

        String time3 = "2022-03-01T12:00:00+02:00";
        Order order3 = new Order();
        order3.setTime(OffsetDateTime.parse(time3));

        String time4 = "2022-04-01T12:00:00+02:00";
        Order order4 = new Order();
        order4.setTime(OffsetDateTime.parse(time4));

        String time5 = "2022-05-01T12:00:00+02:00";
        Order order5 = new Order();
        order5.setTime(OffsetDateTime.parse(time5));

        String time6 = "2022-06-01T12:00:00+02:00";
        Order order6 = new Order();
        order6.setTime(OffsetDateTime.parse(time6));

        String time7 = "2022-07-01T12:00:00+02:00";
        Order order7 = new Order();
        order7.setTime(OffsetDateTime.parse(time7));

        String time8 = "2022-08-01T12:00:00+02:00";
        Order order8 = new Order();
        order8.setTime(OffsetDateTime.parse(time8));

        String time9 = "2022-09-01T12:00:00+02:00";
        Order order9 = new Order();
        order9.setTime(OffsetDateTime.parse(time9));

        String time10 = "2022-10-01T12:00:00+02:00";
        Order order10 = new Order();
        order10.setTime(OffsetDateTime.parse(time10));

        String time11 = "2022-11-01T12:00:00+02:00";
        Order order11 = new Order();
        order11.setTime(OffsetDateTime.parse(time11));

        String time12 = "2022-12-01T12:00:00+02:00";
        Order order12 = new Order();
        order12.setTime(OffsetDateTime.parse(time12));

        List<Order> orderList = List.of(order1, order2, order3, order4, order5, order6,
            order7, order8, order9, order10, order11, order12);
        Customer customer = new Customer();
        customer.setId(13L);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(1.0, this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestTestSomeMonths() {
        String time1 = "2022-01-01T12:00:00+02:00";
        Order order1 = new Order();
        order1.setTime(OffsetDateTime.parse(time1));

        String time2 = "2022-01-01T12:00:00+02:00";
        Order order2 = new Order();
        order2.setTime(OffsetDateTime.parse(time2));

        String time3 = "2022-03-01T12:00:00+02:00";
        Order order3 = new Order();
        order3.setTime(OffsetDateTime.parse(time3));

        String time4 = "2021-05-01T12:00:00+02:00";
        Order order4 = new Order();
        order4.setTime(OffsetDateTime.parse(time4));

        String time5 = "2021-05-01T12:00:00+02:00";
        Order order5 = new Order();
        order5.setTime(OffsetDateTime.parse(time5));

        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        Customer customer = new Customer();
        customer.setId(13L);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(5.0 / 3.0, this.analyticsService.getOrdersPerMonth(13L));

    }
}
