import manager.GradesManager;
import manager.GradesManagerClass;
import manager.exceptions.*;
import others.Rank;
import others.Ranking;
import others.Statistic;
import others.evalHelper.SheetHelper;
import others.evalHelper.SheetHelperClass;
import subject.*;
import subject.evaluations.EvalEntry;
import subject.evaluations.EvalSheet;
import subject.exceptions.AlreadyEvaluatedException;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.NoSuchSubjectInStudentException;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    //Fist Message
    private static final String INIT = "By ©James Hertz\nWelcome to Grades Manager\n";
    private static final String DEFAULT_UPLOAD_FILE_NAME= "James.txt";

    //Messages
    private static final String UNKNOWN_MESSAGE = "Unknown Command. Type '%s' to see all commands\n";
    private static final String HELP_MESSAGE = "All Commands:";
    private static final String HELP_PRINT_MESSAGE = "%s - %s\n";
    private static final String SUBJECT_REQUEST_MESSAGE = "Subject: ";
    private static final String SUBJECT_PRINT_MESSAGE = "Evaluation Sheet of %s:\n";
    private static final String EVALUATIONS_HEAD_MESSAGE = "All evaluation sheets: ";

    //check this things later
    private static final String EVALUATIONS_ERROR_MESSAGE = "No Evaluation Sheet was added so far.";
    private static final String STUDENT_REQUEST_MESSAGE = "Student number: ";
    private static final String STUDENT_HEAD_MESSAGE = "\n# %d - %s\n";
    private static final String STUDENT_SUB_HEAD_MESSAGE = "All Evaluation: ";
    private static final String STUDENT_PRINT_MESSAGE = "\n>> %s - %s\n";
    private static final String STUDENTSUB_SUB_HEAD_MESSAGE = "All Evaluation from %s: \n";
    private static final String STUDENTS_HEAD_MESSAGE = "All students: ";
    private static final String STUDENTS_ERROR_MESSAGE = "No student was added so far.";
    private static final String STUDENTS_PRINT_MESSAGE = "%d - %s - %d evaluations\n";
    private static final String SUBJECTS_HEAD_MESSAGE = "All subjects:";
    private static final String SUBJECTS_PRINT_MESSAGE = "Subject: %s - %d Evaluation Sheet\n";
    private static final String SUBJECTS_ERROR_MESSAGE = "No subject was added so far.";
    private static final String RANKING_HEAD_MESSAGE = "Rank: \tNumber: \t  Name:\n";
    private static final String RANKING_PRINT_MESSAGE = "%03dº \t %d \t\t %s - G: %s\n";
    private static final String EVAL_SHEET_HEAD_MESSAGE = "Number: \t  Name:\n";
    private static final String EVAL_SHEET_PRINT_MESSAGE = "%d \t\t %s - G: %s\n";//"%d \t\t %s - G: %s\n";
    private static final String INVALID_NUMBER = "Invalid number";
    private static final String EVAL_SHEET_ID_REQUEST = "Evaluation Sheet id: ";
    private static final String EVAL_PRINT_MESSAGE = "id: %s; pos: %d; neg: %d; none: %d; total: %d\n";
    private static final String G_EXPLANATION = "G - grade; ", X_EXPLANATION= "X - Ausente, None, etc";
    private static final String STUDENT_NOT_FOUND = "Type '%s' to see all the students of the System\n";
    private static final String SUBJECT_NOT_FOUND = "Type '%s' to see all Subject of the System\n";
    private static final String EVAL_ID_NOT_FOUND = "Type '%s' to see all Evaluation Sheet of the System\n";
    private static final String EXIT_MESSAGE = "tururu";

    //Others
    private static final int SUBJECT_ID = 0, EVAL_ID = 1, FILE_NAME = 2;
    private static final String PROMPT = "\n>>> ";
    private static final String JAMES = "Your fault James";
    private static final String SEPARATOR = "\t", PATH = "./files/%s", PATH_O = "./object/%s";
    private static final String UPLOAD_MESSAGE = "%03d uploaded from %s. New students %d\n";
    private static final String GET_LINE_FORMATTER = "%s %s";
    private static final String INVALID_GRADE = "X", GRADE_FORMATTER = "%.1f";
    private static final String MANAGER = "Manager.obj";
    private static final String FILE_NOT_FOUND ="Missing '%s'\n";
    private static final String IO_MESSAGE = "Problems reading the file %s\n";
    private static final String ERROR = "ERROR!!\nContact the developer.";

    //evaluation from student by subject sort by better grades till worse grades
    //evalSheet just display the evaluations in a normal way
    private enum Commands {
        EVALUATION("list all Evaluation of a given Evaluation Sheet"),
        EVALUATIONS("list all Evaluation Sheet of the System" ),
        STUDENT("list all Evaluation of a given Student"),
        STUDENTS("list all Student of the System"),
        STUDENTSUB("list all Evaluations and some statistic of a given Student by Subject"),
        SUBJECT("list all Evaluation Sheet of a given Subject"),
        SUBJECTS("list all Subjects of the System"),
       // STATISTIC("just by now"),
        RANKING("list all Evaluation of given Evaluation Sheet by rank of better grades"),
        HELP("list all commands"),
        $JAMES(""),
        $HERTZ(""),
        EXIT("Shuts down the program"),
        UNKNOWN_COMMAND("");
        private final String description;
        Commands(String description) {
            this.description = description;
        }

        public boolean hidden(){
            return description.equals("");
        }
        public String description() {
            return description;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public static void main(String[] args) {
        GradesManager manager = new GradesManagerClass();
        //GradesManager manager = start();
        Scanner in = new Scanner(System.in);
        Commands cmd;
        System.out.println(INIT);
        help();
        do {
            System.out.print(PROMPT);
            cmd = command(in);
            switch (cmd) {
                case EVALUATIONS:
                    evaluations(manager);
                    break;
                case STUDENT:
                    student(in, manager);
                    break;
                case STUDENTSUB:
                    studentSub(in, manager);
                    break;
                case STUDENTS:
                    students(manager);
                    break;
                case SUBJECT:
                    subject(in, manager);
                    break;
                case SUBJECTS:
                    subjects(manager);
                    break;
                case $JAMES:
                    trickyThing(in, manager);
                    break;
                case $HERTZ:
                    hertz(in, manager);
                    break;
                case RANKING:
                    ranking(in, manager);
                    break;
              /*  case STATISTIC:
                    statistic(manager);
                    break;*/
                case EVALUATION:
                    evalSheet(in, manager);
                    break;
                case HELP:
                    help();
                    break;
                case EXIT:
                    System.out.println(EXIT_MESSAGE);
                    break;
                default:
                    System.out.printf(UNKNOWN_MESSAGE, Commands.HELP);
            }
        } while (cmd != Commands.EXIT);
        in.close();
    }

    private static Commands command(Scanner in) {
        try {
            return Commands.valueOf(in.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Commands.UNKNOWN_COMMAND;
        }
    }



    private static void evaluations(GradesManager manager) {
        Iterator<EvalSheet> it = manager.listAllSheets();
        if (it.hasNext()){
            System.out.println(EVALUATIONS_HEAD_MESSAGE);
            printEval(it);
        }else
            System.out.println(EVALUATIONS_ERROR_MESSAGE);

    }

    private static void student(Scanner in, GradesManager manager) {
        System.out.print(STUDENT_REQUEST_MESSAGE);
        try {
            int number = Integer.parseInt(in.nextLine().trim());
            Student student = manager.student(number);
            if(number == 61177)
            System.out.println(student);
            System.out.printf(STUDENT_HEAD_MESSAGE, student.number(), student.name());
            System.out.println(STUDENT_SUB_HEAD_MESSAGE);
            Iterator<SubjectSlot> it = student.listSubSlot();
            while(it.hasNext()){
                SubjectSlot slot = it.next();
                System.out.printf("\n# %s:\n", slot.subjectId());
                printStudentEvaluations(slot.listAllEvaluations());
            }
            //printStudentEvaluations(student.listEvaluations());
            printStatistic(student.statistic());

            System.out.printf("\n# - type '%s' to some statistic by Subject\n", Commands.STUDENTSUB);
        } catch (NumberFormatException e) {
            System.out.println(INVALID_NUMBER);
        }catch (NoAddedStudentException e) {
            System.out.println(STUDENTS_ERROR_MESSAGE);
        } catch (StudentDoesNotExistException e) {
            handleStudentDoesExistException(e);
        }
    }

    private static void printStudentEvaluations(Iterator<EvalEntry> it){
        while (it.hasNext()) {
            EvalEntry eval = it.next();
            System.out.printf(STUDENT_PRINT_MESSAGE, eval.evaluationsId(), formatGrade(eval.grade()));
        }
    }

    private static void studentSub(Scanner in, GradesManager manager){
        System.out.print(STUDENT_REQUEST_MESSAGE);
        String number = in.nextLine().trim();
        System.out.print(SUBJECT_REQUEST_MESSAGE);
        String subject = in.nextLine().trim().toUpperCase();
        try {
            int realNumber = Integer.parseInt(number);
            SubjectSlot slot = manager.studentSubjectSlot(realNumber, subject);
            Student student = slot.student();

            System.out.printf(STUDENT_HEAD_MESSAGE, student.number(), student.name());
            System.out.printf(STUDENTSUB_SUB_HEAD_MESSAGE, slot.subjectId());
            printStudentEvaluations(slot.listAllEvaluations());
            printStatistic(slot.statistic());
        } catch (NumberFormatException e) {
            System.out.println(INVALID_NUMBER);
        } catch(NoAddedStudentException  e){
            System.out.println(SUBJECTS_ERROR_MESSAGE);
        } catch (StudentDoesNotExistException e) {
            handleStudentDoesExistException(e);
        }catch(SubjectDoesNotExistException e) {
            handleSubjectDoesNotExistException(e);
        }catch( NoSuchSubjectInStudentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void students(GradesManager manager) {
        Iterator<Student> it = manager.listAllStudents();
        if (it.hasNext())
            System.out.println(STUDENTS_HEAD_MESSAGE);
        else
            System.out.println(STUDENTS_ERROR_MESSAGE);
        while (it.hasNext()) {
            Student student = it.next();
            System.out.printf(STUDENTS_PRINT_MESSAGE, student.number(), student.name(), student.numberOfEvaluation());
        }
    }

    private static void subject(Scanner in, GradesManager manager) {
        System.out.print(SUBJECT_REQUEST_MESSAGE);
        String subject = in.nextLine().trim().toUpperCase();
        try{
            Subject tmp = manager.subject(subject);
            Iterator<EvalSheet> it =tmp.listEvaluations();
            System.out.printf(SUBJECT_PRINT_MESSAGE, subject);
            printEval(it);
            printStatistic(tmp.statistic());
        }catch (NoAddedSubjectException e) {
            System.out.println(SUBJECTS_ERROR_MESSAGE);
        }catch(SubjectDoesNotExistException e) {
            handleSubjectDoesNotExistException(e);
        }
    }

    private static void subjects(GradesManager manager) {
        Iterator<Subject> it = manager.listAllSubjects();
        if (it.hasNext())
            System.out.println(SUBJECTS_HEAD_MESSAGE);
        else
            System.out.println(SUBJECTS_ERROR_MESSAGE);
        while (it.hasNext()) {
            Subject subject = it.next();
            System.out.printf(SUBJECTS_PRINT_MESSAGE, subject.id(), subject.numberOfEvalSheet());
        }
    }

    //------------------------------------------------------------------------------------
    private static void trickyThing(Scanner in, GradesManager manager) {
        String fileName = in.nextLine();
        if(fileName.equals("")){
            fileName = DEFAULT_UPLOAD_FILE_NAME;// "James.txt";//in.nextLine();
        }
        try {
            Scanner input = new Scanner(new FileReader(fileName));
            while (input.hasNextLine()) {
                myStuff(input.nextLine().split(SEPARATOR), manager);
            }

        }catch(AlreadyEvaluatedException | EvaluationAlreadyExistsException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.printf("Problems reading the file: %s.\nError", e.getMessage());

        }
    }

    private static void myStuff(String[] names, GradesManager manager) throws IOException, AlreadyEvaluatedException, EmptyBufHelperException, EvaluationAlreadyExistsException {
        int newEval = 0, newSt;
        try {
            Scanner input = new Scanner(new FileReader(String.format(PATH, names[FILE_NAME])));
            while (input.hasNextLine()) {
                int number = input.nextInt();
                String name = getLine(input);
                float grade = input.nextFloat();
                manager.addEvalEntry(number, name, grade);
                newEval++;
            }
            input.close();
        }catch(IOException e){
            throw new IOException(names[FILE_NAME]);
        }
        newSt = manager.commitBufHelper(names[SUBJECT_ID], names[EVAL_ID]);

        System.out.printf(UPLOAD_MESSAGE, newEval, names[SUBJECT_ID], newSt);
    }

    private static String getLine(Scanner in) {
        String name = "";
        while (!in.hasNextFloat())
            name = String.format(GET_LINE_FORMATTER, name, in.next());
        return name;
    }

    private static void hertz(Scanner in, GradesManager manager){
        try{
            final String fileName = in.nextLine().trim();
            ObjectOutputStream output = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(String.format(PATH_O, fileName))));
            output.writeObject(manager);
            output.flush();
            output.close();
            System.out.println("Done!!");
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Your fault Hertz");
        }
    }

    private static GradesManager start(){
        try{
            ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(MANAGER)));
            GradesManager manager = (GradesManager) input.readObject();
            input.close();
            return manager;
        }catch(FileNotFoundException e){
            System.out.printf(FILE_NOT_FOUND, MANAGER);
        }catch(IOException e){
            System.out.printf(IO_MESSAGE, MANAGER);
        }catch(Exception e){
            System.out.println(ERROR);
        }
        System.exit(-1);
        return null;
    }

    //-----------------------------------------------------------------------------------------

    private static void ranking(Scanner in, GradesManager manager) {
        System.out.print(EVAL_SHEET_ID_REQUEST);
        String evalId = in.nextLine().trim();
        try {
            EvalSheet evalSheet = manager.evalSheet(evalId);
            Iterator<EvalEntry> it = evalSheet.ranking();
            Rank rank = new Ranking();//Thinking about this...
            System.out.println(RANKING_HEAD_MESSAGE);
            while (it.hasNext()) {
                EvalEntry eval = it.next();
                Student student = eval.student();
                float grade = eval.grade();
                System.out.printf(RANKING_PRINT_MESSAGE, rank.rank(grade), student.number(), student.name(), formatGrade(grade));
            }
            printExplanations();
            printStatistic(evalSheet.statistic());
        } catch (EvalSheetDoesNotExistException e) {
            handleEvalSheetDoesNotExitException(e);
        } catch (NoAddedEvalSheetException e) {
            System.out.println(EVALUATIONS_ERROR_MESSAGE);
        }
    }

    private static void evalSheet(Scanner in, GradesManager manager) {
        System.out.print(EVAL_SHEET_ID_REQUEST);
        String evalId = in.nextLine().trim();
        try{
            EvalSheet evalSheet = manager.evalSheet(evalId);
            Iterator<EvalEntry> it = evalSheet.listByAlphabeticalOrder();//Chech it lat
            //What if a evalSheet does not have any evaluation
            System.out.println(EVAL_SHEET_HEAD_MESSAGE);
            while(it.hasNext()){
                EvalEntry eval = it.next();
                Student student = eval.student();
                System.out.printf(EVAL_SHEET_PRINT_MESSAGE, student.number(), student.name(), formatGrade(eval.grade()));
            }
            printExplanations();
            printStatistic(evalSheet.statistic());

        } catch (NoAddedEvalSheetException e) {
            System.out.println(EVALUATIONS_ERROR_MESSAGE);
        }catch(EvalSheetDoesNotExistException e){
            handleEvalSheetDoesNotExitException(e);
        }
    }

    private static void symbols(){
        System.out.println("id - Evaluation Sheet id");
        System.out.println("none - same meaning as 'X'");
        System.out.println("pos  - positive grade");
        System.out.println("neg - negative grade");
        System.out.println("total - total evaluations");
        System.out.println(X_EXPLANATION);
        System.out.println(G_EXPLANATION);

    }

    private static void help() {
        System.out.println(HELP_MESSAGE);
        for (Commands c : Commands.values())
            if (!c.hidden())
                System.out.printf(HELP_PRINT_MESSAGE, c, c.description());


    }
    private static void printEval(Iterator<EvalSheet> it){
        while(it.hasNext()){
            EvalSheet sheet = it.next();
            Statistic tmp = sheet.statistic();
            System.out.printf(EVAL_PRINT_MESSAGE, sheet.evaluationId(),
                    tmp.positiveData(), tmp.negativesData(), tmp.noneData(), tmp.numberOfData());
        }
    }

    private static void printExplanations(){
        System.out.println();
        System.out.println(G_EXPLANATION + X_EXPLANATION);
    }


    private static String formatGrade(float grade) {
        if (grade == -1f)
            return INVALID_GRADE;
        return String.format(GRADE_FORMATTER, grade);

    }

    /*

    private static void statistic(GradesManager manager){
        Iterator<Student> it = manager.statistic();
        if(!it.hasNext())
            System.out.println(STUDENTS_ERROR_MESSAGE);
        else{
            System.out.println("Rank: \tNumber: \t  Name:\n");
            Rank rank = new Ranking();
            while(it.hasNext()){
                Student student = it.next();
                float grade = student.averageGrade();
                System.out.printf("%03dº \t %d \t\t %s - AG: %s - EV: %d\n", rank.rank(grade),
                        student.number(), student.name(), formatGrade(grade), student.numberOfEvaluation());

            }

        }

    }
     */

    private static void printStatistic(Statistic statistic){
        int total = statistic.numberOfData();
        System.out.println("\nStatistic: ");
        System.out.printf("Positive grade: %d/%d - %.2f%%\n", statistic.positiveData(),total, statistic.positivePercent());
        System.out.printf("Negative grade: %d/%d - %.2f%%\n", statistic.negativesData(), total, statistic.negativePercent());
        System.out.printf("None grade: %d/%d - %.2f%%\n", statistic.noneData(), total, statistic.nonePercent());
        System.out.printf("Average grade: %.2f\n", statistic.averageData());
        System.out.printf("Total evaluations: %d\n", total);
    }


    private static void handleStudentDoesExistException(StudentDoesNotExistException e){
        System.out.println(e.getMessage());
        System.out.printf(STUDENT_NOT_FOUND, Commands.STUDENTS);
    }

    private static void handleSubjectDoesNotExistException(SubjectDoesNotExistException e){
        System.out.println(e.getMessage());
        System.out.printf(SUBJECT_NOT_FOUND, Commands.SUBJECTS);

    }

    private static void handleEvalSheetDoesNotExitException(EvalSheetDoesNotExistException e){
        System.out.println(e.getMessage());
        System.out.printf(EVAL_ID_NOT_FOUND, Commands.EVALUATIONS);
    }

}
