package jh.projects.grades.manager;

public enum Period {
    FIRST_SEMESTER(1), SECOND_SEMESTER(2), THIRD_TRIMESTER(0);

    private final int id;
    Period(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
    public static Period getSemester(int id){
        for(Period s : values()){
            if(s.id == id) return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public int compare(Period s2){
       return this.getComparableID() - s2.getComparableID();
    }

    private int getComparableID(){
        return (id == 0 || id == 2) ? id + 1 : id;
    }



}
