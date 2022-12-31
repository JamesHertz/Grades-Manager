package jh.grades.uploader;

import jh.grades.uploader.excepctions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;

public class GradesUploader {

    private static String get_name(Scanner in) {
        String name = "";
        while (!in.hasNextFloat())
            name = String.format("%s %s", name, in.next());
        return name;
    }

    private static List<EnrollRecord> getFileRecords(FileUploadInfo info) throws UploadException {
        List<EnrollRecord> records = new LinkedList<>();
        try(
                Scanner in = new Scanner(new FileReader(info.getFilename()));
        ){
            int lineNumber = 1;
            while(in.hasNext()){
                String line = in.nextLine();
                String[] res = line.trim().split("\\s+");

                // TODO: should I have an exception if there is only one word in the string??

                String number_str = res[0];
                String grade_str = res[ res.length - 1 ];

                int number; float grade; StringBuilder name = null;

                try{
                    number = Integer.parseInt(number_str);
                }catch (NumberFormatException e){
                    throw new InvalidNumberException(line, 1, number_str);
                }

                for(int i = 1; i < res.length- 1; i++){
                    if(name == null) name = new StringBuilder(res[i]);
                    else name.append(" ").append(res[i]);
                }

                try{
                    grade = Float.parseFloat(grade_str);
                }catch (NumberFormatException e){
                    throw new InvalidGradeException(line, 1, grade_str);
                }

                records.add(new Record(
                        number,
                        name == null ? null: name.toString(),
                        grade
                ));

               lineNumber++;
            }

        }catch (FileNotFoundException e){
            throw new UploadFileNotFound(info.getFilename());
        }
        return records;
    }
    public static Iterator<EnrollRecord> getRecords(UploadInfo info) throws UploadException {
        List<EnrollRecord> my_records;
        if(info instanceof FileUploadInfo){
            my_records = getFileRecords((FileUploadInfo) info);
        }else{ // info instanceof UrlUploadInfo
            // not supported yet :)
            throw new UnsupportedOperationException("We don't support any UploadInfo but FileUploadInfo yet.");
        }

        if(my_records.isEmpty()){
            // TODO: exception, it should not be empty :)
        }
        // TODO: fun things :)
        return my_records.iterator();
    }
}
