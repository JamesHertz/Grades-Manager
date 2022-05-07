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
    int getTotEcts();
    //interfaces :>
    void incNEval();
    void addSlot(SubjectSlot slot); // new interface :)
    void addDataToS(float data); // join with incNEval ....
    void setFinalGrade(SubjectSlot slot);
    //end interface

    Statistic statistic();
    SubjectSlot subjectSlot(String subject) throws NoSuchSubjectInStudentException;
    Iterator<SubjectSlot> listSubSlot();
}
