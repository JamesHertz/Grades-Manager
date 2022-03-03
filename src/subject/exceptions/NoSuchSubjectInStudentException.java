package subject.exceptions;

import java.io.Serializable;

public class NoSuchSubjectInStudentException extends Exception implements Serializable {
    private static final String MESSAGE = "Student %d hasn't any evaluation from subject '%s'.";
    private static final long serialVersionUID = 0L;

    public NoSuchSubjectInStudentException(int student, String subject){
        super(String.format(MESSAGE, student, subject));
    }
}
