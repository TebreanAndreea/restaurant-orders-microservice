package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AnalyticsServiceTest {

    private TestRatingRepository ratingRepository;
    private AnalyticsService analyticsService;

    @BeforeEach
    public void setup() {
        this.ratingRepository = new TestRatingRepository();
        this.analyticsService = new AnalyticsService(ratingRepository);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 420L;
        assertEquals(this.analyticsService.getRatingById(id), Optional.empty());

        assertEquals(1, this.ratingRepository.getMethodCalls().size());
        assertEquals("findById", this.ratingRepository.getMethodCalls().get(0));
    }

    @Test
    public void testAddDish() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.ratingRepository.getMethodCalls().size());
        assertEquals("save", this.ratingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
    }

    @Test
    public void testGetRatingById() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.ratingRepository.getMethodCalls().size());
        assertEquals("save", this.ratingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(2, this.ratingRepository.getMethodCalls().size());
        assertEquals("findById", this.ratingRepository.getMethodCalls().get(1));
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
    }
}
