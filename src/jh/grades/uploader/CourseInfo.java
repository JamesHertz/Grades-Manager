package jh.grades.uploader;

import jh.grades.manager.Semesters;

public record CourseInfo(
        String name,
        Semesters semesters,
        int credits,
        int year
) {}
