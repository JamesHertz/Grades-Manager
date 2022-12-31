package jh.grades.uploader;

public class Record implements EnrollRecord{

    private final int number;
    private final String name;
    private final float grade;

    public Record(int number, String name, float grade) {
        this.number = number;
        this.name = name;
        this.grade = grade;
    }

    @Override
    public int student_number() {
        return number;
    }

    @Override
    public String student_name() {
        return name;
    }

    @Override
    public float grade() {
        return grade;
    }
}
