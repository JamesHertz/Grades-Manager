package jh.grades.manager;

import java.util.Iterator;

public interface GradesManager {

    /*
        loadData() =>
                        Map<String, Subject> subjects;
                        Map<String, Student> students;
         question? => how to change data?
         how to invalidate the cache?
     */

    Iterator<Student> top();
    Iterator<Student> listAllStudents();
}
