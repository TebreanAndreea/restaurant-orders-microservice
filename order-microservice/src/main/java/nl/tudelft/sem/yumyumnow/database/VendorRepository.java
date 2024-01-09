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
        + "AND FUNCTION('withinRadius', :location, :radius, v.location) = true")
    List<Vendor> findByLocationWithinRadius(@Param("location") Location location, @Param("filter") String filter,
        @Param("radius") Integer radius);

    /**
     * Checks if the given address is in the given radius of the vendor's address.
     *
     * @param address The address to check.
     * @param radius The radius to check.
     * @param vendorAddress The vendor's address.
     * @return True if the address is in the radius, false otherwise.
     */
    default boolean withinRadius(Location address, Integer radius, Location vendorAddress) {
        if (address == null || vendorAddress == null || radius == null || radius < 0) {
            return false;
        }
        return Math.acos(Math.sin(address.getLatitude()) * Math.sin(vendorAddress.getLatitude())
            + Math.cos(address.getLatitude()) * Math.cos(vendorAddress.getLatitude())
            * Math.cos(address.getLongitude() - vendorAddress.getLongitude())) * 6371 <= (double) radius / 1000.0;
    }

}
