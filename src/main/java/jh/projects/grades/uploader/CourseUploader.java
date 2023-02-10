package jh.projects.grades.uploader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jh.projects.grades.rawdata.CourseInfo;
import jh.projects.grades.rawdata.RawCourse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseUploader {
   public static Iterator<RawCourse> getCourses(String filename){
      try{
         return new ObjectMapper()
                 .readValue(new File(filename), new TypeReference<ArrayList<RawCourse>>() {})
                 .iterator();
      }catch (IOException e){
         throw new RuntimeException(e);
      }
   }

}
