package jh.grades.manager;

public class SimpleEnrollment implements Enrollment{

    private final Student student;
    private final Course course;
    private final float grade;

    public SimpleEnrollment(Student student, Course course, float grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    @Override
    public Student getStudent() {
        return student;
    }

    @Override
    public Course getCourse() {
        return course;
    }

    @Override
    public float grade() {
        return grade;
    }
}
