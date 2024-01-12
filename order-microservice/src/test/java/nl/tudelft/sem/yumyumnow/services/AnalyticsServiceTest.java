package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Dish;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnalyticsServiceTest {

    private TestRatingRepository testRatingRepository;
    private AnalyticsService analyticsService;
    private VendorService vendorService;
    private OrderService orderService;

    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.vendorService = Mockito.mock(VendorService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.testRatingRepository = new TestRatingRepository();
        this.analyticsService = new AnalyticsService(vendorService, testRatingRepository, orderService);
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
}

