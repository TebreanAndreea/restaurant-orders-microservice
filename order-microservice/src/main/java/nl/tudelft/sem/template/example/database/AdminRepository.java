package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.AdminCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminCopy, Long> {
}
