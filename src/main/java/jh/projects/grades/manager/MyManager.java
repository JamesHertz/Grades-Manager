package jh.projects.grades.manager;

import jh.projects.cliparser.cliApp.api.table.CliTable;
import jh.projects.cliparser.cliApp.api.table.Table;
import jh.projects.grades.database.DataBase;
import jh.projects.grades.database.GMDataBase;
import jh.projects.grades.uploader.CourseInfo;
import jh.projects.grades.uploader.EnrollRecord;
import jh.projects.grades.uploader.GradesUploader;
import jh.projects.grades.uploader.UploadInfo;
import jh.projects.grades.uploader.excepctions.UploadException;

import java.sql.*;
import java.text.Collator;
import java.util.*;

import static jh.projects.grades.manager.DataBaseConnection.*;
import static jh.projects.grades.manager.EnrollsProxy.EMPTY_ENROLLS;
import static jh.projects.grades.database.DataBase.*;

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

    private static final int DEFAULT_STUDENT_NUMBER = 300;
    private static final String DB_NAME = "dbs/file1.db";
    private final SortedMap<Integer, Student> students;
    private final Map<String, Course> courses;
    private final SortedSet<Student> studentsByOrder;
    private final List<Student> topBoard;
    private final Connection dbConnection;
    private final DataBase db;

    public MyManager(){
        db = new GMDataBase(DB_NAME);

        dbConnection = getConnection(); // what to when this is null??
        courses = new HashMap<>();
        students = new TreeMap<>(); // by number
        topBoard = new ArrayList<>(DEFAULT_STUDENT_NUMBER);
        studentsByOrder = new TreeSet<>(cmpByName);

        this.uploadData();
    }



    // TODO: fix this naive approach
    private EnrollsProxy createEnrollsProxy(int studentNumber){
        return () -> {
            Iterator<RawEnroll> enrolls = db.getEnrolls(studentNumber);
            List<Enrollment> studentEnrolls = new LinkedList<>();
            while(enrolls.hasNext()){
                RawEnroll aux = enrolls.next();
                studentEnrolls.add(new SimpleEnrollment(
                   students.get(studentNumber),
                   courses.get(aux.courseID()),
                   aux.grade()
                ));
            }
            return studentEnrolls.iterator();
       };
    }

    private void uploadData(){
        Iterator<RawStudent> sts = db.getAllStudents();
        while(sts.hasNext()){
            RawStudent aux = sts.next();
            Student st  = new AcademicStudent(aux.number(), aux.name())
                    .setAvgGrade(aux.avgGrade())
                    .setTotalCredits(aux.totalCredits())
                    .setEnrolls(createEnrollsProxy(aux.number()));
            students.put(aux.number(), st);
            studentsByOrder.add(st);
            topBoard.add(st);
        }

        Iterator<RawCourse> cs = db.getAllCourses();
        while(cs.hasNext()){
            RawCourse aux = cs.next();
            courses.put(aux.id(), new ColleagueCourse(
                aux.id(), aux.name(), aux.credits(), aux.year(),
                Semesters.getSemester(aux.semester())
            ));
        }
    }

    @Override
    public void uploadEnrolls(UploadInfo info) throws UploadException {
        Iterator<EnrollRecord> it = GradesUploader.getRecords(info);

        /*
        CourseInfo cs = info.getCourseInfo();

        CliTable cs_t = new Table(2);
        cs_t.setDelimiter(1, " : ");
        cs_t.add("course id", info.getCourseId());
        cs_t.add("name", cs.name());
        cs_t.add("semester", cs.semester());
        cs_t.add("year", cs.year());
        cs_t.add("credits", cs.credits());
        cs_t.print();
        System.out.println();

        if(it.hasNext()){
            CliTable t = new Table(new String[]{"Number", "Name", "Grade"});
            while(it.hasNext()){
                EnrollRecord rec = it.next();
                t.add(rec.st_number(), rec.st_name(), rec.grade());
            }
            t.print();
        }else
            System.out.println("No records :(");
         */
    }



    @Override
    public Course getCourse(String course_id) {
        return courses.get(course_id);
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
