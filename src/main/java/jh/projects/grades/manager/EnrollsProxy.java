package jh.projects.grades.manager;

import java.util.Collections;
import java.util.Iterator;

@FunctionalInterface
public interface EnrollsProxy {

    EnrollsProxy EMPTY_ENROLLS = Collections::emptyIterator;

    Iterator<Enrollment> getEnrolls();
}
