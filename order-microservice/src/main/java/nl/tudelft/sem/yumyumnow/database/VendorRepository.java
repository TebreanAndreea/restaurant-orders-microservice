package nl.tudelft.sem.yumyumnow.database;

import java.util.List;
import nl.tudelft.sem.yumyumnow.model.Location;
import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    @Query("SELECT v FROM Vendor v WHERE v.name LIKE %:filter%")
    List<Vendor> findByVendorNameContaining(@Param("filter") String filter);

    @Query("SELECT v FROM Vendor v WHERE v.name LIKE %:filter% "
        + "AND FUNCTION('withinRadius', v.location, :location, :radius) = true")
    List<Vendor> findByLocationWithinRadius(@Param("location") Location location, @Param("filter") String filter,
        @Param("radius") Integer radius);
}
