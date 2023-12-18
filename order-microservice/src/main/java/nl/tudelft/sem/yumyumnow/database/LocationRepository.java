package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
