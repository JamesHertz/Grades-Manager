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
    public String getCourseID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getCredits() {
        return credits;
    }

    @Override
    public Semesters getSemester() {
        return semester;
    }
}
