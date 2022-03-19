package tests;
import manager.GradesManager;
import manager.GradesManagerClass;
import manager.exceptions.StudentDoesNotExistException;
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
        tmp = new SheetHelperClass();
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
                fail();
            }
        }
        it = st.iterator();

        while(it.hasNext()){
            Student aux = it.next();
            try{
                tmp.addEvalHelper(aux, 0f);
            }catch (AlreadyEvaluatedException e){
                continue;
            }
            fail();

        }
    }

    @Test
    public void test03(){
        // test when you add a evaluation with the same id twice
        // test Subject part 1
        Subject sub = new SubjectClass("Math", 0);
        test01();
       // test Subject
        assertEquals(sub.numberOfEvalSheet(), 0);
        String evalId = "test01";
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
        Subject sub = new SubjectClass("English", 0);
        int rand = 10 + Math.abs(new Random().nextInt(100));
        for(int i = 0; i < rand; i ++){
            try {
                sub.addEvaluation("" + i, new SheetHelperClass());
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
        Subject sub = new SubjectClass("English", 0);
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

    @Test
    public void test06(){
        // a list with all students
        var manager = new GradesManagerClass();
        Iterator<Student> it = st.iterator();
        var rand = new Random();

        // test if addEvalEntry(int, float) is working well
        while(it.hasNext()){
            var tmp = it.next();
            try {
               manager.addEvalEntry(tmp.number(), rand.nextFloat());
            }catch(StudentDoesNotExistException e){
                continue;
            }catch(Exception e){
                fail();
            }
            fail();
        }

        it = st.iterator();

        // test if addEvalEntry(int, String, float) is working well
        while(it.hasNext()){
            var tmp = it.next();
            try {
                manager.addEvalEntry(tmp.number(), tmp.name(), rand.nextFloat());
            }catch(Exception e){
                fail();
            }
        }

        // testes AlreadyEvaluatedException
        it = st.iterator();
        while(it.hasNext()){
            var tmp = it.next();
            try {
                manager.addEvalEntry(tmp.number(), tmp.name(), rand.nextFloat());
            }catch(AlreadyEvaluatedException e){
                continue;
            }catch(Exception e){
                fail();
            }
            fail();
        }

        // testes AlreadyEvaluatedException
        it = st.iterator();
        while(it.hasNext()){
            var tmp = it.next();
            try {
                manager.addEvalEntry(tmp.number(), rand.nextFloat());
            }catch(AlreadyEvaluatedException e){
                continue;
            }catch(Exception e){
                fail();
            }
            fail();
        }

        // clear the buffer
        manager.clearBufHelper();
        it = st.iterator();

        // adds every one back to the buffer
        while(it.hasNext()){
            var tmp = it.next();
            try {
                manager.addEvalEntry(tmp.number(), rand.nextFloat());
            }catch(Exception e){
                fail();
            }
        }

        it = st.iterator();
        Iterator<EvalHelper> it2 = manager.listBufHelper();

        // verifies if our buffer is consistent
        while(it.hasNext() && it2.hasNext()){
            var stList = it.next();
            var buf = it2.next().getStudent();

            //System.out.printf("stList: %d \t buf: %d\n", stList.number(), buf.number());
            assertEquals(stList.number(), buf.number());
        }
        assertEquals(it.hasNext(), it2.hasNext());

    }


    @Test
    public void test07(){
        // tests the commitBufHelper method
        GradesManager manager = new GradesManagerClass();
        fill(manager);

        try{
            int commits = manager.commitBufHelper("MATH", "test 1");
            assertEquals(commits, st.size());
        }catch(Exception e){
            fail();
        }

        fill(manager);
        try{
            int commits = manager.commitBufHelper("MATH", "test 2");
            assertEquals(commits, 0);
        }catch(Exception e){
            fail();
        }

        fill(manager);
        try{
            manager.commitBufHelper("MATH", "test 2");
        }catch(EvaluationAlreadyExistsException e) {
            return;
        } catch(Exception e){
            fail();
        }
        fail();
    }

    private void fill(GradesManager manager){
        var rand = new Random();
        var it = st.iterator();

        // adds students to manager
        while(it.hasNext()){
            var student = it.next();
            try{
                manager.addEvalEntry(student.number(), student.name(), rand.nextFloat());
            }catch(Exception e){
                fail();
            }
        }
    }
}
