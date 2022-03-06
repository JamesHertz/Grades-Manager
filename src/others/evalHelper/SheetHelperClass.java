package others.evalHelper;

import others.Statistic;
import others.StatisticClass;
import subject.Student;
import subject.exceptions.AlreadyEvaluatedException;

import java.io.Serializable;
import java.util.*;

public class SheetHelperClass implements SheetHelper{

    private static final long serialVersionUID = 0L;
    private final SortedMap<Integer, EvalHelper> evalHelpers; // think about it later


    public SheetHelperClass(){
        evalHelpers = new TreeMap<>();
    }

    @Override
    public boolean isEmpty() {
        return evalHelpers.isEmpty();
    }

    @Override
    public void clear() {
        if(!evalHelpers.isEmpty())
            evalHelpers.clear();
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
    public Iterator<EvalHelper> listAllHelper() {
        return evalHelpers.values().iterator();
    }

}
