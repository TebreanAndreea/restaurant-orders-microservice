package nl.tudelft.sem.yumyumnow.services;

import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.RatingRepository;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final RatingRepository ratingRepository;

    @Autowired
    public AnalyticsService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Optional<Rating> getRatingById(Long ratingId) {
        return this.ratingRepository.findById(ratingId);
    }

    public Rating createNewRating(Rating rating) {
        return this.ratingRepository.save(rating);
    }
}
