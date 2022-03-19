package manager.exceptions;

public class SubjectAlreadyExistException extends Exception{
    private static final String MESSAGE = "%s already exist";
    public SubjectAlreadyExistException(String subjectId){
        super(String.format(MESSAGE, subjectId));
    }
}
