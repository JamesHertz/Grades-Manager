package subject.evaluations;

import others.Statistic;
import subject.Student;
import subject.Subject;
import subject.exceptions.AlreadyEvaluatedException;

import java.util.Iterator;

public interface EvalSheet {
    String id();
    Subject subject();
    String description();

    Statistic statistic();
    boolean isClosed();// never used think about this
    void close(); // think about this
    void evaluate(Student student, float grade) throws AlreadyEvaluatedException, AlreadyEvaluatedException;

    Iterator<EvalEntry> listByAlphabeticalOrder();

    Iterator<EvalEntry> ranking();
}
