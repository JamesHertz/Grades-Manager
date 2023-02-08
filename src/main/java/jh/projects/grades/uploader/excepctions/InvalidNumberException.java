package jh.projects.grades.uploader.excepctions;

public class InvalidNumberException extends InvalidLineException{

    // TODO: should I have an exceptions with enumerate ?
    private final String number;
    public InvalidNumberException(String line, int lineNumber, String number) {
        super(line, lineNumber);
        this.number = number;
    }

    public String getNumber(){
        return this.number;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + String.format("Invalid student number '%s'", number);
    }
}
