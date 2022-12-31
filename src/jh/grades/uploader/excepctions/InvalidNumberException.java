package jh.grades.uploader.excepctions;

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
    public String toString() {
        return super.toString() + String.format(" : Invalid number '%s'", number);
    }
}
