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

    // think about this ...
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
