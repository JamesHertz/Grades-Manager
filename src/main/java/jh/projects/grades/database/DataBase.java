package jh.projects.grades.database;

import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.rawdata.RawEnrollment;
import jh.projects.grades.rawdata.RawStudent;

import java.sql.Connection;
import java.util.Iterator;

public interface DataBase {

    Connection getDBConnection();
    String getDBName();
    void insertStudent(int number, String name);
    void insertCourse(RawCourse cs);
    void insertEnroll(String courseID, int studentNumber, float grade);

    Iterator<RawEnrollment> getEnrolls(int studentNumber);
    Iterator<RawEnrollment> getEnrolls(String courseID);

    Iterator<RawCourse> getAllCourses();
    Iterator<RawStudent> getAllStudents();

    void startTransaction();
    void rollBack();
    void commit();
}
