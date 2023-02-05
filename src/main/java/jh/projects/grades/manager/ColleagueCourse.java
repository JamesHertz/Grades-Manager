package jh.projects.grades.manager;

public class ColleagueCourse implements Course{

    private final String id, name;

    private final int year, credits;
    private final Semesters semester;

    public ColleagueCourse(String id, String name, int credits, int year, Semesters semester) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.year = year;
        this.semester = semester;
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
