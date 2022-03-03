package manager.exceptions;


import java.io.Serializable;

public class SubjectDoesNotExistException extends Exception implements Serializable {
    private static final String MESSAGE = "Subject '%s' does not exit.";
    private static final long serialVersionUID = 0L;

    public SubjectDoesNotExistException(String subject){
        super(String.format(MESSAGE, subject));
    }
}
