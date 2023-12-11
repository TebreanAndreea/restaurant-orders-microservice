package nl.tudelft.sem.template.commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.tudelft.sem.template.model.User;

@Entity
@Table(name = "UserTable")
public class UserEntity extends User {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }
}
