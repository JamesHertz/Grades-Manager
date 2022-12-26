package jh.grades.manager;

import java.util.Collections;
import java.util.Iterator;

@FunctionalInterface
public interface EnrollsProxy {

    static EnrollsProxy EMPTY_ENROLLS = number -> {
        return Collections.emptyIterator();
    };

    Iterator<Enrollment> getEnrolls(int student_number); 
}
