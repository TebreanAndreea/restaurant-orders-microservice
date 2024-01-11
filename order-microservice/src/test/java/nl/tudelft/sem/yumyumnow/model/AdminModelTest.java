package nl.tudelft.sem.yumyumnow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.sem.yumyumnow.services.AuthenticationService;
import nl.tudelft.sem.yumyumnow.services.IntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class AdminModelTest {

    private Admin admin;


    /**
     * Setup of the services needed for testing.
     */
    @BeforeEach
    public void setup() {
        this.admin = new Admin();
    }

    @Test
    public void testId() {
        this.admin.setId(12L);
        this.admin.setEmail("email");
        assertEquals("email", this.admin.id(12L).getEmail());
        assertEquals(this.admin, this.admin.id(12L));
    }

    @Test
    public void testGetId() {
        this.admin.setId(12L);
        this.admin.setEmail("email");
        assertEquals(12L, this.admin.getId());
    }

    @Test
    public void testGetAndSetId() {
        this.admin.setId(12L);
        this.admin.setEmail("email");
        assertEquals(12L, this.admin.getId());
    }

    @Test
    public void testName() {
        this.admin.setName("a");
        this.admin.setEmail("email");
        assertEquals("email", this.admin.name("a").getEmail());
        assertEquals(this.admin, this.admin.name("a"));
    }

    @Test
    public void testGetAndSetName() {
        this.admin.setName("name");
        this.admin.setEmail("email");
        assertEquals("name", this.admin.getName());
    }

    @Test
    public void testSurname() {
        this.admin.setSurname("a");
        this.admin.setEmail("email");
        assertEquals("email", this.admin.surname("a").getEmail());
        assertEquals(this.admin, this.admin.surname("a"));
    }

    @Test
    public void testGetAndSetSurname() {
        this.admin.setSurname("Surname");
        this.admin.setEmail("email");
        assertEquals("Surname", this.admin.getSurname());
    }

    @Test
    public void testEmail() {
        this.admin.setEmail("a");
        this.admin.setName("Name");
        assertEquals("Name", this.admin.email("a").getName());
        assertEquals(this.admin, this.admin.email("a"));
    }

    @Test
    public void testGetAndSetEmail() {
        this.admin.setEmail("Email");
        this.admin.setName("Name");
        assertEquals("Email", this.admin.getEmail());
    }

    @Test
    public void testEqualsSameObject() {
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals(true, this.admin.equals(this.admin));
    }

    @Test
    public void testEquals() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setEmail("email");
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals(true, this.admin.equals(admin2));
    }

    @Test
    public void testNotEqualsNull() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setEmail("email2");
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals(false, this.admin.equals(null));
    }

    @Test
    public void testNotEqualsWrongClass() {
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals(false, this.admin.equals(new Object()));
    }

    @Test
    public void testNotEquals1() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setEmail("email2");
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals(false, this.admin.equals(admin2));
    }

    @Test
    public void testNotEquals2() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setEmail("email");
        this.admin.setId(1L);
        this.admin.setEmail("email");
        assertEquals(false, this.admin.equals(admin2));
    }

    @Test
    public void testNotEquals3() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setName("email2");
        this.admin.setId(2L);
        this.admin.setName("email");
        assertEquals(false, this.admin.equals(admin2));
    }

    @Test
    public void testNotEquals4() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setSurname("email2");
        this.admin.setId(2L);
        this.admin.setSurname("email");
        assertEquals(false, this.admin.equals(admin2));
    }

    @Test
    public void testHashcode() {
        Admin admin2 = new Admin();
        admin2.setName("name");
        admin2.setSurname("su4rname");
        admin2.setId(2L);
        admin2.setEmail("email");
        admin2.setName("name");
        admin2.setSurname("surname");
        admin2.setId(2L);
        admin2.setEmail("email");
        this.admin.setId(2L);
        this.admin.setEmail("email");
        this.admin.setName("name");
        this.admin.setSurname("surname");
        assertEquals(this.admin.hashCode(), admin2.hashCode());
        assertEquals(this.admin, admin2);
        assertEquals(false, this.admin.hashCode() + 1 == admin2.hashCode());
        Admin admin3 = new Admin();
        assertNotEquals(admin3, admin2);
        assertNotEquals(admin3.hashCode(), admin2.hashCode());
        assertNotEquals(this.admin.hashCode(), admin3.hashCode());
    }

    @Test
    public void testToString() {
        this.admin.setId(2L);
        this.admin.setEmail("email");
        assertEquals("class Admin {\n" + "    id: 2\n"
                + "    name: null\n" + "    surname: null\n" + "    email: email\n"
                +  "}", this.admin.toString());
    }

    @Test
    public void testConstructor() {
        Admin admin2 = new Admin(12L, "name", "surname", "email");
        assertEquals(admin2.getEmail(), "email");
        assertEquals(admin2.getId(), 12L);
        assertEquals(admin2.getName(), "name");
        assertEquals(admin2.getSurname(), "surname");
    }

}
