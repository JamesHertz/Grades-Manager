package subject;

import others.Statistic;
import others.StatisticClass;
import others.evalHelper.EvalHelper;
import others.evalHelper.SheetHelper;
import subject.evaluations.EvalEntry;
import subject.evaluations.EvalSheet;
import subject.evaluations.EvalEntryClass;
import subject.evaluations.EvaluationSheet;
import subject.exceptions.EvaluationAlreadyExistsException;
import subject.exceptions.EvaluationDoesNotExit;

import java.io.Serializable;
import java.util.*;

public class SubjectClass implements Subject, Serializable {

    private static final long serialVersionUID = 0L;
    private final List<EvalSheet> evaluations;
   // private final Map<String, EvalSheet> allEvaluations;
    private final Map<Integer, SubjectSlot> allStudents; // think about studentsByName
    private final Set<String> evalIds; // delete later...
    private final String id;
    private final Statistic statistic;

    public SubjectClass(String id){
        this.id = id;
        evaluations = new LinkedList<>();
        evalIds = new HashSet<>();
        statistic = new StatisticClass();
        allStudents = new Hashtable<>();
   //     allEvaluations = new Hashtable<>();
    }


    // future plan
    public void evaluate(Student student, float grade){

    }


    @Override
    public EvalSheet addEvaluation(String evalId, SheetHelper helper) throws EvaluationAlreadyExistsException {
        //if(allEvaluations.containsKey(evalId))
        // if the evaluation exists
         if(findEval(evalId) != null) // think about this later :)
            throw new EvaluationAlreadyExistsException(id, evalId);

        // think about the possibility of it be very fast
        EvalSheet eval = new EvaluationSheet(evalId, this);
        // allEvaluations.put(evalId, eval);
        evaluations.add(eval);

        // for each evaluation:
        // -> Add the evaluation to is respective SubjectSlot
        Iterator<EvalHelper> it = helper.listAllHelper();
        while(it.hasNext()){
           EvalHelper tmp = it.next();
           Student st = tmp.getStudent();
           SubjectSlot slot = allStudents.get(st.number());
           if(slot == null){
                   slot = new SubjectSlotClass(st, this);
                   allStudents.put(tmp.number(), slot);
                   st.addSlot(slot);
           }
            // a lot to think yet :D
            // in some how get the student
            EvalEntry entry =  new EvalEntryClass(eval, st, tmp.getGrade());
            slot.addEvaluations(entry);
            eval.evaluate(st, tmp.getGrade());
        }
        return eval;
    }

    private EvalSheet findEval(String evalId){
        Iterator<EvalSheet> it = evaluations.iterator();
        while(it.hasNext()){ // xiuu intellij
            EvalSheet tmp = it.next();
            if(tmp.evaluationId().equals(evalId))
                return tmp;
        }
        return null;
    }

    @Override
    public EvalSheet getEvalSheet(String evalId) throws EvaluationDoesNotExit {
        EvalSheet tmp = findEval(evalId);
        if(tmp == null)
            throw new EvaluationDoesNotExit(evalId); // not necessary think about it
        return tmp;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Iterator<EvalSheet> listEvaluations() {
        return evaluations.iterator();
    }

    @Override
    public int numberOfEvalSheet() {
        return evaluations.size();
    }


    @Override
    public Statistic statistic() {
        return statistic;
    }

}
