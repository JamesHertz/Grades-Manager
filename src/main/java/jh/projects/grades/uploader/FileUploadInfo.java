package jh.projects.grades.uploader;

public class FileUploadInfo extends UploadInfo {

    private final String filename;
    // TODO: think about this
    // private final FileFormat format; // should I add this?
    public FileUploadInfo(String course_id, String filename) {
        super(course_id);
        this.filename = filename;
    }
    public String getFilename(){
        return this.filename;
    }
}
