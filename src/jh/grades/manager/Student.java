package jh.grades.manager;

import java.util.Iterator;

public interface Student {
    int number();
    String name();
    float averageGrade();
    int totalCredits();
    Iterator<Enrollment> getEnrollments();
}
