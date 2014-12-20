/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sholmager
 */
@Entity
@Table(name = "PERSONS")
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p ORDER BY p.id ASC"),
    @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.findByFName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "Person.findByLName", query = "SELECT p FROM Person p WHERE p.lastName = :lastName"),
    @NamedQuery(name = "Person.findByPhone", query = "SELECT p FROM Person p WHERE p.phone = :phone"),
    @NamedQuery(name = "Person.deleteById", query = "DELETE FROM Person p WHERE p.id = :id")})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s1")
    @SequenceGenerator(name = "s1", sequenceName = "PERSON_SEQ",
            initialValue = 100, allocationSize = 1)
    private Integer id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PHONE", nullable = true)
    private String phone;

    @Column(name = "EMAIL", nullable = false)
    private String eMail;

    @JsonManagedReference
    @OneToMany(mappedBy = "person", cascade = CascadeType.REMOVE)
    private Collection<RoleSchool> rolesAtSchool;

    public Collection<RoleSchool> getRolesAtSchool() {
        return rolesAtSchool;
    }
///////////////////
    public void addRole(RoleSchool r) {
        this.rolesAtSchool.add(r);
    }
//////////////////////////
    public Person() {
    }

    public Person(String firstName, String lastName, String phone, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.eMail = eMail;
    }

    //Test in isolation
    public Person(Integer id, String firstName, String lastName, String phone, String eMail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.eMail = eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
        return "Person{" + "firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", id=" + id + '}';
    }

}
