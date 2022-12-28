package jh.grades.uploader;

public abstract class UploadInfo {
    private final String course_id;
    public UploadInfo(String course_id){
        this.course_id = course_id;
    }
    public String getCourseId() {
        return course_id;
    }
}
