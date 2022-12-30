package jh.grades.uploader;

import jh.grades.uploader.excepctions.InvalidLineException;
import jh.grades.uploader.excepctions.UploadException;
import jh.grades.uploader.excepctions.UploadFileNotFound;

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
    // TODO: look at the return type :) - DONE :)
    // think return a list?
    private static List<EnrollRecord> getFileRecords(FileUploadInfo info) throws UploadException {
        List<EnrollRecord> records = new LinkedList<>();
        try(
                Scanner in = new Scanner(new FileReader(info.getFilename()));
        ){
            while(in.hasNext()){
                String line = in.nextLine();
                // int number = in.nextInt();
                // String name = get_name(in);
                // float grade = in.nextFloat();
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
    // a class main to test this thing :)
    private static void parseLine(String line) throws InvalidLineException {
        try{
            String[] res = line.trim().split("\\s{1,}");
            int length = res.length;
            int number = Integer.parseInt(res[0]);
            String name = null;
            for(int i = 1; i < length - 1; i++){
                if(name == null) name = res[i];
                else name += " " + res[i];
            }
            float grade = Float.parseFloat(res[length - 1]);

            System.out.println("number = " + number);
            System.out.println("name = " + name);
            System.out.println("grade = " + grade);
        }catch (NumberFormatException e){
            throw new InvalidLineException(line, 0);
        }


    }
    public static void main(String[] args) {
        String line = "61177 James Hertz Gomes Furtado 10.0";
        try {
            parseLine(line);
        } catch (InvalidLineException e) {
            System.out.println("line-number: " + e.getLineNumber());
            System.out.println("line: = " + e.getLine());
        }
    }
}
