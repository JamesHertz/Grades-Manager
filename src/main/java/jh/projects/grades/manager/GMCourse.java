package jh.projects.grades.manager;

public class GMCourse implements Course{

    private  String id, name;
    private  int year, credits, code;
    private Period semester;

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
    public Period getPeriod() {
        return semester;
    }

    @Override
    public int getCode(){
        return code;
    }


    // used to create a Course
    public GMCourse setCode(int code) {
        this.code = code;
        return this;
    }

    public GMCourse setId(String id) {
        this.id = id;
        return this;
    }

    public GMCourse setName(String name) {
        this.name = name;
        return this;
    }

    public GMCourse setYear(int year) {
        this.year = year;
        return this;
    }

    public GMCourse setCredits(int credits) {
        this.credits = credits;
        return this;
    }

    public GMCourse setSemester(Period semester) {
        this.semester = semester;
        return this;
    }

}
