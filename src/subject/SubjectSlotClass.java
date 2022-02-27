package subject;

import others.Statistic;
import others.StatisticClass;
import subject.evaluations.EvalEntry;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SubjectSlotClass implements SubjectSlot, Serializable{
    private static final long serialVersionUID = 0L;
    private final Subject subject;
    private final List<EvalEntry> evaluations;
    private final Statistic statistic; // think about this also
    private final Student student;
    public SubjectSlotClass(Student student, Subject subject){
        this.student = student;
        this.subject = subject;
        evaluations = new LinkedList<>();
        statistic = new StatisticClass();
    }

    @Override
    public String subjectId() {
        return subject.id();
    }

    @Override
    public Student student() {
        return student;
    }

    @Override
    public void addEvaluations(EvalEntry eval) {
        evaluations.add(eval);
        statistic.addData(eval.grade());
    }

    @Override
    public Iterator<EvalEntry> listAllEvaluations() {
        return evaluations.iterator();
    }

    @Override
    public Statistic statistic() {
        return statistic;
    }
}
