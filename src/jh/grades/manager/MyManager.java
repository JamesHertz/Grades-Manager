package jh.grades.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Collator;
import java.util.*;

import static jh.grades.manager.DataBaseConnection.*;

public class MyManager implements GradesManager{

    /*
        A student is built when a query is done.
     */
    // TODO: check caching rows jdbc docs
    private final SortedMap<Integer, Student> students;
    private final Map<String, Course> courses;
    private final SortedSet<Student> studentsByOrder;
    private final Connection dbConnection;

    public MyManager(){
        dbConnection = getConnection(); // what to when this is null??
        courses = new HashMap<>();
        students = new TreeMap<>(); // by number
        // think about this :)
        studentsByOrder = new TreeSet<>( (s1, s2) -> {
            final Collator ins = Collator.getInstance();
            ins.setStrength(Collator.NO_DECOMPOSITION);
            return ins.compare(s1.name(), s2.name());
        });
        fill_data_structure();
    }

    /**
     cs_id text primary key,
     cs_name text,
     credits int,
     cs_year int,
     cs_semester int,
     */
    private static final String GET_STUDENT_QUERY = "select name, number, avg_grade, total_credits from MyStudent";
    private static final String GET_COURSE_QUERY = "select cs_id, cs_name, credits, cs_year, cs_semester from Course";

    private void fill_data_structure(){
         try(
                 Statement smt1= dbConnection.createStatement();
                 Statement smt2 = dbConnection.createStatement();
                 ResultSet st_data = smt1.executeQuery(GET_STUDENT_QUERY);
                 ResultSet cs_data = smt2.executeQuery(GET_COURSE_QUERY);
        ){

             // TODO: is it possible to execute two queries in parallel ??
             // TODO: think about the collums number or name
            while(st_data.next()){
                Student st = new AcademicStudent(
                        st_data.getString(1), // the name
                        st_data.getInt(2) // the number and so on
                ).setAvgGrade(st_data.getFloat(3))
                .setTotalCredits(st_data.getInt(4));

                students.put(st.number(), st);
                studentsByOrder.add(st);
            }

            while (cs_data.next()){
               ColleagueCourse course = new ColleagueCourse(
                       cs_data.getString(1), // the id
                       cs_data.getString(2), // the name
                       cs_data.getInt(3) // the credits and so on
               ).setYear(cs_data.getInt(4))
                .setSemester(Semesters.getSemester(cs_data.getInt(5)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudent(int number) {
        return students.get(number);
    }

    @Override
    public Iterator<Student> top() {
        // TODO: think about to do this...
        return students.values().iterator();
//        return Collections.emptyIterator();
        /*
        try(
                Statement smt = dbConnection.createStatement();
                ResultSet res = smt.executeQuery("select name, number, avg_grade, total_credits from TopBoard");
        ){
            List<Student> students = new LinkedList<>();

            while(res.next()){
                students.add(
                        new AcademicStudent(
                                res.getString(1),
                                res.getInt(2)
                        ).setAvgGrade(
                                res.getFloat(3)
                        ).setTotalCredits(
                                res.getInt(4)
                        )
                );
            }

            return students.iterator();
        }catch(Exception e){
            e.printStackTrace(); // TODO: delete later :)
            return Collections.emptyIterator();
        }*/
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return studentsByOrder.iterator();
        /*
        try(
                Statement smt = dbConnection.createStatement();
                ResultSet res = smt.executeQuery("select st_name, st_number from Student order by st_name");
         ){
           List<Student> students = new LinkedList<>();

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
        }*/

    }
}
