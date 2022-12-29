package jh.grades.uploader.excepctions;

public class UploadFileNotFound extends UploadException{
    private final String filename;

    public UploadFileNotFound(String filename){
        this.filename = filename;
    }

    public String getFilename(){
        return filename;
    }
}
