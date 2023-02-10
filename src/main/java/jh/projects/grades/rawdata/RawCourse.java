package jh.projects.grades.rawdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RawCourse(String courseID, String name, int credits, int year, int semester, int code) {


    @JsonCreator
    public static RawCourse createCourse(
             @JsonProperty(required=true, value="id") String courseID,
             @JsonProperty(required=true, value="name") String name,
             @JsonProperty(required=true, value="credits") int credits,
             @JsonProperty(required=true, value="year") int year,
             @JsonProperty(required=true, value="semester") int semester,
             @JsonProperty(required=true, value="code") int code
    ){
        return new RawCourse(courseID, name, credits, year, semester, code);
    }

}
