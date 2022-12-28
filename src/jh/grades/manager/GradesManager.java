package jh.grades.manager;

import jh.grades.uploader.UploadInfo;

import java.util.Iterator;

public interface GradesManager {

    Student getStudent(int number);
    Iterator<Student> top();
    Iterator<Student> listAllStudents();
    Iterator<Course> listAllCourses();

    void uploadEnrolls(UploadInfo info);
    // later upload evals
    /*
        How should the UploadInfo look like ?
        // the name is still not good

        // a decorator

        // I will need to find a better name for this
        interface UploadInfo{
            String course_id();
        }
        interface URL_INFO extends UploadInfo{
            <all the attrs I will need>
        }

        interface FileInfo extends UploadInfo{
            String filename();
            // format? Is it worth?
        }

        // for future
        interface EvalInfo extends UploadInfo{
            String evalID();
        }
     */

    // methods to upload enrolls
    // question which one is better?
    // how can I make the uniform and avoid a mess?
    // void upload_enrolls(String file_name);
    // void upload_eval(Record[] records, String course_id, EvalType type);
}
