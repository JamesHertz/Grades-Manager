package manager.exceptions;

import java.io.Serializable;

public class NoAddedStudentException extends Exception implements Serializable {
    private static final long serialVersionUID = 0L;

    public NoAddedStudentException(){
        super();
    }
}
