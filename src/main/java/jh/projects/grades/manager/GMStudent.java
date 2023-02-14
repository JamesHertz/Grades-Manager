package jh.projects.grades.manager;

import java.util.Iterator;

class GMStudent implements EditStudent{

    private String name;
    private int number;
    private int totalCredits;
    private float averageGrade;
    private EnrollsProxy enrolls = EnrollsProxy.EMPTY_ENROLLS;

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
    public void setAvgGrade(float grade) {
        this.averageGrade = grade;
    }

    @Override
    public void setTotalCredits(int credits) {
        this.totalCredits = credits;
    }



    // METHODS USED TO BUILD A STUDENT :)

    public GMStudent setStudentNumber(int number) {
        this.number = number;
        return this;
    }

    public GMStudent setStudentName(String name) {
        this.name = name;
        return this;
    }

    public GMStudent setStudentGrade(float grade) {
        this.averageGrade = grade;
        return this;
    }

    public GMStudent setStudentEnrolls(EnrollsProxy enrolls) {
        this.enrolls = enrolls;
        return this;
    }

    public GMStudent setStudentTotalCredits(int credits) {
        this.totalCredits = credits;
        return this;
    }


}
