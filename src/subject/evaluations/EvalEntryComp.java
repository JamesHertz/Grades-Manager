package subject.evaluations;

import others.StringComp;

import java.io.Serializable;
import java.util.Comparator;

public class EvalEntryComp implements Comparator<EvalEntry>, Serializable {
    private static final StringComp cmp = new StringComp();
    @Override
    public int compare(EvalEntry o1, EvalEntry o2) {
        return cmp.compare(o1.student().name(), o2.student().name());
    }
}
