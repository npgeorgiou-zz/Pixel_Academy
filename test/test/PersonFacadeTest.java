package test;

import com.google.gson.Gson;
import model.Person;
import exceptions.NotFoundException;
import facades.PersonFacadeDB;
import java.util.ArrayList;
import java.util.List;
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
public class PersonFacadeTest {

    static PersonFacadeDB facade;
    static Gson gson = new Gson();
    static Person person;
    static Person person2;
    static Person person3;
    static List<Person> list;

    public PersonFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        facade = new PersonFacadeDB();
        list = new ArrayList();

        person = facade.addPerson(gson.toJson(new Person("aaaaaaaaaaa", "a", "123345", "aa@aaa.com")));
        person2 = facade.addPerson(gson.toJson(new Person("bbbbbbbbb", "b", "222314", "nk@np.com")));
        person3 = facade.addPerson(gson.toJson(new Person("ccccccccc", "c", "4262134", "sven666@gmail.com")));
        list.add(person);
        list.add(person2);
        list.add(person3);
        System.out.println(person.toString());
        System.out.println(person2.toString());
        System.out.println(person3.toString());

    }

    @AfterClass
    public static void tearDownClass() throws NotFoundException {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() throws NotFoundException {
    }

    @Test
    public void addRole() throws NotFoundException {
        // this method checks if peron with id 102 has two roles. 
        // we have a String in gson format, we check it this strings contains two roles, Student and Teacher Assistant
        // if contains, assert true.
        
        String jj = "{\"semester\":\"3\",\"roleName\":\"Student\"}";
        RoleSchool r = facade.addRole(jj, person.getId());

        assertEquals(r.getRole(), "Student");

        String gg = "{\"date\":\"2010-10-10\",\"degree\":\"MA\",\"roleName\":\"Teacher\"}";
        RoleSchool r2 = facade.addRole(gg, person2.getId());
        assertEquals(r2.getRole(), "Teacher");

        int n = 71854689;

        String ll = "{\"degree\":\"MA\",\"roleName\":\"Teacher Assistant\"}";
        String bb = "{\"semester\":\"2\",\"roleName\":\"Student\"}";
        facade.addRole(ll, person3.getId());
        facade.addRole(bb, person3.getId());

        String s = gson.toJson(facade.getPerson(102));
        boolean sresult = s.contains("Student");
        boolean tresult = s.contains("Teacher Assistant");
        
        assertTrue(sresult);
        assertTrue(tresult);

    }

    @Test
    public void testGetPerson() throws NotFoundException {
        
        //creates a gson string of a person with id person.getID();
        // if output from the db is the same as person assert equal
        String expected = gson.toJson(person);
        String real = facade.getPerson(person.getId());

        assertEquals("Expected person with id = " + expected + " || Real id is = " + real, expected, real);
    }

    @Test
    public void testAddPerson() throws NotFoundException {
        // creates new person (person4), adds it to the gson, 
        // checks if the gson is the same as output from the db
        // if so, assert equal
        // after assertion, delete created person
        Person person4 = facade.addPerson(gson.toJson(new Person("bbb", "bbb", "bbb", "bbb")));

        String expectedJsonString = gson.toJson(person4);
        String actual = facade.getPerson(person4.getId());
        assertEquals(expectedJsonString, actual);
        facade.delete(person4.getId());

    }

    @Test
    public void testGetPersons() {
//         creates a gson list
//         compares gson with the gson from the facade.getPersons(); 
//         success if gsons are equal
        
        String expected = gson.toJson(list);
        String real = facade.getPersons();

        assertEquals(expected, real);
    }
    @Test
    public void testDeletePerson() throws NotFoundException {
        // deletes created person
        // asserts not null after 
        Person person5 = facade.addPerson(gson.toJson(new Person("ToBeDeleted", "TTT", "TTT", "TTT")));
        int id = person5.getId();
        String person5gson = gson.toJson(person5);
     
        facade.delete(id);
        String listOfPeople = facade.getPersons();
        
        assertFalse(listOfPeople.contains(person5gson));
        
    }
    @Test
    public void testFailGetPersons() throws NotFoundException {
        // creates gson list
        // adds new person
        // gets gson from the getPersons();
        // comapres length of gsons

        Person test = facade.addPerson(gson.toJson(new Person("ToBeDeleted", "TTT", "TTT", "TTT")));
        String expected = facade.getPersons();

        facade.delete(test.getId());
        String real = facade.getPersons();
        int r = expected.length();

        int lengthFake = real.length();

        assertNotSame(r, lengthFake);

    }

    @Test(expected = NotFoundException.class)
    public void testFailGetPerson() throws NotFoundException {
        Person test = facade.addPerson(gson.toJson(new Person("ToBeDeleted", "TTT", "TTT", "TTT")));
        String expected = gson.toJson(person);
        facade.delete(test.getId());
        String real = facade.getPerson(test.getId());   
    }
    
    @Test(expected = NotFoundException.class)
    public void testFailDelete() throws NotFoundException{
        facade.delete(12512512);
    }
   @Test
    public void testFailAddPerson() {
        // basically, this method can not crash, whatever user passes is a String (numbers in name ...)
        // we should have implemented users input controll

//        Person test = facade.addPerson(gson.toJson(new Person("1111", "222", "TTT", "TTT")));
//        String name = test.getFirstName();
//        String lname = test.getLastName();
//        String phone = test.getPhone();
//        String mail = test.geteMail();
//        
//        
//         assertTrue(name.matches(".*\\d.*")); // if name contains digits should have been implemented in the program as an input controll 
//         assertTrue(lname.matches(".*\\d.*")); // if lnam contains digits should have been implemented in the program as an input controll
    }
}
