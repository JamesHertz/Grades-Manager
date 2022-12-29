package jh.grades.uploader;

import jh.grades.uploader.excepctions.UploadException;
import jh.grades.uploader.excepctions.UploadFileNotFound;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GradesUploader {

    private static String get_name(Scanner in) {
        String name = "";
        while (!in.hasNextFloat())
            name = String.format("%s %s", name, in.next());
        return name;
    }
    // TODO: look at the return type :) - DONE :)
    // think return a list?
    private static List<EnrollRecord> getFileRecords(FileUploadInfo info) throws UploadException {
        List<EnrollRecord> records = new LinkedList<>();
        try(
                Scanner in = new Scanner(new FileReader(info.getFilename()));
        ){
            while(in.hasNext()){
                int number = in.nextInt();
                String name = get_name(in);
                float grade = in.nextFloat();
                records.add(null); // TODO.
            }

        }catch (FileNotFoundException e){
            throw new UploadFileNotFound(info.getFilename());
        }
        return records;
    }
    public static Iterator<EnrollRecord> getRecords(UploadInfo info) throws UploadException {
        List<EnrollRecord> myrecords;
        if(info instanceof FileUploadInfo){
            myrecords = getFileRecords((FileUploadInfo) info);
        }else{ // info instanceof UrlUploadInfo
            // not supported yet :)
            throw new UnsupportedOperationException("We don't support any UploadInfo but FileUploadInfo yet.");
        }
        if(myrecords.isEmpty()){
            // TODO: exception, it should not be empty :)
        }
        // TODO: fun things :)
        return myrecords.iterator();
    }
}
