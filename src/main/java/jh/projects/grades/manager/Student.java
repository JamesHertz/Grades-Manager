package jh.projects.grades.manager;

import java.util.Iterator;

public interface Student {
    String getName();
    int getNumber();
    float getAvgGrade();
    int getTotalCredits();
    Iterator<Enrollment> getEnrollments();
}
