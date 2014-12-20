package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("Student")
@Table(name = "STUDENTS")
public class Student extends RoleSchool {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "SEMESTER", nullable = false)
    private String semester;

    public Student() {
    }

    public Student(String semester) {
        super("Student");
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Student{ id=" + getId() +" name: " + getRole() +" semester=" + semester + '}';
    }
    
    

}
