package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.CustomerCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerCopy, Long> {
}
