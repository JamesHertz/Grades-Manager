package jh.projects.grades.uploader;

import static org.jsoup.Connection.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static jh.projects.grades.uploader.GradesUploader.HttpValues.*;
import jh.projects.grades.rawdata.StudentRecord;

public class GradesUploader {
    // values used in the Http Connection with CLIP.
    // They vary from url query parameters and values to cookies names and urls
    static class HttpValues{
        // URL QUERY FIELDS AND COOKIE INFO USED TO GET COOKIE
        static final String USERNAME_FIELD  = "identificador";
        static final String PASSWORD_FIELD  = "senha";
        static final String COOKIE_NAME     = "JServSessionIdroot1112";
        static final String COOKIE_URL      = "https://clip.fct.unl.pt/utente/eu";

        // URL QUERY FIELDS VALUES AND URLS USED TO FETCH COURSES RESULTS:

        // URL USED TO GET THE RESULTS
        static final String ENROLLS_BASE_URL = "https://clip.fct.unl.pt/utente/eu/aluno/ano_lectivo/unidades/unidade_curricular/actividade/resultados/pautas/por_curso";

        // QUERY FIELDS
        /*
                CLIP-QUERY-PARS-INFO:
                tipo_de_período_lectivo      :	s -> t (trimester)
                tipo_de_avaliação_curricular :	a -> m (melhoria)
                ano_lectivo                  :  <year>
                período_lectivo              :	1  -> 2 (2nd)
                aluno                        :	<student-number-id>
                instituição                  :	<college>
                unidade_curricular           :	<course-code>
                curso                        :  472 (MIEI), 524 (LEI)
        */

        static final String PERIOD_TYPE  = "tipo_de_período_lectivo";
        static final String PERIOD       = "período_lectivo";
        static final String EVAL_TYPE    = "tipo_de_avaliação_curricular";
        static final String YEAR         = "ano_lectivo";
        static final String NUMBER_ID    = "aluno";
        static final String COLLEGE      = "instituição";
        static final String COURSE_CODE  = "unidade_curricular";
        static final String PROGRAM      = "curso";

        // PERIOD_TYPE values
        static final String SEMESTER_PERIOD   = "s";
        static final String TRIMESTER_PERIOD  = "t";
        // EVAL_TYPE values
        static final String[] EVALS_TYPE      =  {"a", "m"};
        // COLLEGE value
        static final String MY_COLLEGE        = "97747";
        // COURSE_CODE values
        static final String[] TARGET_PROGRAMS = {"472", "524"};

        // Year I started to have classes in college
        static final int BASE_YEAR  = 2020;

        // encoding used to encode the url query parameters
        static final String QUERY_ENCODING = "ISO-8859-1";
    }

    private static String getNumberID(Document doc){
        Pattern p = Pattern.compile("aluno=(\\d+)");
        for(Element el : doc.select("a")){
            Matcher m  = p.matcher(el.attr("href"));
            if(m.find()) return m.group(1);
        }
        return null;
    }

    public static ClipCredentials getStudentInfo(String username, String password){
        try {
            Connection.Response res = Jsoup.connect(HttpValues.COOKIE_URL)
                    .data(HttpValues.USERNAME_FIELD, username)
                    .data(HttpValues.PASSWORD_FIELD, password)
                    .method(Method.POST)
                    .execute();
            String cookie = res.cookie(HttpValues.COOKIE_NAME);
            String numberID = getNumberID(res.parse());

            // todo: add exceptions over here :)
            if(numberID == null) return null;

            return new ClipCredentials(cookie, numberID);
        } catch (IOException e) {}
        return null;
    }

    public record ClipCredentials(String cookie, String numberID){};
    public record CourseInfo(int year, int semester, int code) {}

    private static int getValue(String value){
        try{
            return Integer.parseInt(value.trim());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    private static Iterator<StudentRecord> parseRows(Document doc){
        List<StudentRecord> data = new LinkedList<>();
        for(Element el : doc.select("tr[align=left]")){
            int number = 0, grade = 0; String name = "";
            int idx = 0;
            for(Element inner : el.getAllElements()){
                if(idx == 1) number = getValue(inner.text());
                else if(idx == 2) name = inner.text();
                else{
                    String new_grade = inner.text().replaceAll("\\*", " ");
                    grade = Math.max(grade, getValue(new_grade));
                }
                ++idx;
            }
            data.add(new StudentRecord(number, name, grade));
        }
        return data.iterator();
        // return data;
    }

    public static Iterator<StudentRecord> getEnrolls(ClipCredentials credentials, CourseInfo cs){
        try{
            Map<String, String> reqData = new HashMap<>(){{
                put(COLLEGE, MY_COLLEGE);
                put(NUMBER_ID, credentials.numberID());
                put(COURSE_CODE, String.valueOf(cs.code()));
                put(YEAR, String.valueOf(BASE_YEAR + cs.year()));
            }};

            if(cs.semester() == 0){
                reqData.put(PERIOD_TYPE, TRIMESTER_PERIOD);
                reqData.put(PERIOD, String.valueOf(2)); // 0
            }else{
                reqData.put(PERIOD_TYPE, SEMESTER_PERIOD);
                reqData.put(PERIOD, String.valueOf(cs.semester()));
            }
            // put(PROGRAM, TARGET_PROGRAMS[0]);

            Map<Integer, StudentRecord> records = new TreeMap<>();//new HashMap<>();

            for(String eval : EVALS_TYPE){
                reqData.put(EVAL_TYPE, eval);

                int empty_times = 0;
                for(String program : TARGET_PROGRAMS){
                    reqData.put(PROGRAM, program);

                    Response res = Jsoup.connect(ENROLLS_BASE_URL)
                        .data(reqData)
                        .postDataCharset(QUERY_ENCODING)
                        .method(Method.GET)
                        .cookie(COOKIE_NAME, credentials.cookie())
                        .execute();
                    // todo: think about this :)
                    if(res.statusCode() != 200) return null;

                    Document doc = res.parse();
                    if(doc.text().isBlank()){
                        ++empty_times;
                        continue;
                    }
                    // merge grades :)
                    Iterator<StudentRecord> it = parseRows(doc);
                    while(it.hasNext()){
                        StudentRecord rec = it.next();
                        StudentRecord other = records.get(rec.number());
                        if(other == null || other.grade() < rec.grade())
                            records.put(rec.number(), rec);
                    }

                    // by now :)
                }
                if(empty_times == 2) break;

            }

            return records.values().iterator();
        }catch (IOException e){}
        return null;
    }


}
