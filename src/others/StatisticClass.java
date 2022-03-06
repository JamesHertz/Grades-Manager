package others;

import java.io.Serializable;

public class StatisticClass implements Statistic, Serializable {


    private static final int PERCENT = 100;
    private static final long serialVersionUID = 0L;
    private float positiveData, negativeData, totalGrade, none;
    private int numberOfData;

    public StatisticClass() {
        numberOfData = 0;
        none = 0;
        positiveData = 0f;
        negativeData = 0f;
        totalGrade = 0f;
    }

    @Override
    public float averageData() {
        return totalGrade/numberOfData;
    }

    @Override
    public void addData(float grade) {
        if(grade >= 0){
            totalGrade+= grade;
            if(grade >= 10)
                positiveData++;
            else
                negativeData++;
        }else
            none++;
        numberOfData++;
    }

    @Override
    public int numberOfData() {
        return numberOfData;
    }

    @Override
    public float positivePercent() {
        return getPercentage(positiveData / numberOfData);
    }

    @Override
    public float negativePercent() {
        return getPercentage(negativeData / numberOfData) ;
    }

    @Override
    public float nonePercent() {
        return getPercentage(none/numberOfData);
    }

    @Override
    public int positiveData() {
        return (int) positiveData;
    }

    @Override
    public int negativesData() {
        return (int) negativeData;
    }

    @Override
    public int noneData() {
        return (int) none;
    }


    private float getPercentage(float number){
        return number * PERCENT;
    }
}
