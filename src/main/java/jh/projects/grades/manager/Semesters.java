package jh.projects.grades.manager;

public enum Semesters {
    FIRST(1), SECOND(2), THIRD_TRIMESTER(0);

    private final int id;
    Semesters(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
    public static Semesters getSemester(int id){
        for(Semesters s : values()){
            if(s.id == id) return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public int compare(Semesters s2){
       return this.getComparableID() - s2.getComparableID();
    }

    private int getComparableID(){
        return (id == 0 || id == 2) ? id + 1 : id;
    }



}
