package jh.projects.grades.uploader.excepctions;


public class InvalidLineException extends UploadException{
    private final String line;
    private final int lineNumber;

    // allow format <st-number(integer)> <student-name> (optional) <grade (float)>
    public InvalidLineException(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Error in line: %d: %s\n", lineNumber, line);
    }
}
