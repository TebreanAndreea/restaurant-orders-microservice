package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.VendorCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorCopy, Long> {
}
