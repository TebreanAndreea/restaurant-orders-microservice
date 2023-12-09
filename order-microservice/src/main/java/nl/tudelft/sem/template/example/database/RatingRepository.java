package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.RatingCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingCopy, Long> {
}
