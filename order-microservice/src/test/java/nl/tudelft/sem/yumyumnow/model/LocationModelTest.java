package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationModelTest {

    private Location location;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.location = new Location();
    }

    @Test
    public void testId() {
        this.location.setId(12L);
        this.location.setLongitude(12.2);
        assertEquals(12.2, this.location.id(12L).getLongitude());
        assertEquals(this.location, this.location.id(12L));
    }
    
    @Test
    public void testGetAndSetId() {
        this.location.setId(12L);
        assertEquals(12L, this.location.getId());
    }

    @Test
    public void testLatitude() {
        this.location.setLatitude(12.3);
        this.location.setLongitude(12.2);
        assertEquals(12.2, this.location.latitude(12.3).getLongitude());
        assertEquals(this.location, this.location.latitude(12.3));
    }

    @Test
    public void testGetAndSetLatitude() {
        this.location.setLatitude(12.3);
        assertEquals(12.3, this.location.getLatitude());
    }

    @Test
    public void testLongitude() {
        this.location.setLongitude(12.3);
        this.location.setLatitude(12.2);
        assertEquals(12.2, this.location.longitude(12.3).getLatitude());
        assertEquals(this.location, this.location.longitude(12.3));
    }

    @Test
    public void testGetAndSetLongitude() {
        this.location.setLongitude(12.3);
        assertEquals(12.3, this.location.getLongitude());
    }

    @Test
    public void testEqualsSameObject() {
        assertEquals(true, this.location.equals(this.location));
    }

    @Test
    public void testEquals() {
        Location location2 = new Location();

        location2.setLongitude(12.1);
        location2.setId(12L);
        this.location.setLongitude(12.1);
        this.location.setId(12L);
        assertEquals(true, this.location.equals(location2));
    }

    @Test
    public void testNotEqualsNull() {
        assertEquals(false, this.location.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        assertEquals(false, this.location.equals(new Object()));
    }

    @Test
    public void testNotEquals1() {
        Location location2 = new Location();

        location2.setId(3L);
        location2.setLongitude(12.2);
        this.location.setId(2L);
        this.location.setLongitude(12.2);
        assertEquals(false, this.location.equals(location2));
    }

    @Test
    public void testNotEquals2() {
        Location location2 = new Location();

        location2.setId(3L);
        location2.setLongitude(23.0);
        this.location.setId(3L);
        this.location.setLongitude(231.0);
        assertEquals(false, this.location.equals(location2));
    }

    @Test
    public void testNotEquals3() {
        Location location2 = new Location();

        location2.setId(3L);
        location2.setLatitude(23.0);
        this.location.setId(3L);
        this.location.setLatitude(231.0);
        assertEquals(false, this.location.equals(location2));
    }


    @Test
    public void testHashcode() {
        Location location2 = new Location();
        location2.setId(2L);
        location2.setLatitude(12.2);
        location2.setId(2L);
        location2.setLatitude(12.1);
        this.location.setId(2L);
        this.location.setLatitude(12.1);
        assertEquals(this.location.hashCode(), location2.hashCode());
        assertEquals(this.location, location2);
        assertEquals(false, this.location.hashCode() + 1 == location2.hashCode());
        Location location3 = new Location();
        assertNotEquals(location3, location2);
        assertNotEquals(location3.hashCode(), location2.hashCode());
        assertNotEquals(this.location.hashCode(), location3.hashCode());
    }

    @Test
    public void testToString() {
        this.location.setId(2L);
        this.location.setLongitude(12.2);
        assertEquals("class Location {\n"
                + "    id: 2\n"
                + "    latitude: null\n"
                + "    longitude: 12.2\n"
                + "}", this.location.toString());
    }

    @Test
    public void testConstructor() {
        Location location = new Location(12L, 12.2, 12.1);
        assertEquals(location.getLatitude(), 12.2);
        assertEquals(location.getId(), 12L);
        assertEquals(location.getLongitude(), 12.1);
    }
}
