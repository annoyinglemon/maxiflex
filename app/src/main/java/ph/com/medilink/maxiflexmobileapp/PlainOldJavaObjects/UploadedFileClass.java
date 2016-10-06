package ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects;

import java.io.Serializable;

/**
 * Created by kurt_capatan on 6/29/2016.
 */
public class UploadedFileClass implements Serializable {
    private String FileName;
    private String UploadDate;
    private boolean IsSaved = false;

    public UploadedFileClass(String fileName, String uploadDate, boolean isSaved) {
        FileName = fileName;
        UploadDate = uploadDate;
        IsSaved = isSaved;
    }

    public UploadedFileClass() {
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public boolean isSaved() {
        return IsSaved;
    }

    public void setIsSaved(boolean isSaved) {
        IsSaved = isSaved;
    }
}
