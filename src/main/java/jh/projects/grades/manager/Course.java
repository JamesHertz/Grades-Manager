package jh.projects.grades.manager;

public interface Course {
    String getCourseID();
    String getName();
    int getYear();
    int getCredits();
    Semesters getSemester();
    int getCode();

    // TODO: FOR THE NEAR FUTURE
    // Iterator<Evaluations> listEvaluations()
}
