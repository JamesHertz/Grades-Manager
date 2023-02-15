package jh.projects.grades.database;


import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.rawdata.RawEnrollment;
import jh.projects.grades.rawdata.RawStudent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static jh.projects.grades.database.Queries.*;
public class GMDataBase implements DataBase{

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load DB driver.");
        }
    }

    private static final String DB_URL_FORMAT = "jdbc:sqlite:%s";

    private final Connection dbConnection;
    private final String dbFilename;

    public GMDataBase(String filename){
        try {
            dbConnection = DriverManager.getConnection(String.format(DB_URL_FORMAT, filename));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to load database: " + filename);
        }
        this.dbFilename = filename;
    }

    @Override
    public Connection getDBConnection() {
        return dbConnection;
    }

    @Override
    public String getDBName() {
        return dbFilename;
    }

    @Override
    public void insertStudent(int number, String name) {
        try(PreparedStatement st = dbConnection.prepareStatement(INSERT_STUDENT.getQueryValue())){
            st.setInt(1, number);
            st.setString(2, name);
            st.executeUpdate();
        }catch (SQLException e){
            handleSQLException(e);  // todo: change this
        }
    }

    @Override
    public void insertCourse(RawCourse cs) {
        try(PreparedStatement st = dbConnection.prepareStatement(INSERT_COURSE.getQueryValue())){
            st.setString(1, cs.courseID());
            st.setString(2, cs.name());
            st.setInt(3, cs.credits());
            st.setInt(4, cs.year());
            st.setInt(5, cs.semester());
            st.setInt(6, cs.code());
            st.executeUpdate();
        }catch (SQLException e){
            System.out.println(cs);
            handleSQLException(e);  // todo: change this
        }
    }

    @Override
    public void insertEnroll(String courseID, int studentNumber, float grade) {
       try(PreparedStatement st = dbConnection.prepareStatement(INSERT_ENROLL.getQueryValue())){
           st.setString(1, courseID);
           st.setInt(2, studentNumber);
           st.setFloat(3, grade);
           st.executeUpdate();
        }catch (SQLException e){
           handleSQLException(e);  // todo: change this
        }
    }

    @Override
    public Iterator<RawEnrollment> getEnrolls(int studentNumber) {
        List<RawEnrollment> it = new LinkedList<>();
        try(PreparedStatement st = dbConnection.prepareStatement(GET_STUDENT_ENROLLS.getQueryValue())){
            st.setInt(1, studentNumber);
            ResultSet res = st.executeQuery();
            while(res.next()){
                it.add(new RawEnrollment(
                       res.getString(1),  // cs_id
                       res.getInt(2),     // st_number
                       res.getFloat(3)    // grade
                ));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return it.iterator();
    }

    @Override
    public Iterator<RawEnrollment> getEnrolls(String courseID) {
        List<RawEnrollment> it = new LinkedList<>();
        try(PreparedStatement st = dbConnection.prepareStatement(GET_COURSE_ENROLLS.getQueryValue())){
            st.setString(1, courseID);
            ResultSet res = st.executeQuery();
            while(res.next()){
                it.add(new RawEnrollment(
                        res.getString(1), // cs_id
                        res.getInt(2),    // st_number
                        res.getFloat(3)   // grade
                ));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return it.iterator();
    }

    @Override
    public Iterator<RawCourse> getAllCourses() {
        List<RawCourse> cs = new LinkedList<>();
        try(PreparedStatement st = dbConnection.prepareStatement(GET_COURSES.getQueryValue())){
           st.execute();
           ResultSet res = st.getResultSet();
           while(res.next()){
               cs.add(new RawCourse(
                       res.getString(1),  // cs_id
                       res.getString(2),  // cs_name
                       res.getInt(3),     // credits
                       res.getInt(4),     // cs_year
                       res.getInt(5),     // cs_semester
                       res.getInt(6)      // cs_code
               ));
           }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return cs.iterator();
    }

    @Override
    public Iterator<RawStudent> getAllStudents() {
        List<RawStudent> sts = new ArrayList<>(250);
        try(PreparedStatement st = dbConnection.prepareStatement(GET_STUDENTS.getQueryValue())){
            st.execute();
            ResultSet res = st.getResultSet();
            while(res.next()){
                sts.add(new RawStudent(
                    res.getInt(1),    // number
                    res.getString(2), // name
                    res.getInt(3),    // total_credits
                    res.getFloat(4)   // avg_grade
                )) ;
            }

        }catch (SQLException e){
            handleSQLException(e);
        }

        return sts.iterator();
    }

    @Override
    public void commit() {
        try {
            dbConnection.commit();
            dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void startTransaction() {
        try {
            dbConnection.setAutoCommit(false);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void rollBack() {
        try {
            dbConnection.rollback();
            dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void handleSQLException(SQLException e){
        System.out.println("Error handling DB operation : " + e.getMessage());
        System.out.println("Aborting...");
        System.exit(1);
    }

}
