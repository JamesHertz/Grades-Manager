package jh.grades.manager;

import jh.grades.uploader.UploadInfo;

import java.sql.*;
import java.text.Collator;
import java.util.*;

import static jh.grades.manager.DataBaseConnection.*;
import static jh.grades.manager.EnrollsProxy.EMPTY_ENROLLS;

// TODO: finish course thing and start working on the inserting info commands.
public class MyManager implements GradesManager{

    // TODO: check caching rows jdbc docs
    // comparators used in the app
    // the first compares by name
    // and the second by credits if tie avg_grade and if still tie by name
    private static final Comparator<Student> cmpByName = (s1, s2) -> {
            final Collator ins = Collator.getInstance();
            ins.setStrength(Collator.NO_DECOMPOSITION);
            return ins.compare(s1.name(), s2.name());
    };
    private static final Comparator<Student> cmpByGrades = (s1, s2) -> {
        int credits_diff =  s2.totalCredits() - s1.totalCredits();
        if(credits_diff != 0) return credits_diff;
        float grades_diff = s2.averageGrade() - s1.averageGrade() ;
        if(grades_diff != 0) return (grades_diff > 0) ? 1 : -1 ;
        return cmpByName.compare(s1, s2); // compare by name
    };


    // SQL QUERIES
    private static final String GET_STUDENT_QUERY = "select number, name, avg_grade, total_credits from MyStudent";
    private static final String GET_COURSE_QUERY = "select cs_id, cs_name, credits, cs_year, cs_semester from Course";
    private static final String GET_ENROLLS_QUERY = "select cs_id, grade from Enrollment where st_number = ?";


    // the data structures
    private static final int DEFAULT_STUDENTS = 400;
    private final SortedMap<Integer, Student> students;
    private final Map<String, Course> courses;
    private final SortedSet<Student> studentsByOrder;
    private final List<Student> topBoard;
    private final Connection dbConnection;

    public MyManager(){
        dbConnection = getConnection(); // what to when this is null??
        courses = new HashMap<>();
        students = new TreeMap<>(); // by number
        topBoard = new ArrayList<>(DEFAULT_STUDENTS);
        studentsByOrder = new TreeSet<>(cmpByName);

        init_data_structure();
    }



    // TODO: fix this naive approach
    private EnrollsProxy create_enrolls_proxy(int student_number){
        return () -> {
            try(
                    PreparedStatement smt = dbConnection.prepareStatement(GET_ENROLLS_QUERY);
            ){
                smt.setInt(1, student_number);
                ResultSet res = smt.executeQuery();
                List<Enrollment> enrolls = new LinkedList<>();

                while(res.next()){
                    String cs_id = res.getString(1);
                    enrolls.add(new SimpleEnrollment(
                            students.get(student_number),
                            courses.get(cs_id),
                            res.getFloat(2)
                    ));
                }
                return enrolls.iterator();
            }catch (SQLException e){
                e.printStackTrace();
                return EMPTY_ENROLLS.getEnrolls();
            }
        };
    }

    private void init_data_structure(){
         try(
                 Statement smt1= dbConnection.createStatement();
                 Statement smt2 = dbConnection.createStatement();
         ){

             ResultSet st_data = smt1.executeQuery(GET_STUDENT_QUERY);
             ResultSet cs_data = smt2.executeQuery(GET_COURSE_QUERY);

             // TODO: is it possible to execute two queries in parallel ??
             // TODO: think about the columns number or name
            while(st_data.next()){
                int st_number = st_data.getInt(1);
                Student st = new AcademicStudent(
                        st_number,
                        st_data.getString(2) // the name
                ).setAvgGrade(st_data.getFloat(3))
                .setTotalCredits(st_data.getInt(4))
                .setEnrolls(create_enrolls_proxy(st_number));

                students.put(st.number(), st);
                studentsByOrder.add(st);
                topBoard.add(st);
            }

            while (cs_data.next()){
                final String course_id = cs_data.getString(1);
               ColleagueCourse course = new ColleagueCourse(
                       course_id, // the id
                       cs_data.getString(2), // the name
                       cs_data.getInt(3), // the credits
                       cs_data.getInt(4), // the year
                       Semesters.getSemester(cs_data.getInt(5)) // and the semester
               );
               courses.put(course_id, course);
            }

        }catch (Exception e){
            e.printStackTrace(); // TODO: what to do??
        }
    }

    @Override
    public void uploadEnrolls(UploadInfo info) {
        // do fun stuffs :)
        // I need a class on the uploader package
        // that will just do a new thing for me :)
        // first question: what should this class return me?
        // answer: a class that will have the course id and all the records
        // I need to upload to the database.
        // how should I name this class?
        // EnrollRecords
        // second question: how should I name the class that will eventually
        // parse the records and return to me?
        // IDEAS: RecordParser, UploadManager, GradesUploader
        // Upload .getRecords(

        /*
        UploaderManager{

        }
        getRecords(UploadInfo info);
         */
    }

    @Override
    public Student getStudent(int number) {
        return students.get(number);
    }

    @Override
    public Iterator<Student> top() {
        topBoard.sort(cmpByGrades);
        return topBoard.iterator();
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return studentsByOrder.iterator();
    }

    @Override
    public Iterator<Course> listAllCourses() {
        return courses.values().iterator();
    }
}
