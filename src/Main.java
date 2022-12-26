import jh.grades.manager.GradesManager;
import jh.grades.manager.MyManager;
import jh.grades.manager.SimpleStudent;

import java.util.Iterator;
import java.util.Scanner;

public class Main {
    //Fist Message
    private static final String INIT_MESSAGE = "By ©James Hertz\nWelcome to Grades Manager\n";

    // adhoc commands
    private static final String LIST_STUDENTS = "listStudents";
    public static final String QUIT = "quit";

    public static void main(String[] args) {
        System.out.println(INIT_MESSAGE);
        run_commands();
    }

    private static void list_students(Scanner in, GradesManager manager){
        in.nextLine();
        Iterator<SimpleStudent> students = manager.listAllStudents();
        if(!students.hasNext())
            System.out.println("No students!");
        else {
            System.out.println("students: ");
            while(students.hasNext()){
                SimpleStudent st = students.next();
                System.out.printf("%6d %s\n", st.number(), st.name());
            }
        }

    }
    private static void run_commands(){
        Scanner in = new Scanner(System.in);
        GradesManager manager = new MyManager();
        String cmd;
        do{
            cmd = in.next();
            switch (cmd){
                case LIST_STUDENTS:
                    list_students(in, manager);
                case QUIT:
                    System.out.println("bye bye");
            }
        }while (!cmd.equals(QUIT));
    }
}
