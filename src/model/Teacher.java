package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;

@Entity
@DiscriminatorValue("Teacher")
@Table(name = "TEACHERS")
public class Teacher extends RoleSchool {

    private static final long serialVersionUID = 1L;

    @Column(name = "HIRING_DATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date hiringDate;

    @Column(name = "DEGREE", nullable = false)
    private String degree;

    public Teacher() {
    }

    public Teacher(Date date, String degree) {
        super("Teacher");
        this.hiringDate = date;
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Date getDate() {
        return hiringDate;
    }

    public void setDate(Date date) {
        this.hiringDate = date;
    }

    @Override
    public String toString() {
        return "Teacher{ id=" + getId() + "name: " + getRole() + "degree=" + degree + '}';
    }

}
