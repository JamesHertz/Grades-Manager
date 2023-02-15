package jh.projects.grades.manager;

import jh.projects.grades.database.DataBase;
import jh.projects.grades.database.GMDataBase;
import jh.projects.grades.manager.exceptions.CourseAlreadyExistsException;
import jh.projects.grades.manager.exceptions.GMException;
import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.rawdata.RawEnrollment;
import jh.projects.grades.rawdata.RawStudent;
import jh.projects.grades.rawdata.StudentRecord;
import jh.projects.grades.uploader.CourseUploader;
import jh.projects.grades.uploader.GradesUploader;
import jh.projects.grades.uploader.exceptions.UploadException;

import java.text.Collator;
import java.util.*;

import static jh.projects.grades.uploader.GradesUploader.*;

// TODO: finish course thing and start working on the inserting info commands.
public class Manager implements GradesManager {

    // comparators used in the app
    // the first compares by name
    // and the second by credits if tie avg_grade and if still tie by name
    private static final Comparator<Student> cmpByName = (s1, s2) -> {
        final Collator ins = Collator.getInstance();
        ins.setStrength(Collator.NO_DECOMPOSITION);
        return ins.compare(s1.getName(), s2.getName());
    };

    private static final Comparator<Student> cmpByGrades = (s1, s2) -> {
        int credits_diff = s2.getTotalCredits() - s1.getTotalCredits();
        if (credits_diff != 0) return credits_diff;
        float grades_diff = s2.getAvgGrade() - s1.getAvgGrade();
        if (grades_diff != 0) return (grades_diff > 0) ? 1 : -1;
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

    private ClipCredentials credentials;

    public Manager() {
        db = new GMDataBase(DB_NAME);

        courses = new TreeMap<>();
        students = new TreeMap<>(); // by number
        topBoard = new ArrayList<>(DEFAULT_STUDENT_NUMBER);
        studentsByOrder = new TreeSet<>(cmpByName);
        credentials = null;

        this.uploadData();
    }


    private EnrollsProxy createEnrollsProxy(int studentNumber) {
        return () -> {
            Iterator<RawEnrollment> enrolls = db.getEnrolls(studentNumber);
            List<Enrollment> studentEnrolls = new LinkedList<>();
            while (enrolls.hasNext()) {
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

    private void uploadData() {
        Iterator<RawStudent> sts = db.getAllStudents();
        while (sts.hasNext()) {
            RawStudent aux = sts.next();
            EditStudent st = addStudent(aux.number(), aux.name());
            st.setAvgGrade(aux.avgGrade());
            st.setTotalCredits(aux.totalCredits());
        }

        Iterator<RawCourse> cs = db.getAllCourses();
        while (cs.hasNext()) {
            RawCourse aux = cs.next();
            courses.put(aux.courseID(),
                    new GMCourse()
                            .setId(aux.courseID())
                            .setName(aux.name())
                            .setCredits(aux.credits())
                            .setYear(aux.year())
                            .setSemester(Semesters.getSemester(aux.semester()))
                            .setCode(aux.code())
            );
        }
    }

    @Override
    public int uploadCourses(String filename) throws GMException, UploadException {
        Iterator<RawCourse> cs = CourseUploader.getCourses(filename);
        Map<String, Course> cache = new TreeMap<>();

        db.startTransaction();
        while (cs.hasNext()) {
            RawCourse raw = cs.next();
            if (courses.containsKey(raw.courseID())) {
                db.rollBack();
                throw new CourseAlreadyExistsException(raw.courseID());
            }

            // todo: check if everything is okay
            cache.put(
                    raw.courseID(),
                    new GMCourse()
                            .setId(raw.courseID())
                            .setName(raw.name())
                            .setCredits(raw.credits())
                            .setYear(raw.year())
                            .setSemester(Semesters.getSemester(raw.semester()))
                            .setCode(raw.code())
            );
            db.insertCourse(raw);
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
    public EditStudent getStudent(int number) {
        return (EditStudent) students.get(number);
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

    @Override
    public void setCredentials(ClipCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public ClipCredentials getCredentials() {
        return credentials;
    }

    @Override
    public void update() {

        db.startTransaction();
        for (Course cs : courses.values()) {
            Map<Integer, StudentRecord> dummy = new TreeMap<>();
            for (int year = cs.getYear(); year <= 3; ++year) {
                for (int sem = cs.getSemester().getId(); sem != 0 && sem <= 2; ++sem) {
                    Iterator<StudentRecord> records = GradesUploader.getEnrolls(
                            this.credentials, new CourseInfo(year, sem, cs.getCode())
                    );

                    if (records == null) throw new RuntimeException("Error getting " + cs.getCourseID() + " grades.");

                    boolean first_time = dummy.isEmpty();
                    while (records.hasNext()) {
                        StudentRecord rec = records.next(), aux;
                        if (first_time || (aux = dummy.get(rec.number())) != null
                                && rec.grade() > aux.grade()) {
                            dummy.put(rec.number(), rec);
                        }
                    }
                }
            }

            for (StudentRecord rec : dummy.values()) {
                if (rec.grade() < 10.0f) continue;

                EditStudent st = getStudent(rec.number());
                if (st == null) {
                    st = addStudent(rec.number(), rec.name());
                    students.put(rec.number(), st);
                    db.insertStudent(rec.number(), rec.name());
                }

                int tot_credits = st.getTotalCredits();

                // updates the avg grade :)
                st.setAvgGrade((st.getAvgGrade() * tot_credits + rec.grade() * cs.getCredits()) / (tot_credits + cs.getCredits()));
                st.setTotalCredits(tot_credits + cs.getCredits());
                db.insertEnroll(cs.getCourseID(), rec.number(), rec.grade());
            }

        }

        db.commit();
    }

    private EditStudent addStudent(int number, String name) {
        EditStudent st = new GMStudent()
                .setStudentName(name)
                .setStudentNumber(number)
                .setStudentEnrolls(createEnrollsProxy(number));
        students.put(number, st);
        studentsByOrder.add(st);
        topBoard.add(st);
        return st;
    }
}