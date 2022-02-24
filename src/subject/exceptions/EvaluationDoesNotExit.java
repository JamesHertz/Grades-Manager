package subject.exceptions;

import java.io.Serializable;

public class EvaluationDoesNotExit extends Exception implements Serializable {

    private static final long serialVersionUID = 0L;
    private final String evalId;
    public EvaluationDoesNotExit(String evalId){
        super();
        this.evalId = evalId;
    }

    public String getEvalId(){
        return evalId;
    }

}
