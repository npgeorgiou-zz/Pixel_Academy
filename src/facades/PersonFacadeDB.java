/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.NotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import model.Person;
import model.RoleSchool;
import model.Teacher;
import model.TeacherAssistant;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;

/**
 *
 * @author ksptsinplanet
 */
public class PersonFacadeDB implements IPersonFacade {

    private Gson gson = new Gson();
    private String persistenceFileName = "PIXEL_ACADEMYPU";
    SimpleDateFormat dateFormatterForJackson = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a");
    ObjectMapper om = new ObjectMapper().setDateFormat(dateFormatterForJackson);

    @Override
    public Person addPerson(String json) {
        //make person from Json
        Person p = gson.fromJson(json, Person.class);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceFileName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(p);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Person delete(Integer id) throws NotFoundException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceFileName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Person p = em.find(Person.class, id);

        if (p == null) {
            transaction.rollback();
            throw new NotFoundException("No person for the given id");
        } else {
            em.remove(p);
            transaction.commit();
        }

        return p;
    }

    @Override
    public String getPerson(Integer id) throws NotFoundException {
        String result = "";
        //get person with this id from DB
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceFileName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Person p = em.find(Person.class, id);

            result = om.writeValueAsString(p);
//            Query query = em.createNamedQuery("Person.findById").setParameter("id", id);
//            List<Person> people = query.getResultList();

            //result = gson.toJson(people.get(0));
            //result = om.writeValueAsString(people.get(0));
        } catch (Exception e) {
            throw new NotFoundException("No person exists for the given id");
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public String getPersons() {

        String result = "";
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceFileName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Query query = em.createNamedQuery("Person.findAll");
            List<Person> people = query.getResultList();

            try {
                result = om.writeValueAsString(people);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(PersonFacadeDB.class.getName()).log(Level.SEVERE, null, ex);
            }

        } finally {
            em.close();
        }
        return result;
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
        //save this info
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceFileName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(role);
            transaction.commit();
            em.getEntityManagerFactory().getCache().evictAll();
        } catch (Exception e) {
            throw new NotFoundException("Couldnt add role");
        } finally {
            em.close();
        }

        return role;

    }
}
