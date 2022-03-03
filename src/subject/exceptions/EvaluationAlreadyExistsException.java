package subject.exceptions;


import java.io.Serializable;

public class EvaluationAlreadyExistsException extends Exception implements Serializable {

    private static final String MESSAGE = "%s already has %s.";
    private static final long serialVersionUID = 0L;

    public EvaluationAlreadyExistsException(String subject, String evalId){
        super(String.format(MESSAGE, subject, evalId));
    }
}
