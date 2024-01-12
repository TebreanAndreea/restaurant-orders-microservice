package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.RatingRepository;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class AnalyticsServiceTest {

    private TestRatingRepository testRatingRepository;
    private RatingRepository ratingRepository;
    private AnalyticsService analyticsService;
    private AnalyticsService mockedAnalyticsService;
    private VendorService vendorService;
    private OrderService orderService;

    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.vendorService = Mockito.mock(VendorService.class);
        this.ratingRepository = new TestRatingRepository();
        this.orderService = Mockito.mock(OrderService.class);
        this.testRatingRepository = new TestRatingRepository();
        this.analyticsService = new AnalyticsService(vendorService, testRatingRepository, orderService);
        this.mockedAnalyticsService = new AnalyticsService(vendorService, ratingRepository, orderService);
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

        assertEquals(average, mockedAnalyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceOneDish() {
        Long vendorId = 1L;
        Dish dish = new Dish().id(2L).price(23.4);
        List<Dish> dishes = Arrays.asList(dish);

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(dishes);

        assertEquals(23.4, mockedAnalyticsService.getAverageVendorPrice(vendorId));
    }

    @Test
    void testGetAverageVendorPriceNoDishes() {
        Long vendorId = 1L;

        Mockito.when(vendorService.getVendorDishes(vendorId)).thenReturn(Collections.emptyList());

        assertEquals(null, mockedAnalyticsService.getAverageVendorPrice(vendorId));
    }
}

