package jh.projects.grades.database;


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
    // queries

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
            e.printStackTrace();
        }
    }

    @Override
    public void insertCourse(String id, String name, int credits, int year, int semester) {
        try(PreparedStatement st = dbConnection.prepareStatement(INSERT_COURSE.getQueryValue())){
            st.setString(1, id);
            st.setString(2, name);
            st.setInt(3, credits);
            st.setInt(4, year);
            st.setInt(5, semester);
            st.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<RawEnroll> getEnrolls(int studentNumber) {
        List<RawEnroll> it = new LinkedList<>();
        try(PreparedStatement st = dbConnection.prepareStatement(GET_STUDENT_ENROLLS.getQueryValue())){
            st.setInt(1, studentNumber);
            ResultSet res = st.executeQuery();
            while(res.next()){
                it.add(new RawEnroll(
                       res.getString(1),  // cs_id
                       res.getInt(2),     // st_number
                       res.getFloat(3)    // grade
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return it.iterator();
    }

    @Override
    public Iterator<RawEnroll> getEnrolls(String courseID) {
        List<RawEnroll> it = new LinkedList<>();
        try(PreparedStatement st = dbConnection.prepareStatement(GET_COURSE_ENROLLS.getQueryValue())){
            st.setString(1, courseID);
            ResultSet res = st.executeQuery();
            while(res.next()){
                it.add(new RawEnroll(
                        res.getString(1), // cs_id
                        res.getInt(2),    // st_number
                        res.getFloat(3)   // grade
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                       res.getString(1), // cs_id
                       res.getString(2), // cs_name
                       res.getInt(3),    // credits
                       res.getInt(4),    // cs_year
                       res.getInt(5)     // cs_semester
               ));
           }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return sts.iterator();
    }

    @Override
    public void commit() {
        try {
            dbConnection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
