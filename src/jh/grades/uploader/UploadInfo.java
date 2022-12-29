package jh.grades.uploader;

public abstract class UploadInfo {
    // TODO: I've been assuming that the course is there, what to do if it doesn't happen to be there?
    private final String course_id;
    UploadInfo(String course_id){
        this.course_id = course_id;
    }
    public String getCourseId() {
        return course_id;
    }
}
