package jh.grades.uploader;

public class FileInfo extends UploadInfo {

    private final String filename;
    public FileInfo(String course_id, String filename) {
        super(course_id);
        this.filename = filename;
    }
    public String getFilename(){
        return this.filename;
    }
}
