package jh.grades.manager;

import java.util.Iterator;

public interface GradesManager {

    Student getStudent(int number);
    Iterator<Student> top();
    Iterator<Student> listAllStudents();
    Iterator<Course> listAllCourses();
}
