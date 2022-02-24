package subject.evaluations;

import subject.Student;
import subject.Subject;

public interface EvalEntry extends Comparable<EvalEntry> {
    Student student();
    Subject subject();
    String subjectId();
    String evaluationsId();
    float grade();
}
