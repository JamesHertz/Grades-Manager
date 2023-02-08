package jh.projects.grades.database;

import jh.projects.grades.manager.Course;
import jh.projects.grades.manager.Enrollment;
import jh.projects.grades.manager.Student;

import java.sql.Connection;
import java.util.Iterator;

public interface DataBase {
    record RawStudent(int number, String name, int totalCredits, float avgGrade){}
    record RawCourse(String id, String name, int credits, int year, int semester){};
    record RawEnroll(String courseID, int studentNumber, float grade){};

    Connection getDBConnection();
    String getDBName();
    void insertStudent(int number, String name);
    void insertCourse(String id, String name, int credits, int year, int semester);
    void insertEnroll(String courseID, int studentNumber, float grade);

    Iterator<RawEnroll> getEnrolls(int studentNumber);
    Iterator<RawEnroll> getEnrolls(String courseID);

    Iterator<RawCourse> getAllCourses();
    Iterator<RawStudent> getAllStudents();

    void commit();
}
