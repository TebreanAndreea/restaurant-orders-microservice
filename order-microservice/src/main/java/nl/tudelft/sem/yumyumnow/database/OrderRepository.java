package nl.tudelft.sem.yumyumnow.database;

import nl.tudelft.sem.yumyumnow.commons.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
