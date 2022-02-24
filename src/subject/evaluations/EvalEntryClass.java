package subject.evaluations;

import subject.Student;
import subject.Subject;

import java.io.Serializable;

public class EvalEntryClass implements EvalEntry, Serializable {

    private static final long serialVersionUID = 0L;
    private final EvalSheet evaluationSheet;
    private final Student student;
    private float grade;

    public EvalEntryClass(EvalSheet evaluationSheet, Student student, float grade){
        this.evaluationSheet = evaluationSheet;
        this.student = student;
        this.grade = grade;
    }

    @Override
    public Student student() {
        return student;
    }

    @Override
    public String subjectId() {
        return evaluationSheet.subject().id();
    }

    @Override
    public Subject subject() {
        return evaluationSheet.subject();
    }

    @Override
    public float grade() {
        return grade;
    }

    @Override
    public String evaluationsId() {
        return evaluationSheet.id();
    }

    @Override
    public int compareTo(EvalEntry o) {
        return (grade > o.grade()) ? -1 : 1;
    }
}
