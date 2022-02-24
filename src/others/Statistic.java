package others;

public interface Statistic {
    void addData(float grade);
    int numberOfData();
    int positiveData();
    int negativesData();
    int noneData();
    float positivePercent();
    float negativePercent();
    float nonePercent();
    float averageData();
}
