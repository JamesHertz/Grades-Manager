package others.evalHelper;

import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.util.Iterator;

public interface SheetHelper {
    String subject();
    String evalId();
    int getNumberOfEvalHelper();
    void addEvalHelper(String name, int number, float grade) throws AlreadyEvaluatedException;
    Iterator<EvalHelper> listAllHelper();

}
