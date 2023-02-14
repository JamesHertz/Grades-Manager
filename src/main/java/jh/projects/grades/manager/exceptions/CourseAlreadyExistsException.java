package jh.projects.grades.manager.exceptions;

public class CourseAlreadyExistsException extends GMException{

    private String courseID;
    public CourseAlreadyExistsException(String courseID) {
        super(String.format("Course '%s' already exists.", courseID));
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }
}
