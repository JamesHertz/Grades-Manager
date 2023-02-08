package jh.projects.grades.uploader.excepctions;

public class InvalidGradeException extends InvalidLineException{
    private final String grade;
    public InvalidGradeException(String line, int lineNumber, String grade) {
        super(line, lineNumber);
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + String.format("Invalid grade '%s'", grade);
    }
}
