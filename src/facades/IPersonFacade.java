package facades;

import model.Person;
import model.RoleSchool;
import exceptions.NotFoundException;

public interface IPersonFacade {

    public String getPersons();

    public String getPerson(Integer id) throws NotFoundException;

    public Person addPerson(String json) throws NotFoundException;

    public RoleSchool addRole(String json, Integer id) throws NotFoundException;

    public Person delete(Integer id) throws NotFoundException;
}
