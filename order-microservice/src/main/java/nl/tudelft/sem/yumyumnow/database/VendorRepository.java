package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
