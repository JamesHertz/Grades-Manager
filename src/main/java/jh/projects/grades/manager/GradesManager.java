package jh.projects.grades.manager;

import jh.projects.grades.uploader.UploadInfo;
import jh.projects.grades.uploader.excepctions.UploadException;

import java.util.Iterator;

public interface GradesManager {

    Student getStudent(int number);
    Course getCourse(String course_id);
    Iterator<Student> top();
    Iterator<Student> listAllStudents();
    Iterator<Course> listAllCourses();

    // will read json file and upload all the course in it.

    void uploadEnrolls(UploadInfo info) throws UploadException;

    /*
        Assumptions:
            -> I only keep courses that has enrollments
        how should I upload the courses?
        At this point, it makes sense that the UploadInfo has the Course info.
        But I want that such information be optional

        I also want to be able to upload several courses using a json file.
        Should I have other methods?
        The option would be instead of loading enrolls let's load courses.
        And there we would have the UploadInfo.

        I could instead of this have on the other hand upload the courses independently of the Enrolls.

        I think that this makes sense. Having the course info in the UploadInfo.
        But the question now is, what about me uploading several courses?

        I think that I should have one this method.
        Now let's just think of how this can work out.

        How should I change UploadInfo.
     */
}
