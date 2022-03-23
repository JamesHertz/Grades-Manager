package others;

import subject.Student;

import java.util.Comparator;

public class AvgGradeComp implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        // compare by ect
        int cmp = o2.getTotEcts() - o1.getTotEcts();
        if(cmp != 0)
            return cmp;
        // if draw compare by avg_grade
        float cmp2 = o2.averageGrade() - o1.averageGrade();
        if(cmp2 > 0)
            return 1;
        if(cmp2 < 0)
            return -1;
        // if still draw compare by name
        cmp = o1.name().compareTo(o2.name());
        if(cmp != 0)
            return cmp;
        // ultimate case compare by number
        return o1.number() - o2.number();
    }
}
