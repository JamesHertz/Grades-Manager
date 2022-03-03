package others;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

public class StringComp implements Comparator<String>, Serializable {
    private final long serialVersionUID = 0L;
    private static final Collator col = Collator.getInstance();

    static{
        col.setStrength(Collator.NO_DECOMPOSITION);
    }
    @Override
    public int compare(String o1, String o2) {
        return col.compare(o1, o2);
    }
}
