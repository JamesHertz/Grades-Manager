package others.evalHelper;

import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.util.Iterator;

public interface SheetHelper {
    // remove
    String subject();
    // remove
    String evalId();
    int size();
    void addEvalHelper(String name, int number, float grade) throws AlreadyEvaluatedException;
    void addEvalHelper(Student student, float grade) throws AlreadyEvaluatedException;
    Iterator<EvalHelper> listAllHelper();

}
