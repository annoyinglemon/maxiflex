package ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects;

import java.io.Serializable;

/**
 * Created by kurt_capatan on 6/29/2016.
 */
public class VitalSignClass implements Serializable {
    private double Height = 0.0;
    private double Weight = 0.0;
    private double BMI = 0.0;
    private String BloodPressure = "";
    private double Temperature = 0.0;
    private double PulseRate= 0.0;
    private double RespiratoryRate= 0.0;
    private double SP= 0.0;
    private String PainIndex ="";
    private String RecDate = "";

    public VitalSignClass() {
    }

    public VitalSignClass(double height, double weight, double BMI, String bloodPressure, double temperature, double pulseRate, double respiratoryRate, double SP, String painIndex, String recDate) {
        Height = height;
        Weight = weight;
        this.BMI = BMI;
        BloodPressure = bloodPressure;
        Temperature = temperature;
        PulseRate = pulseRate;
        RespiratoryRate = respiratoryRate;
        this.SP = SP;
        PainIndex = painIndex;
        RecDate = recDate;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI() {
        double heightInMeters = this.Height / 100;
        this.BMI = this.Weight / (heightInMeters*heightInMeters);
    }

    public String getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getPulseRate() {
        return PulseRate;
    }

    public void setPulseRate(double pulseRate) {
        PulseRate = pulseRate;
    }

    public double getRespiratoryRate() {
        return RespiratoryRate;
    }

    public void setRespiratoryRate(double respiratoryRate) {
        RespiratoryRate = respiratoryRate;
    }

    public double getSP() {
        return SP;
    }

    public void setSP(double SP) {
        this.SP = SP;
    }

    public String getPainIndex() {
        return PainIndex;
    }

    public void setPainIndex(String painIndex) {
        PainIndex = painIndex;
    }

    public String getRecDate() {
        return RecDate;
    }

    public void setRecDate(String recDate) {
        RecDate = recDate;
    }
}
