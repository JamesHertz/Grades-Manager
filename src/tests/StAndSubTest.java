package tests;
import org.junit.Before;
import org.junit.Test;
import others.evalHelper.SheetHelper;
import others.evalHelper.SheetHelperClass;
import subject.Student;
import subject.StudentClass;
import subject.Subject;
import subject.SubjectClass;
import subject.evaluations.EvalSheet;
import subject.exceptions.AlreadyEvaluatedException;
import subject.exceptions.EvaluationAlreadyExistsException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
public class StAndSubTest {
    private SheetHelper tmp;
    private List<Student> st;

    // make sure when you add a grate it goes to the right student ...
    // that's it
    @Before
    public void setUP(){
        tmp = new SheetHelperClass("Math", "Test 1");
        st = new LinkedList<>();
        String[] names = new String[]{"James", "Hertz","John", "Peter", "Richard", "Michael"};
        int number = 61177;
        for(String name : names){
            st.add(new StudentClass(number++, name));
        }
    }

    @Test
    public void test01(){
        // size of SheetHelper
        assertEquals(tmp.size(), 0);
        Iterator<Student> it = st.iterator();
        int i = 0;
        while(it.hasNext()){
            Student aux = it.next();
            try{
               tmp.addEvalHelper(aux, 0f);
            }catch (Exception e){

            }
            assertEquals(++i, tmp.size());
        }
    }

    @Test
    public void test02() {
        // the detection of repetition
        Iterator<Student> it = st.iterator();
        while(it.hasNext()){
            Student aux = it.next();
            try{
                tmp.addEvalHelper(aux, 0f);
            }catch (Exception e){

            }
        }
        it = st.iterator();

        boolean in = false;
        while(it.hasNext()){
            Student aux = it.next();
            try{
                tmp.addEvalHelper(aux, 0f);
            }catch (AlreadyEvaluatedException e){
               in = true;
            }
            assertTrue(in);
            in = false;
        }
    }

    @Test
    public void test03(){
        // test Subject part 1
        Subject sub = new SubjectClass("Math");
        test01();
       // test Subject
        assertEquals(sub.numberOfEvalSheet(), 0);
        String evalId = tmp.evalId();
        try{
            // think about tmp.evalId();
           sub.addEvaluation(evalId, tmp);
        }catch (Exception e){

        }
        assertEquals(sub.numberOfEvalSheet(), 1);
        boolean in = false;
        try{
            sub.addEvaluation(evalId, tmp);
        }catch( EvaluationAlreadyExistsException e){
            in = true;
        }
        assertTrue(in);
    }

    @Test
    public void test04() {
        // testing Subject part 2
        Subject sub = new SubjectClass("English");
        int rand = 10 + Math.abs(new Random().nextInt(100));
        for(int i = 0; i < rand; i ++){
            try {
                sub.addEvaluation("" + i, new SheetHelperClass("other", "another"));
            }catch( Exception e){

            }
            assertEquals(sub.numberOfEvalSheet(), i + 1);
        }
    }
}
