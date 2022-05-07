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
    private int ects;
    private float ectsFGradeProduct;


    public StudentClass(int number, String name){
        this.name = name;
        this.number = number;
        evaluationsBySubject = new TreeMap<>(); // maybe this is not necessary
        statistic = new StatisticClass();
        nEval = 0;
        ects = 0;
        ectsFGradeProduct = 0.0f;
    }

    @Override
    public void setFinalGrade(SubjectSlot slot) {
       // about failures (etc) we solve it later
        int ects = slot.getSubEcts();
        ectsFGradeProduct += ects * slot.getFinalGrade();
        this.ects += ects;
    }

    @Override
    public void addSlot(SubjectSlot slot) {
        evaluationsBySubject.put(slot.subjectId(), slot);
    }

    @Override
    public void incNEval() {
        nEval++;
    }

    @Override
    public void addDataToS(float data) {
       statistic.addData(data);
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
    public int getTotEcts() {
        return ects;
    }

    @Override
    public int numberOfEvaluation() {
        return nEval;
    }

    @Override
    public float averageGrade() {
        // I'm happy iei :)
        return  (ects == 0) ? 0.0f : ectsFGradeProduct/ects;
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
