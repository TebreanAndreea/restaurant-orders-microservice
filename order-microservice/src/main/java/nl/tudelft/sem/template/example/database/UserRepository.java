package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.commons.UserCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserCopy, Long> {
}
