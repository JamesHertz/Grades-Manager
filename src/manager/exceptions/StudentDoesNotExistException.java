package manager.exceptions;

import java.io.Serializable;

public class StudentDoesNotExistException extends  Exception implements Serializable {
    private static final String MESSAGE = "Student '%d' does not exit.";
    private static final long serialVersionUID = 0L;

    public StudentDoesNotExistException( int number){
        super(String.format(MESSAGE, number));
    }
}
