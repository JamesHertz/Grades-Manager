package tests;
import org.junit.Before;
import org.junit.Test;
import others.evalHelper.EvalHelper;
import others.evalHelper.SheetHelper;
import others.evalHelper.SheetHelperClass;
import subject.*;
import subject.evaluations.EvalEntry;
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
        // test when you add a evaluation with the same id twice
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
            fail();
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
        // test when you add a evaluation with the same Id
        Subject sub = new SubjectClass("English");
        int rand = 10 + Math.abs(new Random().nextInt(100));
        for(int i = 0; i < rand; i ++){
            try {
                sub.addEvaluation("" + i, new SheetHelperClass("other", "another"));
            }catch(Exception e){
                fail();
            }
            assertEquals(sub.numberOfEvalSheet(), i + 1);
        }
    }

    @Test
    public void test05(){
        // test if when added a new student
        // it's the evaluation actually goes to the Student
        // internal dataStruture
        Subject sub = new SubjectClass("English");
        int times = new Random().nextInt(10);
        try {
            for (int i = 0; i < times; i++) {
                sub.addEvaluation(String.format("exam %02d", i), tmp);
                Iterator<EvalHelper> it = tmp.listAllHelper();
                while (it.hasNext()) {
                    Student st = it.next().getStudent();
                    assertEquals(st.numberOfEvaluation(), i + 1);
                    // check if has the the evaluations :(
                    Iterator<EvalEntry> itS = st.listSubSlot().next().listAllEvaluations();
                    Iterator<EvalSheet> itS2 = sub.listEvaluations();
                    while (itS.hasNext() && itS2.hasNext()) {
                        assertEquals(itS.next().evaluationsId(), itS2.next().evaluationId());
                    }
                    assertFalse(itS.hasNext());
                    assertFalse(itS2.hasNext());
                }
            }
        }catch (Exception e){
            fail();
        }

    }
}
