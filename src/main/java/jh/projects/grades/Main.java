package jh.projects.grades;

import jh.projects.grades.manager.*;

// cli-parser imports
import jh.projects.cliparser.cliApp.CliApp;
import jh.projects.cliparser.cliApp.SimpleCliApp;
import jh.projects.cliparser.cliApp.annotations.CliAppCommand;
import jh.projects.cliparser.cliApp.api.CliAPI;
import jh.projects.cliparser.cliApp.api.table.CliTable;
import jh.projects.cliparser.cliApp.listeners.CliRunListener;
import jh.projects.grades.manager.exceptions.GMException;
import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.uploader.CourseUploader;
import jh.projects.grades.uploader.exceptions.UploadException;

import java.util.Iterator;

import static java.lang.String.format;
import static jh.projects.grades.manager.Semesters.*;

public class Main implements CliRunListener {
    // TODO: have a mini animation playing on the background (writing loading on the screen)
    private static final String INIT_MESSAGE = "By ©James Hertz\nWelcome to Grades Manager\n";
    private GradesManager manager;
    /*
        TODO: add this dependencies below:
                -> implementation group: 'org.xerial', name: 'sqlite-jdbc', version: '3.40.1.0'
                -> implementation group: 'org.jsoup', name: 'jsoup', version: '1.14.4'
                -> cli-parser :)
     */

    @Override
    public void onRun(CliAPI api) {
        System.out.println(INIT_MESSAGE);
        this.manager = new Manager();
    }

    @CliAppCommand(
            key = "top",
            desc = "lists the students by sorted by credits and then by average grade"
    )
    public void topBoard(CliAPI api){
        Iterator<Student> students = manager.top();
        if(!students.hasNext())
            System.out.println("No students!");
        else{
            int count =1, rank = 1;
            Float lastGrade = null;
            String[] headers = {"COUNT", "RANK", "NUMBER", "NAME", "GRADE", "CREDITS"};
            CliTable table = api.createTable(headers);

            while(students.hasNext()){
                Student st = students.next();

                if(lastGrade == null) lastGrade = st.getAvgGrade();
                if(lastGrade != st.getAvgGrade()){
                    lastGrade = st.getAvgGrade();
                    rank++;
                }
                table.add(format("%3d", count++), format("%3d°", rank), st.getNumber(),
                        st.getName(), format("%.2f", st.getAvgGrade()), format("%5d", st.getTotalCredits()));
            }

            table.print();
        }
    }

