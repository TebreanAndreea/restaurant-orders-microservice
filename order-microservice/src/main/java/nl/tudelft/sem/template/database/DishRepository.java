package nl.tudelft.sem.template.database;

import nl.tudelft.sem.template.commons.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, String> {
}
