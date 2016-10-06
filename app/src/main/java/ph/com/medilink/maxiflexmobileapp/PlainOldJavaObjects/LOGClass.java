package ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kurt_capatan on 4/19/2016.
 */
public class LOGClass implements Serializable{

    private String IssueDate;
    private String RequestNo;
    private String LOGNo;
    private String MemberName;
    private String ClinicName;
    private String DentistName;
    private String AvailmentDate;
    private String ClaimStatusName;
    private String MemberID;
    private String IsRead;

    private boolean IsSaved = false;

    public LOGClass() {

    }

    public LOGClass(String issueDate, String requestNo, String LOGNo, String memberName, String clinicName, String dentistName, String availmentDate, String claimStatusName, String memberID, String isRead, boolean isSaved) {
        IssueDate = issueDate;
        RequestNo = requestNo;
        this.LOGNo = LOGNo;
        MemberName = memberName;
        ClinicName = clinicName;
        DentistName = dentistName;
        AvailmentDate = availmentDate;
        ClaimStatusName = claimStatusName;
        MemberID = memberID;
        IsRead = isRead;
        this.IsSaved = isSaved;
    }

    public String getIssueDate() {
        try {
            Date issue = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(IssueDate);
            SimpleDateFormat readable_format = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return readable_format.format(issue);
        }catch (ParseException exc){
            return IssueDate;
        }

    }

    public void setIssueDate(String issueDate) {
        IssueDate = issueDate;
    }

    public String getRequestNo() {
        return RequestNo;
    }

    public void setRequestNo(String requestNo) {
        RequestNo = requestNo;
    }

    public String getLOGNo() {
        return LOGNo;
    }

    public void setLOGNo(String LOGNo) {
        this.LOGNo = LOGNo;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public String getDentistName() {
        return DentistName;
    }

    public void setDentistName(String dentistName) {
        DentistName = dentistName;
    }

    public String getAvailmentDate() {
        try {
            Date availment = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(AvailmentDate);
            SimpleDateFormat readable_format = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return readable_format.format(availment);
        }catch (ParseException exc){
            return AvailmentDate;
        }
    }

    public void setAvailmentDate(String availmentDate) {
        AvailmentDate = availmentDate;
    }

    public String getClaimStatusName() {
        return ClaimStatusName;
    }

    public void setClaimStatusName(String claimStatusName) {
        ClaimStatusName = claimStatusName;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public boolean isSaved() {
        return IsSaved;
    }

    public void setIsSaved(boolean isSaved) {
        IsSaved = isSaved;
    }
}
