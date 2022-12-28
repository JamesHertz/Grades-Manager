package jh.grades.uploader;

import java.util.Iterator;

public interface EnrollRecords {
    // TODO: check this, probably it's not necessary
    String courseId();
    // String evalID() ??
    Iterator<Record> records();
}
