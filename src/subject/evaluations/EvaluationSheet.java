package subject.evaluations;

import others.Statistic;
import others.StatisticClass;
import others.StringComp;
import subject.Student;
import subject.Subject;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.*;

public class EvaluationSheet implements EvalSheet, Serializable {

    private static final String DEFAULT_DESCRIPTION = "None!!";
    private static final long serialVersionUID = 0L;
    private final SortedMap<Float, EvalEntry> ranking;
    private final SortedMap<String, EvalEntry> byAlphabeticalOrder;
    private final SortedSet<Student> students;
    private final Statistic statistic;
    private final String id;
    private final Subject subject;
    private boolean closed;
    private String description;

    public EvaluationSheet(String id, Subject subject){
        this(id, subject, DEFAULT_DESCRIPTION);
    }

    public EvaluationSheet(String id, Subject subject, String description){
        this.id = id;
        this.subject = subject;
        this.description = description;
        ranking = new TreeMap<>(); // problems later...
        byAlphabeticalOrder = new TreeMap<>(new StringComp());
        students = new TreeSet<>();
        statistic = new StatisticClass();
        closed = false;
    }

    @Override
    public void evaluate(Student student, float grade) throws AlreadyEvaluatedException {
        if(!students.add(student))
            throw new AlreadyEvaluatedException(student.number());
        EvalEntry eval = new EvalEntryClass(this, student, grade);
        student.addEvaluation(eval);
        statistic.addData(grade);
        addEvaluation(eval);
    }

    private void addEvaluation(EvalEntry eval){
        ranking.put(eval.grade(), eval);
        byAlphabeticalOrder.put(eval.student().name(), eval);
        subject.addData(eval.grade());
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String evaluationId() {
        return id;
    }

    @Override
    public Subject subject() {
        return subject;
    }

    @Override
    public Statistic statistic() {
        return statistic;
    }

    @Override
    public Iterator<EvalEntry> listByAlphabeticalOrder() {
        return byAlphabeticalOrder.values().iterator();
    }


    @Override
    public Iterator<EvalEntry> ranking() {
        return ranking.values().iterator();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        closed = true;
    }


    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof EvaluationSheet))
            return false;
        EvaluationSheet eval = (EvaluationSheet) obj;
        if(id == null)
            return eval.evaluationId() == null;
        else
            return id.equals(eval.evaluationId());
    }
}
