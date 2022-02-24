package subject;

import others.Statistic;
import others.StatisticClass;
import subject.evaluations.EvalEntry;
import subject.exceptions.NoSuchSubjectInStudentException;

import java.io.Serializable;
import java.util.*;

public class StudentClass implements Student, Serializable {
    private static final long serialVersionUID = 0L;

    // think about this
    private final SortedMap<String, SubjectSlot> evaluationsBySubject;
    private final String name;
    private final int number;
    private final Statistic statistic;
    private int nEval;


    public StudentClass(int number, String name){
        this.name = name;
        this.number = number;
        evaluationsBySubject = new TreeMap<>(); // maybe this is not necessary
        statistic = new StatisticClass();
        nEval = 0;
    }

    @Override
    public void addSlot(SubjectSlot slot) {
       evaluationsBySubject.put(slot.subjectId(), slot);
    }

    @Override
    public void addEvaluation(EvalEntry eval) {
        String subject = eval.subjectId();
        statistic.addData(eval.grade());
        SubjectSlot slot = evaluationsBySubject.get(subject);
        if(slot == null){
            slot = new SubjectSlotClass(this, eval.subject());
            evaluationsBySubject.put(subject, slot);
        }
        slot.addEvaluations(eval);
        nEval++;
    }


    @Override
    public Statistic statistic() {
        return statistic;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public int numberOfEvaluation() {
        return nEval;
    }

    @Override
    public float averageGrade() {
        return statistic.averageData();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Iterator<SubjectSlot> listSubSlot() {
        return evaluationsBySubject.values().iterator();
    }

    @Override
    public SubjectSlot subjectSlot(String subjectId) throws NoSuchSubjectInStudentException {
        SubjectSlot slot = evaluationsBySubject.get(subjectId);
        if(slot == null)
            throw new NoSuchSubjectInStudentException(number, subjectId);
        return slot;
    }

    @Override
    public int compareTo(Student o) {
        return number - o.number();
    }
}
