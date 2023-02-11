package jh.projects.grades.uploader.exceptions;

public class NoSuchFileException extends Exception{
    private final String filename;
    public NoSuchFileException(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
