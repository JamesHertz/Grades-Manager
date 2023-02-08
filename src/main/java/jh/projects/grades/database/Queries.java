package jh.projects.grades.database;

public enum Queries {
    INSERT_STUDENT("insert into Student values(?, ?)"),
    // id, name, credits, year, semester
    INSERT_COURSE("insert into Course values(?, ?, ?, ?, ?)"),
    // id, number, grade
    INSERT_ENROLL("insert into Enrollment values(?, ?, ?)"),
    GET_STUDENT_ENROLLS("select cs_id, st_number, grade from Enrollment where st_number=?"),
    GET_COURSE_ENROLLS("select cs_id, st_number, grade from Enrollment where cs_id=?"),
    GET_STUDENTS("select number, name, total_credits, avg_grade from MyStudent"),
    GET_COURSES("select cs_id, cs_name, credits, cs_year, cs_semester from Course");

    private final String value;
    Queries(String value){
        this.value = value;
    }

    public String getQueryValue(){
        return this.value ;
    }


}
