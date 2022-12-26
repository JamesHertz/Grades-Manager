package jh.grades.manager;

import java.sql.Connection;
import java.util.Iterator;
import static jh.grades.manager.DataBaseConnection.*;

public class MyManager implements GradesManager{

    /*
        => let me think about this....
        I could for example have the following:

        # first approach:

        GradesManager{
            Map<String, Student> students;
            Map<String, Course> courses;
        }

        Student{
            // all it's info
            Enrollments[] enrolls;
        }

        Course{
            // all it's info
            Enrollments[] enrolls;
        }
        ## The idea would be that I just use the database to store the data
        load them when the app boots up and make changes in these datastructures them
        save when the app goes down.

        ### Problems:
            - The complexity of this task increase as the app gets more and more data
        # Second approach
            -> Every option just queries the database and returns the data.
         ## It doesn't seem to be bad, the only problem is that at some point I have
         to use caching.
     */
    /*
        A student is built when a query is done.
     */
    private final Connection dbConnection;
    public MyManager(){
        dbConnection = getConnection(); // what to when this is null??
    }

    @Override
    public Iterator<SimpleStudent> top() {
        return null;
    }

    @Override
    public Iterator<SimpleStudent> listAllStudents() {
        return null;
    }
}
