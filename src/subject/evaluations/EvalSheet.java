package subject.evaluations;

import others.Statistic;
import subject.Student;
import subject.Subject;
import subject.exceptions.AlreadyEvaluatedException;

import java.util.Iterator;

public interface EvalSheet {
    String evaluationId();
    Subject subject();
    String description();

    Statistic statistic();
    boolean isClosed();// never used think about this
    void close(); // think about this
    // think about this later :>
    void evaluate(Student student, EvalEntry eval); //throws AlreadyEvaluatedException;

    Iterator<EvalEntry> listByAlphabeticalOrder();

    Iterator<EvalEntry> ranking();
}
