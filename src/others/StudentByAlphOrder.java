package others;

import subject.Student;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

public class StudentByAlphOrder implements Comparator<Student>, Serializable{
    private static final long serialVersionUID = 0L;
    private static final Collator col = Collator.getInstance();

    static{
        col.setStrength(Collator.NO_DECOMPOSITION);
    }

    @Override
    public int compare(Student o1, Student o2) {
        return  col.compare(o1.name(), o2.name());
    }
}
