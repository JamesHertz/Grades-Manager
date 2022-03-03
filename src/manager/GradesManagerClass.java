package manager;

import manager.exceptions.*;
import others.StudentByAlphOrder;
import others.evalHelper.EvalHelper;
import others.evalHelper.SheetHelper;
import others.evalHelper.SheetHelperClass;
import subject.*;
import subject.evaluations.EvalSheet;
import subject.evaluations.EvaluationSheet;
import subject.exceptions.AlreadyEvaluatedException;
import subject.exceptions.EvaluationAlreadyExistsException;
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

    private static final int DEFAULT_SIZE = 25;
    private static final int STUDENTS_DEFAULT_SIZE = 200; // think about this
    private static final long serialVersionUID = 0L;
    private final SortedMap<String, Subject> subjects;
    private final Map<Integer, Student> students;
    // I gonna key you out dude :>
    private final Map<String, EvalSheet> evaluations;// this is quite a very interesting thing
    private final SortedSet<Student> studentsByAlphOrder;
    private final List<EvalSheet> evalSheet;
    private final SheetHelper helper;
    //private final List<Student> statistic;

    public GradesManagerClass(){
        subjects = new TreeMap<>();
        students = new HashMap<>(STUDENTS_DEFAULT_SIZE);
        evaluations = new HashMap<>(DEFAULT_SIZE);
        studentsByAlphOrder = new TreeSet<>(new StudentByAlphOrder());
        evalSheet = new ArrayList<>(DEFAULT_SIZE);
        helper = new SheetHelperClass();
        //statistic = new ArrayList<>();
    }

    @Override
    public String createEvaluationSheet(String subjectId, String evalId) throws EvaluationAlreadyExistsException {
        Subject subject = getSubject(subjectId);
        EvalSheet eval = subject.createEvaluations(evalId);
        String id = eval.evaluationId();
        evaluations.put(id, eval);
        evalSheet.add(eval);
        return id;
    }


    @Override
    public void evaluate(String evalId, int studentNumber, String studentName, float grade) throws  EvalSheetDoesNotExistException, AlreadyEvaluatedException{
        EvalSheet eval = evaluations.get(evalId);
        if(eval == null)
            throw new EvalSheetDoesNotExistException(evalId);
        Student student = getStudent(studentNumber, studentName);
        eval.evaluate(student, grade);
    }

    @Override
    public void addSheetHelper(SheetHelper sheet) throws EvaluationAlreadyExistsException, AlreadyEvaluatedException, InvalidSheetException {
        if(sheet == null)
            throw new InvalidSheetException();
        String evalId = createEvaluationSheet(sheet.subject(), sheet.evalId());
        EvalSheet eval = evaluations.get(evalId);
        Iterator<EvalHelper> it = sheet.listAllHelper();
        while (it.hasNext()) {
            EvalHelper tmp = it.next();
            eval.evaluate(getStudent(tmp.number(), tmp.name()), tmp.getGrade());
        }
    }

    private Student getStudent(int number, String name){
        Student student = students.get(number);
        if(student == null){
            student = new StudentClass(number, name);
            students.put(number, student);
            studentsByAlphOrder.add(student);
            //statistic.add(student);
        }
        return student;
    }

    private Subject getSubject(String subjectId){
        Subject subject = subjects.get(subjectId);
        if(subject == null){
            subject = new SubjectClass(subjectId);
            subjects.put(subjectId, subject);
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
}
