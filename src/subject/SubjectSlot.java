package subject;

import others.Statistic;
import subject.evaluations.EvalEntry;

import java.util.Iterator;

public interface SubjectSlot{
    String subjectId();
    Student student();
    void addEvaluations(EvalEntry eval);
    Iterator<EvalEntry> listAllEvaluations();
    Statistic statistic();
}
