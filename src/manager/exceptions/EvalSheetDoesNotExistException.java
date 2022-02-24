package manager.exceptions;

import java.io.Serializable;

public class EvalSheetDoesNotExistException extends Exception implements Serializable {
    private static final String MESSAGE = "Evaluation Sheet '%s' does not exit.";
    private static final long serialVersionUID = 0L;

    public EvalSheetDoesNotExistException(String evalId){
        super(String.format(MESSAGE, evalId));
    }
}
