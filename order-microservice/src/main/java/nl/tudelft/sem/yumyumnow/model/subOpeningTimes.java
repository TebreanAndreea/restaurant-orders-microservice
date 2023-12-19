package nl.tudelft.sem.yumyumnow.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "opening_times")
public class subOpeningTimes extends OpeningTimes {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    /**
     * Empty constructor for mapper.
     */
    public subOpeningTimes() {
    }

    /**
     * @param monday opening / closing times on mondays
     * @param tuesday opening / closing times on tuesdays
     * @param wednesday opening / closing times on wednesdays
     * @param thursday opening / closing times on thursdays
     * @param friday opening / closing times on fridays
     * @param saturday opening / closing times on saturdays
     * @param sunday opening / closing times on sundays
     */
    public subOpeningTimes(Day monday, Day tuesday, Day wednesday, Day thursday, Day friday, Day saturday, Day sunday) {
        super(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof subOpeningTimes)) return false;
        if (!super.equals(o)) return false;
        subOpeningTimes that = (subOpeningTimes) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

}
