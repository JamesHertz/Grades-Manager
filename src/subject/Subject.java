package subject;

import others.Statistic;
import others.evalHelper.SheetHelper;
import subject.evaluations.EvalSheet;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.EvaluationDoesNotExit;
import subject.exceptions.FinalGradeAlreadySetException;

import java.util.Iterator;

public interface Subject {
    String id();
    EvalSheet addEvaluation(String evalId, SheetHelper helper) throws EvaluationAlreadyExistsException;
    EvalSheet addFinalEvaluation(SheetHelper helper) throws FinalGradeAlreadySetException;
    EvalSheet getEvalSheet(String evalId) throws EvaluationDoesNotExit;
    int numberOfEvalSheet();
    Statistic statistic();
    int getEcts();
    Iterator<EvalSheet> listEvaluations();
}

