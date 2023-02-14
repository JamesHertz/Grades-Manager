package jh.projects.grades.manager;

import jh.projects.grades.database.DataBase;
import jh.projects.grades.database.GMDataBase;
import jh.projects.grades.manager.exceptions.CourseAlreadyExistsException;
import jh.projects.grades.manager.exceptions.GMException;
import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.rawdata.RawEnrollment;
import jh.projects.grades.rawdata.RawStudent;
import jh.projects.grades.uploader.CourseUploader;
import jh.projects.grades.uploader.exceptions.UploadException;

import java.text.Collator;
import java.util.*;

// TODO: finish course thing and start working on the inserting info commands.
public class Manager implements GradesManager{

    // comparators used in the app
    // the first compares by name
    // and the second by credits if tie avg_grade and if still tie by name
    private static final Comparator<Student> cmpByName = (s1, s2) -> {
        final Collator ins = Collator.getInstance();
        ins.setStrength(Collator.NO_DECOMPOSITION);
        return ins.compare(s1.getName(), s2.getName());
    };

    private static final Comparator<Student> cmpByGrades = (s1, s2) -> {
        int credits_diff =  s2.getTotalCredits() - s1.getTotalCredits();
        if(credits_diff != 0) return credits_diff;
        float grades_diff = s2.getAvgGrade() - s1.getAvgGrade();
        if(grades_diff != 0) return (grades_diff > 0) ? 1 : -1 ;
        return cmpByName.compare(s1, s2); // compare by name
    };

    /*
    private static final Comparator<Course> courseComp = (c1, c2) -> {
        int diff =  c1.getYear() - c2.getYear();
        if(diff != 0) return diff;

        diff = c1.getSemester().compare(c2.getSemester());
        if(diff != 0) return diff;

        return c1.getName().compareTo(c1.getName());
    };
     */

    private static final int DEFAULT_STUDENT_NUMBER = 300;
    private static final String DB_NAME = "dbs/storage.db";
    private final SortedMap<Integer, Student> students;
    private final Map<String, Course> courses;
    private final SortedSet<Student> studentsByOrder;
    private final List<Student> topBoard;
    private final DataBase db;

    public Manager(){
        db = new GMDataBase(DB_NAME);

        courses = new TreeMap<>();
        students = new TreeMap<>(); // by number
        topBoard = new ArrayList<>(DEFAULT_STUDENT_NUMBER);
        studentsByOrder = new TreeSet<>(cmpByName);

        this.uploadData();
    }



    private EnrollsProxy createEnrollsProxy(int studentNumber){
        return () -> {
            Iterator<RawEnrollment> enrolls = db.getEnrolls(studentNumber);
            List<Enrollment> studentEnrolls = new LinkedList<>();
            while(enrolls.hasNext()){
                RawEnrollment aux = enrolls.next();
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
            Student st  = new GMStudent()
                                .setStudentName(aux.name())
                                .setStudentNumber(aux.number())
                                .setStudentEnrolls(createEnrollsProxy(aux.number()))
                                .setStudentGrade(aux.avgGrade())
                                .setStudentTotalCredits(aux.totalCredits());

            students.put(aux.number(), st);
            studentsByOrder.add(st);
            topBoard.add(st);
        }

        Iterator<RawCourse> cs = db.getAllCourses();
        while(cs.hasNext()){
            RawCourse aux = cs.next();
            Course new_cs =  new GMCourse()
                        .setId(aux.courseID())
                        .setName(aux.name())
                        .setCredits(aux.credits())
                        .setYear(aux.year())
                        .setSemester(Semesters.getSemester(aux.semester()))
                        .setCode(aux.code());

            courses.put(aux.courseID(),new_cs);
        }
    }

    @Override
    public int uploadCourses(String filename) throws GMException,  UploadException {
        Iterator<RawCourse> cs = CourseUploader.getCourses(filename);
        Map<String, Course> cache = new TreeMap<>();

        db.startTransaction();
        while(cs.hasNext()){
            RawCourse raw = cs.next() ;
            if(courses.containsKey(raw.courseID())){
                db.rollBack();
                throw new CourseAlreadyExistsException(raw.courseID());
            }

            // todo: check if everything is okay
            cache.put(raw.courseID(),
                        new GMCourse()
                            .setId(raw.courseID())
                            .setName(raw.name())
                            .setCredits(raw.credits())
                            .setYear(raw.year())
                            .setSemester(Semesters.getSemester(raw.semester()))
                            .setCode(raw.code())
            );
            db.insertCourse(cs.next());
        }
        db.commit();
        courses.putAll(cache);

        return cache.size();
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
        // todo: handle this mess below
        return courses.values().iterator();
    }
}
