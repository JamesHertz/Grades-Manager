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
    private final Subject sub;
    private final List<EvalEntry> evaluations;
    private final Statistic statistic; // think about this also
    private final Student student;
    private int finalGrade;
    public SubjectSlotClass(Student student, Subject subject){
        this.student = student;
        this.sub = subject;
        finalGrade = 0;
        evaluations = new LinkedList<>();
        statistic = new StatisticClass();
    }

    @Override
    public int getFinalGrade() {
        return finalGrade;
    }

    @Override
    public int getSubEcts() {
        return sub.getEcts();
    }

    @Override
    public String subjectId() {
        return sub.id();
    }

    @Override
    public Student student() {
        return student;
    }

    @Override
    public void addEvaluations(EvalEntry eval) {
        evaluations.add(eval);
        statistic.addData(eval.grade());
        student.incNEval();// just being nice <<:)
    }

    @Override
    public void addFinalEntry(EvalEntry eval) {
        finalGrade = (int) eval.grade();
       if(eval.grade() >= 10)
           student.setFinalGrade(this);
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
