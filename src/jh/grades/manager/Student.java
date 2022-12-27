package jh.grades.manager;

import java.util.Iterator;

public interface Student {
    String name();
    int number();
    float averageGrade();
    int totalCredits();
    Iterator<Enrollment> getEnrollments();
}
