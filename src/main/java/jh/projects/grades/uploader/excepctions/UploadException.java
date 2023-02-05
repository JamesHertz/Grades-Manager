package jh.projects.grades.uploader.excepctions;

// note: runtime exceptions are used when the program
// cannot recover from the exception anymore, like index out of bound
public class UploadException extends Exception{
    /* DEPRECATED UNTIL I ACTUALLY NEED IT
    public UploadException(String message){
        super(message);
    }*/

    public UploadException(){
        super();
    }
}
