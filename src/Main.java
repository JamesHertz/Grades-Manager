import jh.grades.manager.GradesManager;
import jh.grades.manager.MyManager;
import jh.grades.manager.SimpleStudent;
import jh.grades.manager.Student;

import java.util.Iterator;
import java.util.Scanner;

public class Main {
    //Fist Message
    private static final String INIT_MESSAGE = "By ©James Hertz\nWelcome to Grades Manager\n";

    // adhoc commands
    private static final String LIST_STUDENTS = "listStudents";
    private static final String TOP = "top";
    public static final String QUIT = "quit";

    public static final String PROMPT = ">> ";

    public static void main(String[] args) {
        System.out.println(INIT_MESSAGE);
        run_commands();
    }

    private static void top_board(Scanner in, GradesManager manager){
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
        Iterator<SimpleStudent> students = manager.listAllStudents();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            System.out.println("NUMBER  NAME");
            while(students.hasNext()){
                SimpleStudent st = students.next();
                System.out.printf("%6d  %s\n", st.number(), st.name());
            }
        }
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
                case QUIT:
                    System.out.println("bye bye");
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        }while (!cmd.equals(QUIT));
    }
}
