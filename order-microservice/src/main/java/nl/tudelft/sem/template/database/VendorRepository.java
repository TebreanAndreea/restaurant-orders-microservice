package nl.tudelft.sem.template.database;

import nl.tudelft.sem.template.commons.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Long> {
}
