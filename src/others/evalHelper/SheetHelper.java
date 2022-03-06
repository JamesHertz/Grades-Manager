package others.evalHelper;

import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.Iterator;

public interface SheetHelper extends Serializable {
    // remove
    String subject();
    // remove
    String evalId();
    boolean isEmpty();
    void clear();
    int size();
    void addEvalHelper(String name, int number, float grade) throws AlreadyEvaluatedException;
    void addEvalHelper(Student student, float grade) throws AlreadyEvaluatedException;
    Iterator<EvalHelper> listAllHelper();

}
