package jh.grades.manager;

public interface Course {
    String id();
    String name();
    int year();
    int credits();
    Semesters semester();
    // TODO: FOR THE NEAR FUTURE
    // Iterator<Evaluations> listEvaluations()
}
