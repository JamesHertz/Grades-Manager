import jh.grades.manager.*;
import jh.grades.uploader.CourseInfo;
import jh.grades.uploader.FileUploadInfo;
import jh.grades.uploader.UploadInfo;

import javax.sound.midi.Soundbank;
import java.util.Iterator;
import java.util.Scanner;
import static jh.grades.manager.Semesters.*;

public class Main {
    // TODO: have a mini animation playing on the background (writing loading on the screen)
    //Fist Message
    private static final String INIT_MESSAGE = "By ©James Hertz\nWelcome to Grades Manager\n";

    // adhoc commands
    private static final String LIST_STUDENTS = "students";
    private static final String TOP = "top";
    public static final String QUIT = "quit";

    public static final String PROMPT = ">> ";

    public static void main(String[] args) {
        System.out.println(INIT_MESSAGE);
        run_commands();
    }

    private static void top_board(Scanner in, GradesManager manager){
        // TODO: top <init?> <end?> or better saying top range :)
        in.nextLine();
        Iterator<Student> students = manager.top();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            // TODO: check this thing out later
            System.out.printf("%5s %5s  %-6s  %-50s %-7s %s\n", "COUNT", "RANK", "NUMBER", "NAME", "GRADE", "CREDITS");
            int count =1, rank = 1;
            Float lastGrade = null;
            while(students.hasNext()){
                Student st = students.next();

                if(lastGrade == null) lastGrade = st.averageGrade();
                if(lastGrade != st.averageGrade()){
                    lastGrade = st.averageGrade();
                    rank++;
                }
                // TODO: automatize this...
                System.out.printf("%5d %4d°  %6d  %-50s %5.2f - %3d\n", count, rank, st.number(), st.name(), st.averageGrade(), st.totalCredits());
                count++;

            }
        }

    }

    private static void list_students(Scanner in, GradesManager manager){
        in.nextLine();
        Iterator<Student> students = manager.listAllStudents();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            System.out.println("NUMBER  NAME");
            while(students.hasNext()){
                Student st = students.next();
                System.out.printf("%6d  %s\n", st.number(), st.name());
            }
        }
    }

    private static void print_enrolls(Scanner in, GradesManager manager){
        int number = in.nextInt();
        in.nextLine();

        if(number < 0){
            System.out.println("Invalid number, it should be greater than 0");
            return;
        }

        Student st = manager.getStudent(number);
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

    private static String format_ord_num(int num){
        switch (num){
            case 1:
                return "1st";
            case 2:
                return "2nd";
            default:
                return num + "th";
        }
    }
    private static void print_courses(Scanner in, GradesManager manager){
        in.nextLine();
        // TODO: print courses by year and by semester
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
        return in.nextInt(); // TODO: work to be done later :)
    }

    private static void upload_enrolls(Scanner in, GradesManager manager){
        String course_id = in.next();
        CourseInfo c_info = null;

       // if(manager.getCourse(course_id) == null){
       //     // get's the course information
       // }

        UploadInfo up_info;

        switch (choose_upload_method(in)) {
            case FILE_UPLOAD -> {
                System.out.println("filename: ");
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
        System.out.println("Uploading the files:");



    }

    private static void run_commands(){
        Scanner in = new Scanner(System.in);
        GradesManager manager = new MyManager();
        String cmd;
        do{
            System.out.print(PROMPT);
            cmd = in.next(); // do later
            switch (cmd){
                case LIST_STUDENTS:
                    list_students(in, manager);
                    break;
                case TOP:
                    top_board(in, manager);
                    break;
                case "enrolls":
                    print_enrolls(in, manager);
                    break;
                case "courses":
                    print_courses(in, manager);
                    break;
                case "upload":
                    upload_enrolls(in, manager);
                    break;
                case QUIT:
                    System.out.println("bye bye");
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
                    in.nextLine();
            }
        }while (!cmd.equals(QUIT));
    }
}
