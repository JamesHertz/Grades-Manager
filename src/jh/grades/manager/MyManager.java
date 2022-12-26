package jh.grades.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static jh.grades.manager.DataBaseConnection.*;

public class MyManager implements GradesManager{

    /*
        A student is built when a query is done.
     */
    private final Connection dbConnection;

    public MyManager(){
        dbConnection = getConnection(); // what to when this is null??
    }

    @Override
    public Iterator<Student> top() {
        return null;
    }

    @Override
    public Iterator<SimpleStudent> listAllStudents() {
        try(
            Statement smt = dbConnection.createStatement();
            ResultSet res = smt.executeQuery("select st_name, st_number from Student;")
        ){
            List<SimpleStudent> students = new LinkedList<>();

            while(res.next()){
                students.add(
                    new AcademicStudent(
                        res.getString("st_name"), 
                        res.getInt("st_number")
                    )
                );
            }

            return students.iterator();
        }catch(Exception e){
            e.printStackTrace(); // TODO: delete later :)
            return Collections.emptyIterator();
        }
        
    }
}
