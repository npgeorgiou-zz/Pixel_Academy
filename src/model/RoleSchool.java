/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;


import com.fasterxml.jackson.core.*;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ROLE_TYPES")
@Table(name = "ROLES")
//@XmlRootElement

public class RoleSchool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
@JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "r1")
    @SequenceGenerator(name = "r1", sequenceName = "Role_SEQ",
            initialValue = 1000, allocationSize = 1)

    private Integer id;
    private String roleAtSchool;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    public RoleSchool() {
    }

    public RoleSchool(String name) {
        roleAtSchool = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return roleAtSchool;
    }

    public void setRole(String role) {
        this.roleAtSchool = role;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "RoleSchool{" + "id=" + id + ", roleName=" + roleAtSchool + '}';
    }

}
