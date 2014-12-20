package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("Teacher Assistant")
@Table(name = "ASSISTANT_TEACHER")
public class TeacherAssistant extends RoleSchool {
    private static final long serialVersionUID = 1L;

    
    
    
    public TeacherAssistant() {
         super("Teacher Assistant");
    }

    @Override
    public String toString() {
        return "Teacher Assistant{ id="+getId()+" name:"+ getRole() + '}';
    }
    
}
