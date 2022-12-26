package jh.grades.manager;

import java.util.Iterator;

public interface GradesManager {

    Iterator<Student> top();
    Iterator<SimpleStudent> listAllStudents();
}
