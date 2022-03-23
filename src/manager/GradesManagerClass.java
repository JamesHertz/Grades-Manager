package manager;

import manager.exceptions.*;
import others.AvgGradeComp;
import others.StudentByAlphOrder;
import others.evalHelper.EvalHelper;
import others.evalHelper.SheetHelper;
import others.evalHelper.SheetHelperClass;
import subject.*;
import subject.evaluations.EvalSheet;
import subject.exceptions.AlreadyEvaluatedException;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.FinalGradeAlreadySetException;
import subject.exceptions.NoSuchSubjectInStudentException;

import java.io.Serializable;
import java.util.*;

public class GradesManagerClass implements GradesManager, Serializable {
    /*
        TODO:
            -> add some methods for helper
            -> clear the others
            -> test it
     */

    public static final String REPLACEMENT = "-"; // think about this later
    private static final String DEFAULT_ID = "%s-%s", SPACE = " ", REGEX = "\\s{2,}";

    private static final int DEFAULT_SIZE = 25;
    private static final int STUDENTS_DEFAULT_SIZE = 200; // think about this
    private static final long serialVersionUID = 0L;
    private final SortedMap<String, Subject> subjects;
    private final Map<Integer, Student> students;
    // I gonna key you out dude :>
    private final Map<String, EvalSheet> evaluations;// this is quite a very interesting thing
    private final SortedSet<Student> studentsByAlphOrder;
    private final List<EvalSheet> evalSheet; // still thinking about this
    private final SheetHelper bufHelper;
    private final List<Student> studentList;
    private int newStudents;
    //private final List<Student> statistic;

    public GradesManagerClass(){
        subjects = new TreeMap<>();
        students = new HashMap<>(STUDENTS_DEFAULT_SIZE);
        evaluations = new HashMap<>(DEFAULT_SIZE);
        studentsByAlphOrder = new TreeSet<>(new StudentByAlphOrder());
        evalSheet = new ArrayList<>(DEFAULT_SIZE);
        bufHelper = new SheetHelperClass();
        studentList = new LinkedList<>();
        newStudents = 0;
        //statistic = new ArrayList<>();
    }


    @Override
    public int commitBufHelper(String subjectId, String evalId) throws EmptyBufHelperException, EvaluationAlreadyExistsException, SubjectDoesNotExistException {
        if(bufHelper.isEmpty())
            throw new EmptyBufHelperException();
        Subject sub = findSubject(subjectId);
        String realEvalId = formatId(subjectId, evalId);

        if(evaluations.containsKey(realEvalId))
            throw new EvaluationAlreadyExistsException(subjectId, evalId);

        // updates internal dataStructures
        EvalSheet aux = sub.addEvaluation(realEvalId, bufHelper);
        evaluations.put(realEvalId, aux);
        evalSheet.add(aux);

        // change this thing later :)
        int tmp = newStudents;
        clearBufHelper();
        return tmp;
    }

    @Override
    public int commitFinal(String subjectId) throws SubjectDoesNotExistException, FinalGradeAlreadySetException {
        if(bufHelper.isEmpty())
            throw new EmptyBufHelperException();
        Subject sub = subjects.get(subjectId);
        if(sub == null)
            throw new SubjectDoesNotExistException(subjectId);
        sub.addFinalEvaluation(bufHelper);

        // change this thing later
        int tmp = newStudents;
        clearBufHelper();
        return tmp;
    }

    @Override
    public Subject createSubject(String subId, int ects) throws SubjectDoesNotExistException {
        if(subjects.containsKey(subId))
            throw new SubjectDoesNotExistException(subId);
        Subject sub = new SubjectClass(subId, ects);
        subjects.put(subId,sub);
        return sub;
    }

    @Override
    public void clearBufHelper() {
        newStudents = 0;
        bufHelper.clear();
    }

    @Override
    public Iterator<EvalHelper> listBufHelper() {
        return bufHelper.listAllHelper();
    }

    @Override
    public void addEvalEntry(int studentNumber, String name, float eval) throws AlreadyEvaluatedException {
       Student st = getStudent(studentNumber, name); // still I'm gonna think about it :D
       bufHelper.addEvalHelper(st, eval);
    }

    @Override
    public void addEvalEntry(int studentNumber, float eval) throws StudentDoesNotExistException, AlreadyEvaluatedException {
        Student st = findStudent(studentNumber);
        bufHelper.addEvalHelper(st, eval);
    }


    private Student getStudent(int number, String name){
        Student student = students.get(number);
        if(student == null){
            student = new StudentClass(number, name);
            students.put(number, student);
            studentsByAlphOrder.add(student);
            studentList.add(student);
            newStudents++;
            //statistic.add(student);
        }
        return student;
    }

    private Student findStudent(int number) throws StudentDoesNotExistException {
        Student student = students.get(number);
        if(student == null)
            throw new StudentDoesNotExistException(number);
        return student;
    }

    private Subject findSubject(String subjectId) throws SubjectDoesNotExistException {
        Subject subject = subjects.get(subjectId);
        if(subject == null){
          throw new SubjectDoesNotExistException(subjectId);
        }
        return subject;
    }

    @Override
    public Iterator<EvalSheet> listAllSheets() {
        return evalSheet.iterator();
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return studentsByAlphOrder.iterator();
    }

    @Override
    public Subject subject(String subjectId) throws NoAddedSubjectException, SubjectDoesNotExistException{
        if(subjects.isEmpty())
            throw new NoAddedSubjectException();
        Subject tmp = subjects.get(subjectId);
        if(tmp == null)
            throw new SubjectDoesNotExistException(subjectId);
        return tmp;
    }


    @Override
    public Iterator<Student> top() {
        studentList.sort(new AvgGradeComp());
        return studentList.listIterator();
    }

    @Override
    public Iterator<Subject> listAllSubjects() {
        return subjects.values().iterator();
    }

    @Override
    public Student student(int number) throws  NoAddedStudentException , StudentDoesNotExistException{
        if(students.isEmpty())
            throw new NoAddedStudentException();
        Student student = students.get(number);
        if(student == null)
            throw new StudentDoesNotExistException(number);
        return student;
    }

    /*
    @Override
    public Iterator<Student> statistic() {
        statistic.sort(new ByStatistic());
        return statistic.iterator();
    }
     */

    @Override
    public SubjectSlot studentSubjectSlot(int studentNumber, String subjectId) throws NoAddedStudentException, StudentDoesNotExistException, SubjectDoesNotExistException, NoSuchSubjectInStudentException {
        Student student = student(studentNumber);
        if(!subjects.containsKey(subjectId))
            throw new SubjectDoesNotExistException(subjectId);
        return student.subjectSlot(subjectId);
    }

    @Override
    public EvalSheet evalSheet(String evalId) throws NoAddedEvalSheetException, EvalSheetDoesNotExistException{
        if(evaluations.isEmpty())
            throw new NoAddedEvalSheetException();
        EvalSheet eval = evaluations.get(evalId);
        if(eval == null)
            throw new EvalSheetDoesNotExistException(evalId);
        return eval;
    }

    private String formatId(String subjectId, String evalId){
        String realId = evalId.replaceAll(REGEX, SPACE).trim(); // search for it later
        return String.format(DEFAULT_ID, subjectId, realId.replace(SPACE, REPLACEMENT));
    }
}
