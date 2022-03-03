package others;

import subject.evaluations.EvalEntry;

import java.io.Serializable;
import java.util.Comparator;

public class AlphabeticalOrder implements Comparator<EvalEntry>, Serializable {
    private static final long serialVersionUID = 0L;
    private final StudentByAlphOrder byAlphOrder;
    public AlphabeticalOrder(){
        byAlphOrder = new StudentByAlphOrder();
    }

    @Override
    public int compare(EvalEntry o1, EvalEntry o2) {
        return byAlphOrder.compare(o1.student(), o2.student());
    }
}
