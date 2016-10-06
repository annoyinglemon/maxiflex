package ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects;

import java.io.Serializable;

/**
 * Created by kurt_capatan on 6/29/2016.
 */
public class AllergyClass implements Serializable{

    private String AllergyName;
    private boolean IsDesensitized;
    private String Symptoms;
    private String Management;

    public AllergyClass() {
    }

    public AllergyClass(String allergyName, boolean isDesensitized, String symptoms, String management) {
        AllergyName = allergyName;
        IsDesensitized = isDesensitized;
        Symptoms = symptoms;
        Management = management;
    }

    public String getAllergyName() {
        return AllergyName;
    }

    public void setAllergyName(String allergyName) {
        AllergyName = allergyName;
    }

    public boolean isDesensitized() {
        return IsDesensitized;
    }

    public void setIsDesensitized(boolean isDesensitized) {
        IsDesensitized = isDesensitized;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public String getManagement() {
        return Management;
    }

    public void setManagement(String management) {
        Management = management;
    }
}
