package nl.tudelft.sem.template.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<nl.tudelft.sem.template.example.commons.VendorEntity, Long> {
}
