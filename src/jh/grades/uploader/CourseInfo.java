package jh.grades.uploader;

import jh.grades.manager.Semesters;

// should this a record??
public interface CourseInfo {
    // this is very fun?
    String name();
    Semesters semester();
    int credits();
    int year();
}
