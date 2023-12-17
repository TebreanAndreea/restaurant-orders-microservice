package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
