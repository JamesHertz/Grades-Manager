package jh.projects.grades.uploader;

public abstract class UploadInfo {

    private final String course_id;
    private CourseInfo course_info;
    UploadInfo(String course_id){
        this.course_id = course_id;
        this.course_info = null;
    }

    public CourseInfo getCourseInfo() {
        return course_info;
    }

    public void setCourseInfo(CourseInfo info) {
        this.course_info = info;
    }

    public String getCourseId() {
        return course_id;
    }
}
