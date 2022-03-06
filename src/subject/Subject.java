package subject;

import others.Statistic;
import others.evalHelper.EvalHelper;
import others.evalHelper.SheetHelper;
import subject.evaluations.EvalSheet;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.EvaluationDoesNotExit;

import java.util.Iterator;

public interface Subject {
    String id();
    EvalSheet addEvaluation(String evalId, SheetHelper helper) throws EvaluationAlreadyExistsException;
    EvalSheet getEvalSheet(String evalId) throws EvaluationDoesNotExit;
    int numberOfEvalSheet();
    Statistic statistic();
    Iterator<EvalSheet> listEvaluations();
}

