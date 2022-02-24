package subject.exceptions;

import java.io.Serializable;

public class AlreadyEvaluatedException extends Exception implements Serializable {

    private static final String MESSAGE ="%d already evaluated.";
    private static final long serialVersionUID = 0L;

    public AlreadyEvaluatedException(int number){
        super(String.format(MESSAGE, number));
    }
}
