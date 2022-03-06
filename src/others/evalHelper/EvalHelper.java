package others.evalHelper;

import subject.Student;

import java.io.Serializable;

public interface EvalHelper extends Serializable {
   // remove
    String name();
    // remove
    int number();
    Student getStudent();
    float getGrade();
}
