package jh.projects.grades.manager;

import java.util.Iterator;

class AcademicStudent implements EditStudent{

    private final String name;
    private final int number;

    private int totalCredits;
    private float averageGrade;

    private EnrollsProxy enrolls;

    public AcademicStudent(int number, String name){
        this.name = name;
        this.number = number;
        this.enrolls = EnrollsProxy.EMPTY_ENROLLS;
        this.totalCredits = 0;
        this.averageGrade = 0;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public float getAvgGrade() {
        return averageGrade;
    }

    @Override
    public int getTotalCredits() {
        return totalCredits;
    }

    @Override
    public Iterator<Enrollment> getEnrollments() {
        return enrolls.getEnrolls();
    }

    @Override
    public EditStudent setAvgGrade(float grade) {
        this.averageGrade = grade;
        return this;
    }

    @Override
    public EditStudent setEnrolls(EnrollsProxy enrolls) {
        this.enrolls = enrolls;
        return this;
    }

    @Override
    public EditStudent setTotalCredits(int credits) {
        this.totalCredits = credits;
        return this;
    }
    
}
