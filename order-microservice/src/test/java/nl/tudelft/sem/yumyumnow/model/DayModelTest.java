package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DayModelTest {

    private Day day;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.day = new Day();
    }

    @Test
    public void testOpen() {
        this.day.setOpen("12H");
        this.day.setClosed("Closed");
        assertEquals("Closed", this.day.open("12H").getClosed());
        assertEquals(this.day, this.day.open("12H"));
    }

    @Test
    public void testGetOpen() {
        this.day.setOpen("12H");
        this.day.setClosed("Closed");
        assertEquals("12H", this.day.getOpen());
    }

    @Test
    public void testClosed() {
        this.day.setClosed("12H");
        this.day.setOpen("Closed");
        assertEquals("Closed", this.day.closed("12H").getOpen());
        assertEquals(this.day, this.day.closed("12H"));
    }

    @Test
    public void testGetClosed() {
        this.day.setClosed("12H");
        this.day.setOpen("Closed");
        assertEquals("12H", this.day.getClosed());
    }

    @Test
    public void testEqualsSameObject() {
        this.day.setClosed("3H");
        this.day.setOpen("12H");
        assertEquals(true, this.day.equals(this.day));
    }

    @Test
    public void testEquals() {
        Day day2 = new Day();
        day2.setClosed("3H");
        day2.setOpen("12H");
        this.day.setClosed("3H");
        this.day.setOpen("12H");
        assertEquals(true, this.day.equals(day2));
    }

    @Test
    public void testNotEqualsNull() {
        Day day2 = new Day();

        day2.setClosed("32H");
        day2.setOpen("12H");
        this.day.setClosed("3H");
        this.day.setOpen("12H");
        assertEquals(false, this.day.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        this.day.setClosed("3H");
        this.day.setOpen("12H");
        assertEquals(false, this.day.equals(new Object()));
    }

    @Test
    public void testNotEquals() {
        Day day2 = new Day();

        day2.setClosed("32H");
        day2.setOpen("12H");
        this.day.setClosed("3H");
        this.day.setOpen("12H");
        assertEquals(false, this.day.equals(day2));
    }

    @Test
    public void testHashcode() {
        Day day2 = new Day();
        day2.setClosed("Closed");
        day2.setOpen("su4rClosed");
        day2.setClosed("Closed");
        day2.setOpen("Open");
        this.day.setClosed("Closed");
        this.day.setOpen("Open");
        assertEquals(this.day.hashCode(), day2.hashCode());
        assertEquals(this.day, day2);
        assertEquals(false, this.day.hashCode() + 1 == day2.hashCode());
        Day day3 = new Day();
        assertNotEquals(day3, day2);
        assertNotEquals(day3.hashCode(), day2.hashCode());
        assertNotEquals(this.day.hashCode(), day3.hashCode());
    }

    @Test
    public void testToString() {
        this.day.setOpen("he");
        this.day.setClosed("email");
        assertEquals("class Day {\n"
                + "    open: he\n"
                + "    closed: email\n"
                + "}", this.day.toString());
    }

    @Test
    public void testConstructor() {
        Day day2 = new Day("surname", "email");
        assertEquals(day2.getOpen(), "surname");
        assertEquals(day2.getClosed(), "email");
    }

}
