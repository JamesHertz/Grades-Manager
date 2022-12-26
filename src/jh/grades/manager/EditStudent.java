package jh.grades.manager;

public interface EditStudent extends Student{
    EditStudent setAvgGrade(float grade);
    EditStudent setTotalCredits(int credits);
    EditStudent setEnrolls(EnrollsProxy enrolls);
}
