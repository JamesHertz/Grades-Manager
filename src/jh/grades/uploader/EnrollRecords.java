package jh.grades.uploader;

import java.util.Iterator;

public interface EnrollRecords {
    //
    String courseId();
    // String evalID() ??
    Iterator<Record> records();
}
