package others;

import subject.Student;

import java.util.Comparator;

public class ByStatistic implements Comparator<Student>{
    @Override
    public int compare(Student o1, Student o2) {
        float cmp = o2.averageGrade() - o1.averageGrade();
        return (cmp > 0) ? 1 : -1;
    }
}