    @CliAppCommand(
            key = "students",
            desc = "lists all students"
    )
    public void listStudents(CliAPI api){
        Iterator<Student> students = manager.listAllStudents();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            CliTable table = api.createTable(new String[]{ "NUMBER", "NAME"});
            while(students.hasNext()){
                Student st = students.next();
                table.add(st.getNumber(), st.getName());
            }
            table.print();
        }
    }

    @CliAppCommand(
            key = "enrolls",
            desc = "lists all the course that the given student is enrolled in"
    )
    public void printEnrolls(int student_number){
        Student st = manager.getStudent(student_number);
        if(st == null) {
            System.out.println("No such student");
        }else{
            System.out.printf("Student: %s - %d\n", st.getName(), st.getNumber());
            Iterator<Enrollment> enrolls = st.getEnrollments();

            if(!enrolls.hasNext()) System.out.println("No enrollments");
            else{
                while(enrolls.hasNext()){
                    Enrollment aux = enrolls.next();

                    Course course = aux.getCourse();
                    System.out.printf("> %s - (%s)\n",course.getCourseID(), course.getName());
                    System.out.printf("final-grade: %.2f\n\n", aux.grade());
                }
            }
            // have a command named student that prints statistics about him
        }
    }

    private String semToString(Semesters sem){
        return (sem == THIRD_TRIMESTER) ? "3rd trimester" : format_ord_num(sem.getId()) + " semester";
    }

    @CliAppCommand(
            key = "courses",
            desc = "lists all courses"
    )
    public void printCourses(){
        Iterator<Course> courses = manager.listAllCourses();

        if(!courses.hasNext())
            System.out.println("No courses");
        else{
            while(courses.hasNext()){
                Course cs = courses.next();

                System.out.printf("# %s (%s)\n", cs.getCourseID(), cs.getName());
                System.out.println("credits: " + cs.getCredits());
                System.out.printf("period: %s year - %s\n", format_ord_num(cs.getYear()), semToString(cs.getSemester()));
                System.out.println();
           }
        }

        // try to print the courses by year and by semester
        // then by id number
    }

    @CliAppCommand(
            desc = "uploads enrollments to Grades Manager"
    )
    public void upload(CliAPI api, String filename){ // this is fun :)
        try{
            System.out.printf("%d new courses uploaded\n", manager.uploadCourses(filename));
        }catch (GMException | UploadException e){
            System.out.println("Error uploading courses: " + e.getMessage());
        }
    }

    public void update(String mode){

    }

    public void login(){

    }

     private String format_ord_num(int num){
        return switch (num % 10) {
            case 1 -> "1st";
            case 2 -> "2nd";
            case 3 -> "3rd";
            default -> num + "th";
        };
    }


    /*

        -> JSON parser
        -> configuration
        -> login command
        -> update command
     */
    // api.prompt(String, type1, type2, type3); -> return next string
    // api.prompt();
    // api.prompt(type 1, type2) -> parses the line and returns an a

    /*
        How to insert data into my program?
        Which commands should I have?
        IDEAS:
            -> I need something  that goes to my school website takes the html
               parses it and upload the grades to my database.
            -> But that works just for final grades.
            -> Well I am just supporting final grades right now.
            -> But I somehow need to have another way of uploading the grades.
     */
    /*
     // CLASSS DONE TO TEST THE DATA_BAASE MODULE
    static class SimpleDBInter implements CliRunListener{
        DataBase db;
        @Override
        public void onRun(CliAPI cliAPI) {
            System.out.println("Creating db...");
            db = new GMDataBase("dbs/file.db");
        }
        @CliAppCommand
        public void listStudents(CliAPI api){
            Iterator<DataBase.RawStudent> st = db.getAllStudents();
            if(!st.hasNext())
                System.out.println("No students :(");
            else{
                CliTable table = api.createTable(new String[]{"NUMBER", "NAME", "AVG-GRADE", "CREDITS"});
                while(st.hasNext()){
                    var aux = st.next();
                    table.add(aux.number(), aux.name(), format("%.2f", aux.avgGrade()), aux.number());
                }
                table.print();
            }
        }
        @CliAppCommand
        public void listCourse(CliAPI api){
            Iterator<DataBase.RawCourse> cs = db.getAllCourses();
            if(!cs.hasNext())
                System.out.println("No course!!");
            else {
                CliTable table = api.createTable(new String[]{"COURSE-ID", "CREDITS"});
                while(cs.hasNext()){
                    var aux = cs.next();
                    table.add(aux.id(), aux.credits());
                }
                table.print();
            }
        }
        @CliAppCommand
        public void studentEnrolls(CliAPI api, int studentNumber){
            listEnrolls(api, db.getEnrolls(studentNumber));
        }
        @CliAppCommand
        public void courseEnrolls(CliAPI api, String courseID){
           listEnrolls(api, db.getEnrolls(courseID));
        }
        private void listEnrolls(CliAPI api, Iterator<DataBase.RawEnroll> enrolls){
            if(!enrolls.hasNext())
                System.out.println("No enrolls :(");
            else{
                CliTable table = api.createTable(new String[]{"COURSE-ID", "STUDENT-NUMBER", "GRADE"});
                while(enrolls.hasNext()){
                    var aux = enrolls.next();
                    table.add(aux.courseID(), aux.studentNumber(), aux.grade());
                }
                table.print();
            }
        }
        @CliAppCommand
        public void addStudent(int number, String name){
            db.insertStudent(number, name);
            System.out.println("Inserted!!!");
        }
        @CliAppCommand
        public void addCourse(String courseID, int credits){
            db.insertCourse(courseID, "", credits, 1, 1);
            System.out.println("Inserted!!!");
        }

        @CliAppCommand
        public void addEnroll(int studentNumber, String courseID, float grade){
            db.insertEnroll(courseID, studentNumber, grade);
            System.out.println("Inserted");
        }
    };
     */

    public static void main(String[] args) {
        CliApp app = new SimpleCliApp(new Main());
        app.run();
    }
}
