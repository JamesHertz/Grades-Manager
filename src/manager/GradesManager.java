package manager;

import manager.exceptions.*;
import others.evalHelper.SheetHelper;
import subject.Subject;
import subject.SubjectSlot;
import subject.evaluations.EvalSheet;
import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.NoSuchSubjectInStudentException;

import java.util.Iterator;

public interface GradesManager {
    
    // changes:
    // BufHelper
    void clearBufHelper();
    // adds EvalEntry if the student doesn't exist on the system it creates a new one
    void addEvalEntry(int studentNumber, String name, float eval) throws AlreadyEvaluatedException; // probably not gonna use this one
    void addEvalEntry(int studentNumber, float eval) throws StudentDoesNotExistException, AlreadyEvaluatedException;
    // addEvalEntry(List<....> eval)
    // returns the number of new Student created
    int commitBufHelper(String subjectId, String evalId) throws EmptyBufHelperException, EvaluationAlreadyExistsException;
    // is it really necessary??
    String createEvaluationSheet(String subject, String evalId) throws EvaluationAlreadyExistsException;
    void evaluate(String evalId, int studentNumber, String studentName, float grade) throws  EvalSheetDoesNotExistException, AlreadyEvaluatedException;
    void addSheetHelper(SheetHelper sheet) throws EvaluationAlreadyExistsException, AlreadyEvaluatedException, InvalidSheetException, AlreadyEvaluatedException;

    // interesting I will think about this
    SubjectSlot studentSubjectSlot(int studentNumber, String subjectId) throws StudentDoesNotExistException, SubjectDoesNotExistException, NoSuchSubjectInStudentException, NoAddedStudentException;

    EvalSheet evalSheet(String evalId) throws EvalSheetDoesNotExistException, NoAddedEvalSheetException;
    Student student(int number) throws StudentDoesNotExistException, NoAddedStudentException;
    Subject subject(String subjectId) throws SubjectDoesNotExistException, NoAddedSubjectException;

    Iterator<EvalSheet> listAllSheets();
    Iterator<Student> listAllStudents();
    //Iterator<Student> statistic();
    Iterator<Subject> listAllSubjects();
}
