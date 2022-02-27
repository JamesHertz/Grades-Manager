package others.evalHelper;

import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.*;

public class SheetHelperClass implements SheetHelper, Serializable {

    private static final long serialVersionUID = 0L;
    private final String subject, evalId;
    private final SortedMap<Integer, EvalHelper> evalHelpers; // think about it later
    public SheetHelperClass(String subject, String evalId){
        this.subject = subject;
        this.evalId = evalId;
        evalHelpers = new TreeMap<>();
    }
    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String evalId() {
        return evalId;
    }

    @Override
    public void addEvalHelper(Student student, float grade) throws AlreadyEvaluatedException {
        int number = student.number();
        if(evalHelpers.containsKey(number))
            throw new AlreadyEvaluatedException(number);
        evalHelpers.put(number, new EvalHelperClass(student, grade));
    }

    @Override
    public int size() {
        return evalHelpers.size();
    }

    @Override
    public void addEvalHelper(String name, int number, float grade) throws AlreadyEvaluatedException {
        if(evalHelpers.containsKey(number))
            throw new AlreadyEvaluatedException(number);
        evalHelpers.put(number, new EvalHelperClass(name, number, grade));
    }

    @Override
    public Iterator<EvalHelper> listAllHelper() {
        return evalHelpers.values().iterator();
    }

}
