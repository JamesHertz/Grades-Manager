package jh.grades.manager;

import java.util.Iterator;

public interface Student extends SimpleStudent{

    float averageGrade();
    int totalCredits();
    Iterator<Enrollment> getEnrollments();
}
