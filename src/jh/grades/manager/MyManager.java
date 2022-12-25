package jh.grades.manager;

import java.sql.Connection;
import java.util.Iterator;
import static jh.grades.manager.DataBaseConnection.*;

public class MyManager implements GradesManager{

    private final Connection dbConnection;
    public MyManager(){
        dbConnection = getConnection(); // what to when this is null??
        // get the database connection
        // and that's it :)
    }

    @Override
    public Iterator<Student> top() {
        return null;
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return null;
    }
}
