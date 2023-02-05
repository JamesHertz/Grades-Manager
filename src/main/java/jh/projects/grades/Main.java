package jh.projects.grades;

import jh.projects.grades.manager.*;
import jh.projects.grades.uploader.CourseInfo;
import jh.projects.grades.uploader.FileUploadInfo;
import jh.projects.grades.uploader.UploadInfo;

import jh.projects.cliparser.cliApp.CliApp;
import jh.projects.cliparser.cliApp.SimpleCliApp;
import jh.projects.cliparser.cliApp.annotations.CliAppCommand;
import jh.projects.cliparser.cliApp.api.CliAPI;
import jh.projects.cliparser.cliApp.api.CliTable;
import jh.projects.cliparser.cliApp.listeners.CliRunListener;

import java.util.Iterator;
import java.util.Scanner;

import static java.lang.String.format;
import static jh.projects.grades.manager.Semesters.*;

public class Main implements CliRunListener {
    // TODO: have a mini animation playing on the background (writing loading on the screen)
    private static final String INIT_MESSAGE = "By ©James Hertz\nWelcome to Grades Manager\n";

    private GradesManager manager;

    @Override
    public void onRun(CliAPI api) {
        System.out.println(INIT_MESSAGE);
        this.manager = new MyManager();
    }

    @CliAppCommand(
            key = "top",
            desc = "lists the students by sorted by credits and then by average grade"
    )
    public void top_board(CliAPI api){
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

                if(lastGrade == null) lastGrade = st.averageGrade();
                if(lastGrade != st.averageGrade()){
                    lastGrade = st.averageGrade();
                    rank++;
                }
                table.add(format("%3d", count++), format("%3d°", rank), st.number(),
                        st.name(), st.averageGrade(), st.totalCredits());
            }

            table.print();
        }
    }

    @CliAppCommand(
            key = "students",
            desc = "lists all students"
    )
    public void list_students(CliAPI api){
        Iterator<Student> students = manager.listAllStudents();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            CliTable table = api.createTable(new String[]{ "NUMBER", "NAME"});
            while(students.hasNext()){
                Student st = students.next();
                table.add(st.number(), st.name());
            }
            table.print();
        }
    }

    @CliAppCommand(
            key = "enrolls",
            desc = "lists all the course that the given student is enrolled in"
    )
    public void print_enrolls(int student_number){
        Student st = manager.getStudent(student_number);
        if(st == null) {
            System.out.println("No such student");
        }else{
            System.out.printf("Student: %s - %d\n", st.name(), st.number());
            Iterator<Enrollment> enrolls = st.getEnrollments();

            if(!enrolls.hasNext()) System.out.println("No enrollments");
            else{
                while(enrolls.hasNext()){
                    Enrollment aux = enrolls.next();

                    Course course = aux.getCourse();
                    System.out.printf("> %s - (%s)\n",course.id(), course.name());
                    System.out.printf("final-grade: %.2f\n\n", aux.grade());
                }
            }
        }
    }

    @CliAppCommand(
            key = "courses",
            desc = "lists all courses"
    )
    public void print_courses(){
        Iterator<Course> courses = manager.listAllCourses();

        if(!courses.hasNext())
            System.out.println("No courses");
        else{
            while(courses.hasNext()){
                Course cs = courses.next();
                System.out.printf("# %s (%s)\n", cs.id(), cs.name()); // heading
                System.out.printf("credits: %d\n", cs.credits());
                System.out.printf("period: %s year - ", format_ord_num(cs.year()));

                Semesters sem = cs.semester();
                if(sem == THIRD_TRIMESTER)
                    System.out.println("3rd trimester");
                else
                    System.out.println(format_ord_num(sem.getId()) + " semester");

                System.out.println();
            }
        }

        // try to print the courses by year and by semester
        // then by id number
    }
    public void upload(){
        System.out.println("Hi there you are uploading :)");
    }

     private String format_ord_num(int num){
        return switch (num) {
            case 1 -> "1st";
            case 2 -> "2nd";
            default -> num + "th";
        };
    }
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


    private static final int FILE_UPLOAD = 0;
    private static final int URL_UPLOAD = 1;

    private static int choose_upload_method(Scanner in){
        System.out.println("Choose upload type: ");
        System.out.printf("%d - file upload\n", FILE_UPLOAD);
        System.out.printf("%d - url upload\n", URL_UPLOAD);
        System.out.print("your choice: ");

        return Integer.parseInt(in.nextLine());
    }


    private static CourseInfo get_courseInfo(Scanner in){
        System.out.println("name: ");
        System.out.println("semester: ");
        System.out.println("year: ");
        System.out.println("credits: ");
        return null;
    }

    // upload command :)
    private static void upload_enrolls(Scanner in, GradesManager manager){
        String course_id = in.nextLine().trim();

        UploadInfo up_info;

        switch (choose_upload_method(in)) {
            case FILE_UPLOAD -> {
                System.out.print("filename: ");
                String filename = in.nextLine().trim();
                up_info = new FileUploadInfo(course_id, filename);
            }
            case URL_UPLOAD -> {
                System.out.println("Not supported yet :(");
                return;
            }
            default -> {
                System.out.println("Invalid upload method");
                return;
            }
        }

        // todo: should I stop and complete my framework??
        if(manager.getCourse(course_id) == null){
            System.out.println("Course doesn't exist.");
            System.out.println("----------------------------------------------");
            System.out.println("Adding course ... Fill the course info below:");
            System.out.println("----------------------------------------------");

            CourseInfo cs_info = get_courseInfo(in);
            if(cs_info == null) return;
            up_info.setCourseInfo(cs_info);
        }
        System.out.println("Uploading the files!!");
        // done :)
        manager.uploadEnrolls(up_info);
    }

    public static void main(String[] args) {
        CliApp app = new SimpleCliApp(new Main());
        app.run();
    }
}
