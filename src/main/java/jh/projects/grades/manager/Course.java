package jh.projects.grades.manager;

public interface Course {
    String getCourseID();
    String getName();
    int getYear();
    int getCredits();
    Semesters getSemester();
    // int getCode();
    // Iterator<Evaluations> listEvaluations()

    // TODO: FOR THE NEAR FUTURE
}
