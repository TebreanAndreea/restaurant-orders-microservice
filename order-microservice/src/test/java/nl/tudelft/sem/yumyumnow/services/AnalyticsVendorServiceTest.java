package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnalyticsVendorServiceTest {

    private TestRatingRepository testRatingRepository;
    private CustomerService customerService;
    private AnalyticsVendorService analyticsVendorService;
    private VendorService vendorService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.customerService = mock(CustomerService.class);
        this.vendorService = Mockito.mock(VendorService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.testRatingRepository = new TestRatingRepository();
        this.analyticsVendorService = new AnalyticsVendorService(vendorService, testRatingRepository, orderService);
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

        assertEquals(average, analyticsVendorService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceOneDish() {
        Long vendorId = 1L;
        Dish dish = new Dish().id(2L).price(23.4);
        List<Dish> dishes = Collections.singletonList(dish);

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(23.4, analyticsVendorService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceNoDishes() {
        Long vendorId = 1L;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, analyticsVendorService.getAverageVendorPrice(vendorId));
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
        assertEquals(average, analyticsVendorService.getAverageVendorRating(vendorId));
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

        assertEquals((double) rating.getGrade(), analyticsVendorService.getAverageVendorRating(vendorId));
    }

    @Test
    void testGetAverageVendorRatingNoRatings() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllRatingsForVendor(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, analyticsVendorService.getAverageVendorRating(vendorId));
    }

    @Test
    void testGetPopularDishOneDish() {
        Long vendorId = 1L;

        Dish dish = new Dish().id(1L).name("fries");

        Order order = new Order().orderId(11L).dishes(Collections.singletonList(dish));
        List<Order> orders = List.of(order);

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Dish popular = analyticsVendorService.getPopularDish(vendorId);

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

        Dish popular = analyticsVendorService.getPopularDish(vendorId);

        assertNotNull(popular);
        assertEquals(dish4.getName(), popular.getName());
    }

    @Test
    void testGetPopularDishNull() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(null);

        Dish popular = analyticsVendorService.getPopularDish(vendorId);

        assertEquals(null, popular);

    }

    @Test
    void testAverageOrdersPerDayOneOrder() {
        OffsetDateTime now = OffsetDateTime.now();

        Order order = new Order().orderId(11L).time(now);
        List<Order> orders = List.of(order);
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(orders);

        Double average = analyticsVendorService.averageOrdersPerDay(vendorId);

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

        Double average = analyticsVendorService.averageOrdersPerDay(vendorId);

        assertEquals(expected, average);
    }

    @Test
    void testAverageOrdersPerDayNull() {
        Long vendorId = 1L;

        Mockito.when(orderService.getAllOrdersForVendor(vendorId)).thenReturn(null);

        Double average = analyticsVendorService.averageOrdersPerDay(vendorId);

        assertEquals(null, average);
    }

}
