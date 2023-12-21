package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.SubOpeningTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningTimesRepository extends JpaRepository<SubOpeningTimes, Long> {
}
