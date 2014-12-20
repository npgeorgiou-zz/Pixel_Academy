package facades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.NotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.Person;
import model.RoleSchool;
import model.Student;
import model.Teacher;
import model.TeacherAssistant;

/**
 *
 * @author Artur
 */
public class FacadeNoDB implements IPersonFacade {

    Map<Integer, Person> persons = new HashMap();
    private int nextId;
    private final Gson gson = new Gson();
    private static FacadeNoDB instance = new FacadeNoDB();

    public FacadeNoDB() {
    }

    public static FacadeNoDB getFacade(boolean reseet) {
        if (true) {
            instance = new FacadeNoDB();
        }
        return instance;
    }

    @Override
    public Person addPerson(String json) {
        Person p = gson.fromJson(json, Person.class);
        p.setId(nextId);
        persons.put(nextId, p);
        nextId++;
        return p;
    }

    @Override
    public Person delete(Integer id) throws NotFoundException {
        Person p = persons.remove(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return p;
    }

    @Override
    public String getPerson(Integer id) throws NotFoundException {
        Person p = persons.get(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return gson.toJson(p);
    }

    @Override
    public String getPersons() {
        if (persons.isEmpty()) {
            return null;
        }
        return gson.toJson(persons.values());
    }

    @Override
    public RoleSchool addRole(String json, Integer id) throws NotFoundException {
        Person p = gson.fromJson(getPerson(id), Person.class);
        HashMap<String, String> map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());
        String roleName = map.get("roleName");
        RoleSchool role;
        switch (roleName) {
            case "Teacher Assistant":
                //Create role
                RoleSchool ta = new TeacherAssistant();
                ta.setPerson(p);
                role = ta;
                break;
            case "Teacher":
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                String dateInString = map.get("date");
                Date date = new Date();

                try {
                    date = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace(System.out);
                }
                RoleSchool t = new Teacher(date, map.get("degree"));
                t.setPerson(p);
                role = t;
                break;
            case "Student":
                RoleSchool s = new Student(map.get("semester"));
                s.setPerson(p);
                role = s;
                break;
            default:
                throw new IllegalArgumentException("no such role");
        }

        return role;
    }
}
