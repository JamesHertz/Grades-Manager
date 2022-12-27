package jh.grades.manager;

public class ColleagueCourse implements Course{

    private String id, name;

    private int year, credits;
    private Semesters semester;

    public ColleagueCourse(String id, String name, int credits) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.year = 0;
        this.semester = null;
    }

    // TODO: think about this arbitrary decision :(
    public ColleagueCourse setSemester(Semesters semester) {
        this.semester = semester;
        return this;
    }

    public ColleagueCourse setYear(int year) {
        this.year = year;
        return this;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int year() {
        return year;
    }

    @Override
    public int credits() {
        return credits;
    }

    @Override
    public Semesters semester() {
        return semester;
    }
}
