package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.DishCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishCopy, Long> {
}
