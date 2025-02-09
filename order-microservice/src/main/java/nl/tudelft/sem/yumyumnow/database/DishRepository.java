package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}
