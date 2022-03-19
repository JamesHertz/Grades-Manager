package manager;

import manager.exceptions.*;
import others.evalHelper.EvalHelper;
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
    // returns some metadata ....
    int commitBufHelper(String subjectId, String evalId) throws EmptyBufHelperException, EvaluationAlreadyExistsException, SubjectDoesNotExistException;
    // is it really necessary??

    Subject createSubject(String subId, int etcs) throws SubjectDoesNotExistException;
    // interesting I will think about this
    SubjectSlot studentSubjectSlot(int studentNumber, String subjectId) throws StudentDoesNotExistException, SubjectDoesNotExistException, NoSuchSubjectInStudentException, NoAddedStudentException;

    EvalSheet evalSheet(String evalId) throws EvalSheetDoesNotExistException, NoAddedEvalSheetException;
    Student student(int number) throws StudentDoesNotExistException, NoAddedStudentException;
    Subject subject(String subjectId) throws SubjectDoesNotExistException, NoAddedSubjectException;

    Iterator<EvalSheet> listAllSheets();
    Iterator<Student> listAllStudents();
    //Iterator<Student> statistic();
    Iterator<Subject> listAllSubjects();
    Iterator<EvalHelper> listBufHelper();
}
