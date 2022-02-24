package others;

import java.io.Serializable;

public class Ranking implements Rank, Serializable {
    private static final long serialVersionUID = 0L;
    private int rank;
    private Float grade;

    public Ranking(){
        rank = 0;
        grade = null;
    }

    @Override
    public int rank(float grade) {
        if(this.grade == null || this.grade != grade){
            this.grade = grade;
            rank++;
        }
        return rank;
    }


}
