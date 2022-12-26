package jh.grades.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

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
        try(
                Statement smt = dbConnection.createStatement();
                ResultSet res = smt.executeQuery("select name, number, avg_grade, total_credits from TopBoard");
        ){
            List<Student> students = new LinkedList<>();

            while(res.next()){
                students.add(
                        new AcademicStudent(
                                res.getString("name"),
                                res.getInt("number")
                        ).setAvgGrade(
                                res.getFloat("avg_grade")
                        ).setTotalCredits(
                                res.getInt("total_credits")
                        )
                );
            }

            return students.iterator();
        }catch(Exception e){
            e.printStackTrace(); // TODO: delete later :)
            return Collections.emptyIterator();
        }
    }

    @Override
    public Iterator<SimpleStudent> listAllStudents() {
        try(
                Statement smt = dbConnection.createStatement();
                ResultSet res = smt.executeQuery("select st_name, st_number from Student order by st_name");
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
