package others.evalHelper;

import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.*;

public class SheetHelperClass implements SheetHelper, Serializable {

    private static final int DEFAULT_SIZE = 20;
    private static final long serialVersionUID = -2142269003925284585L;
    private final List<EvalHelper> evalHelpers;
    private final Set<String> numbers;
    private final String subject, evalId;
    public SheetHelperClass(String subject, String evalId){
        this.subject = subject;
        this.evalId = evalId;
        numbers = new HashSet<>(DEFAULT_SIZE);
        evalHelpers = new LinkedList<>();
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
    public int getNumberOfEvalHelper() {
        return evalHelpers.size();
    }

    @Override
    public void addEvalHelper(String name, int number, float grade) throws AlreadyEvaluatedException {
        String realNumber = Integer.toString(number);
        if(!numbers.add(realNumber))
            throw new AlreadyEvaluatedException(number);
        evalHelpers.add(new EvalHelperClass(name, number, grade));
    }

    @Override
    public Iterator<EvalHelper> listAllHelper() {
        return evalHelpers.iterator();
    }




}
