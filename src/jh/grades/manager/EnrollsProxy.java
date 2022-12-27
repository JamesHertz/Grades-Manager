package jh.grades.manager;

import java.util.Collections;
import java.util.Iterator;

@FunctionalInterface
public interface EnrollsProxy {

    static EnrollsProxy EMPTY_ENROLLS = Collections::emptyIterator;

    Iterator<Enrollment> getEnrolls();
}
