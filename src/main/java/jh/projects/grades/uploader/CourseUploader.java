package jh.projects.grades.uploader;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jh.projects.grades.rawdata.RawCourse;
import jh.projects.grades.uploader.exceptions.NoSuchFileException;
import jh.projects.grades.uploader.exceptions.ParsingException;
import jh.projects.grades.uploader.exceptions.UploadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CourseUploader {
   public static Iterator<RawCourse> getCourses(String filename) throws UploadException{
      try {
         return new ObjectMapper()
                 .readValue(new File(filename), new TypeReference<ArrayList<RawCourse>>() {})
                 .iterator();
      } catch (FileNotFoundException e) {
         throw new NoSuchFileException(filename);
      }catch (JsonMappingException e){
         // TODO: fix this
         JsonLocation loc = e.getLocation();
         throw new ParsingException(String.format(
                 "unexpected token at [ line: %d ; column: %d ]",
                 loc.getLineNr(),
                 loc.getLineNr()
        ));
      } catch (IOException e){
         throw new ParsingException(e.getMessage());
      }
   }

}
