package nl.tudelft.sem.yumyumnow.database;


import nl.tudelft.sem.yumyumnow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
