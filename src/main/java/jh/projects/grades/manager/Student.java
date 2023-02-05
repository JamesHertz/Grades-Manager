package jh.projects.grades.manager;

import java.util.Iterator;

public interface Student {
    // a valid grade is the one between 0 and 20.0
    static boolean validGrade(float grade){
        return grade >= 0.0f && grade <= 20.0f;
    }
    String name();
    int number();
    float averageGrade();
    int totalCredits();
    Iterator<Enrollment> getEnrollments();
}
