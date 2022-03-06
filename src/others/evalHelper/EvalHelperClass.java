package others.evalHelper;

import subject.Student;

import java.io.Serializable;

public class EvalHelperClass implements EvalHelper{

    private static final long serialVersionUID = -3364902666218938897L;
    private String name;
    private int number;
    private float grade;
    private Student student;

    public EvalHelperClass(String name, int number, float grade){
        this.name = name;
        this.number = number;
        this.grade = grade;
    }

    public EvalHelperClass(Student student, float grade){
        this.student = student;
        this.grade = grade;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public Student getStudent() {
        return student;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public float getGrade() {
        return grade;
    }
}
