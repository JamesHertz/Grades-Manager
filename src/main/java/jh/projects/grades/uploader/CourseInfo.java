package jh.projects.grades.uploader;

import jh.projects.grades.manager.Semesters;

public record CourseInfo(
        String name,
        Semesters semester,
        int year,
        int credits
) {}
