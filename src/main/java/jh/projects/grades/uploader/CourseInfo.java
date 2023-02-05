package jh.projects.grades.uploader;

import jh.projects.grades.manager.Semesters;

public record CourseInfo(
        String name,
        Semesters semesters,
        int credits,
        int year
) {}
