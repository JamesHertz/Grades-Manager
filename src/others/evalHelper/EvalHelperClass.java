package others.evalHelper;

import subject.Student;

import java.io.Serializable;

public class EvalHelperClass implements EvalHelper, Serializable {

    private static final long serialVersionUID = -3364902666218938897L;
    private final String name;
    private final int number;
    private final float grade;

    public EvalHelperClass(String name, int number, float grade){
        this.name = name;
        this.number = number;
        this.grade = grade;
    }
    @Override
    public int number() {
        return number;
    }

    @Override
    public Student getStudent() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public float grade() {
        return grade;
    }
}
