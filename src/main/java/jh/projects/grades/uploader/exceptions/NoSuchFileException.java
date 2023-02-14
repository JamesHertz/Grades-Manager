package jh.projects.grades.uploader.exceptions;

public class NoSuchFileException extends UploadException{
    private final String filename;
    public NoSuchFileException(String filename){
        super(String.format("File '%s' not found or it's inaccessible.", filename));
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
