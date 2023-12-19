package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.subOpeningTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningTimesRepository extends JpaRepository<subOpeningTimes, Long> {
}
