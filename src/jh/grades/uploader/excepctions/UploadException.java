package jh.grades.uploader.excepctions;

import jh.grades.uploader.UploadInfo;

// note: runtime exceptions are used when the program
// cannot recover from the exception anymore, like index out of bound
public class UploadException extends Exception{
    public UploadException(String message){
        super(message);
    }

}
