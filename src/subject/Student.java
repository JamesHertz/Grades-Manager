package subject;

import others.Statistic;
import subject.evaluations.EvalEntry;
import subject.exceptions.NoSuchSubjectInStudentException;

import java.util.Iterator;

public interface Student extends Comparable<Student> {
    int number();
    String name();
    int numberOfEvaluation();
    float averageGrade(); // target ...

    // interfaces :>
    void incNEval();
    void addEvaluation(EvalEntry eval);
    void addSlot(SubjectSlot slot);
    Statistic statistic();
    SubjectSlot subjectSlot(String subject) throws NoSuchSubjectInStudentException;
    Iterator<SubjectSlot> listSubSlot();
}
