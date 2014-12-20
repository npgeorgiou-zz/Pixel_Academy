package test;

import com.google.gson.Gson;
import exceptions.NotFoundException;
import facades.FacadeNoDB;
import java.util.HashMap;
import java.util.Map;
import model.Person;
import model.RoleSchool;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Artur
 */
public class TestFacadeInIsolation {

    public static FacadeNoDB facade;
    static Gson gson = new Gson();
    static Map<Integer, Person> persons = new HashMap();
    private static Person p1;
    private static Person p2;
    private static Person p3;

    public TestFacadeInIsolation() {
    }

    @BeforeClass
    public static void setUpClass() {
        facade = new FacadeNoDB();
        p1 = facade.addPerson(gson.toJson(new Person(1, "Artur", "Koziel", "1234", "email@e.gm")));
        p2 = facade.addPerson(gson.toJson(new Person(2, "Nikos", "P", "4312", "nnnnnooooooo@e.gm")));
        p3 = facade.addPerson(gson.toJson(new Person(3, "Sven", "H", "6543", "yessssss@e.gm")));
        persons.put(p1.getId(), p1);
        persons.put(p2.getId(), p2);
        persons.put(p3.getId(), p3);

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddPerson() {
        int p1ID = p1.getId();
        int expectedIDPerson1 = 0;

        int p2ID = p2.getId();
        int expectedIDPerson2 = 1;

        int p3ID = p3.getId();
        int expectedIDPerson3 = 2;

        assertEquals(expectedIDPerson1, p1ID);
        assertEquals(expectedIDPerson2, p2ID);
        assertEquals(expectedIDPerson3, p3ID);

    }

    @Test
    public void testGetPerson() throws NotFoundException {

        String expectedpp2 = facade.getPerson(2);
        String pp3 = gson.toJson(p3);

        assertEquals(expectedpp2, pp3);
    }

    @Test
    public void testGetPersons() {

        String jstring = facade.getPersons();
        String jtest = gson.toJson(persons.values());

        assertEquals(jstring, jtest);
    }

    @Test
    public void testDelete() throws NotFoundException {
        Person pp = facade.addPerson(gson.toJson(new Person(3, "Sven", "H", "6543", "yessssss@e.gm")));

        facade.delete(pp.getId());
        
        String ppgson = gson.toJson(pp);
        
        String listOfPeople = facade.getPersons();
        assertFalse(listOfPeople.contains(ppgson));
    }

    @Test
    public void testAddRole() throws NotFoundException {
        String jj = "{\"semester\":\"3\",\"roleName\":\"Student\"}";
        RoleSchool r = facade.addRole(jj, 0);
        assertEquals(r.getRole(), "Student");

        String gg = "{\"date\":\"2010-10-10\",\"degree\":\"MA\",\"roleName\":\"Teacher\"}";
        RoleSchool r2 = facade.addRole(gg, 1);
        assertEquals(r2.getRole(), "Teacher");

        String ll = "{\"degree\":\"MA\",\"roleName\":\"Teacher Assistant\"}";
        String st = "{\"degree\":\"MA\",\"roleName\":\"Student\"}";
        RoleSchool r3 = facade.addRole(ll, 2);
        RoleSchool r4 = facade.addRole(st, 2);
        assertEquals(r3.getRole(), "Teacher Assistant");
        assertEquals(r4.getRole(), "Student");
        System.out.println(gson.toJson(facade.getPerson(2)));
        System.out.println(gson.toJson(r3));
        System.out.println(gson.toJson(r4));

    }

    @Test(expected = NotFoundException.class)
    public void testDeleteWithWrongID() throws NotFoundException {
        facade.delete(12414124);
    }

    @Test(expected = NotFoundException.class)
    public void testGetPersonThatDoesNotExist() throws NotFoundException {
        Person pp = facade.addPerson(gson.toJson(new Person(3, "Sven", "H", "6543", "yessssss@e.gm")));
        facade.delete(pp.getId());
        facade.getPerson(pp.getId());

    }

    @Test
    public void testGetPersonsWhenThereAreNoPeople() throws NotFoundException {

    }
}
