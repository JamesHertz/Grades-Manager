package others.evalHelper;

import others.Statistic;
import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.Iterator;

public interface SheetHelper extends Serializable {
    boolean isEmpty();
    void clear();
    int size();
    void addEvalHelper(Student student, float grade) throws AlreadyEvaluatedException;
    Iterator<EvalHelper> listAllHelper();

}
