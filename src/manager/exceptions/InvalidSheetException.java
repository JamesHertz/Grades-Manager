package manager.exceptions;

import java.io.Serializable;

public class InvalidSheetException extends Exception implements Serializable {
    private static final String MESSAGE = "Invalid sheet!!";
    private static final long serialVersionUID = 0L;

    public InvalidSheetException(){
        super(MESSAGE);
    }
}
