package ph.com.medilink.maxiflexmobileapp.WebServices;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.ClinicClass;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;

/**
 * Created by nino_sandajan on 4/15/2016.
 */
public class WebServiceClass {


    private static String NAMESPACE = new ServiceParametersClass().Service_namespace();
    private static String URL = new ServiceParametersClass().Service_url();
    private String METHOD_NAME = "";
    private String SOAP_ACTION = "";


    /**
     * Used for validating credentials (e.g. Username and Password)
     * @param context current context of activity
     * @param username user typed username
     * @param password user typed password
     * @return "Welcome" if credentials are valid, contains "Error:" if service returns a SoapFault, "Error" if something unexpected error happens,
     * and these messages:
     * "Slow Network"
     * "The Clinic where you belong is already inactive."
     * "This application is intend for Member only."
     * "User is Currently logged in."
     * "Your Account is Deactivated. Please contact your System Admin."
     * "Your password has been reset and sent to your registered email address."
     * "Username or Password is incorrect."
     */
    public String ValidateCredential_eCard(Context context, String username, String password) {
        String resultString = "Error";

        METHOD_NAME = "ValidateCredential_eCard";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);

        try {
            if (new WebServiceClass().isURLReachable(context)) {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("_strUserID", username);
                request.addProperty("_strPwd", password);

                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);

//                ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
//                headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
//                httpTransport.call(SOAP_ACTION, envelope, headerPropertyArrayList);

                httpTransport.call(SOAP_ACTION, envelope);

                if(envelope.bodyIn instanceof SoapFault){
                    resultString = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
                } else {

                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String[] wholeResult = soapObject.getProperty("ValidateCredential_eCardResult").toString().split("\\|", -1);

                    resultString = wholeResult[1];
                    if (wholeResult[0].equalsIgnoreCase("SUCCESS") && wholeResult[1].equalsIgnoreCase("Welcome")) {
                        GlobalVariables.USER_ID = username;
                        GlobalVariables.CORP_CODE = wholeResult[2];
                        GlobalVariables.MEMBER_ID = wholeResult[3];
                        GlobalVariables.MAIN_MEMBER_ID = wholeResult[4];
                        GlobalVariables.MEMBER_NAME = wholeResult[5];
                        GlobalVariables.CORP_NAME = wholeResult[6];
                        GlobalVariables.EXPIRY_DATE = wholeResult[7];
                        GlobalVariables.CARD_NO = wholeResult[8];
                        GlobalVariables.EXPIRY_DATE_MMddYYYY = wholeResult[9];
                        GlobalVariables.READ_PDPA_POLICY = wholeResult[10];
                        GlobalVariables.PLAN_CODE = wholeResult[11];
                        GlobalVariables.CHOSEN_CARD_DESIGN = wholeResult[12];
                        GlobalVariables.ID_NUMBER = wholeResult[13];
                        GlobalVariables.HAVE_SECURITY_QUESTIONS = wholeResult[14];
                        int count = 2;
                        while (count < wholeResult.length) {
                            switch (count) {
                                case 2:
                                    GlobalVariables.CORP_CODE = wholeResult[2];
                                    break;
                                case 3:
                                    GlobalVariables.MEMBER_ID = wholeResult[3];
                                    break;
                                case 4:
                                    GlobalVariables.MAIN_MEMBER_ID = wholeResult[4];
                                    break;
                                case 5:
                                    GlobalVariables.MEMBER_NAME = wholeResult[5];
                                    break;
                                case 6:
                                    GlobalVariables.CORP_NAME = wholeResult[6];
                                    break;
                                case 7:
                                    GlobalVariables.EXPIRY_DATE = wholeResult[7];
                                    break;
                                case 8:
                                    GlobalVariables.CARD_NO = wholeResult[8];
                                    break;
                                case 9:
                                    GlobalVariables.EXPIRY_DATE_MMddYYYY = wholeResult[9];
                                    break;
                                case 10:
                                    GlobalVariables.READ_PDPA_POLICY = wholeResult[10];
                                    break;
                                case 11:
                                    GlobalVariables.PLAN_CODE = wholeResult[11];
                                    break;
                                case 12:
                                    GlobalVariables.CHOSEN_CARD_DESIGN = wholeResult[12];
                                    break;
                                case 13:
                                    GlobalVariables.ID_NUMBER = wholeResult[13];
                                    break;
                                case 14:
                                    GlobalVariables.HAVE_SECURITY_QUESTIONS = wholeResult[14];
                                    break;
                            }
                            count++;
                        }
                    }
                }
            } else
                resultString = "Slow Network";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            resultString = "Exception: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            resultString = "Exception: " + e.getMessage();
        } catch (Exception e) {
            resultString = "Exception: " + e.getMessage();
        }
        return resultString;
    }

    /**
     * Download Card Design Image,
     * if there is an error during download use default card layout required.
     * @param Filename filename of desired design, if filename is front card, the plan code returned by ValidateCredential_eCard
     *                 must be attached with "_F" at the end, "_B" for the back of card (e.g. PLN01_F, PLN01_B)
     * @return Image in byte[] data type, must be converted to Bitmap for viewing. Returns null if an error occurred.
     */
    public byte[] DownloadCardDesign(String Filename){
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "DownloadCardDesign";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Filename", Filename);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);
            if(envelope.bodyIn instanceof SoapFault ){
                GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String wholeResult = result.getProperty(0).toString(); // -> DownloadCardDesignResult
                //Convert Object to Base 64
                return Base64.decode(wholeResult, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return null;
    }

    /**
     * Generates QR code image
     * @param infoString any preferred text
     * @return Image in byte[] data type, must be converted to Bitmap for viewing. Returns null if an error occurred.
     */
    public byte[] GenerateQR_eCard(String infoString) {
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "GenerateQR_eCard";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("prefixText", infoString);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);
            if(envelope.bodyIn instanceof SoapFault ){
                GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String wholeResult = result.getProperty(0).toString(); // -> GenerateQRResult
                //Convert Object to Base 64
                return Base64.decode(wholeResult, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return null;
    }

    /**
     * Resets password by passing a member's card number as argument
     * @param context
     * @param cardNo 16-digit card number of user who wants to reset password (eg. 2367125616591235)
     * @return true if successful, false otherwise
     */
    public boolean ResetPasswordKeystone(Context context, String cardNo) {
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "ResetPasswordKeystone";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        boolean result = false;
        try {
            if (isURLReachable(context)) {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("CardNo", cardNo);
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
                httpTransport.call(SOAP_ACTION, envelope);

                if (envelope.bodyIn instanceof SoapFault) {
                    GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault) envelope.bodyIn).getMessage();
                } else {
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    result = Boolean.parseBoolean(soapObject.getPropertyAsString("ResetPasswordKeystoneResult"));
                }
            } else {
                GlobalVariables.ERROR_MESSAGE = "Slow network";
            }
        } catch (Exception e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return result;
    }

    /**
     * Sets the security question of the user..
     * @param context
     * @param idNumberQuestionCodeAnswer string concatenation of id number of user, question code and answer to the question
     *                                   must be in format ID-QuestionCode-Answer|ID-QuestionCode-Answer|ID-QuestionCode-Answer.....
     *                                   (E.g 1001-01-TestAnswer1|1001-02-TestAnswer2|1001-03-TestAnswer3|1001-04....)
     * @return true if successful, false otherwise
     */
    public boolean SetSecurityQuestion(Context context, String idNumberQuestionCodeAnswer) {
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "SetSecurityQuestion";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        boolean result = false;
        try {
            if (isURLReachable(context)) {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("secQuestions", idNumberQuestionCodeAnswer);
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
                httpTransport.call(SOAP_ACTION, envelope);

                if (envelope.bodyIn instanceof SoapFault) {
                    GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault) envelope.bodyIn).getMessage();
                } else {
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    result = Boolean.parseBoolean(soapObject.getPropertyAsString("SetSecurityQuestionResult"));
                }
            } else {
                GlobalVariables.ERROR_MESSAGE = "Slow network";
            }
        } catch (Exception e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return result;
    }

    /**
     * Gives security question in the format (questionCode)_(Answer)
     * E.G. 01_ThisIsMyAnswer
     * @param context
     * @param idNumber id number of the user, you can get this using ValidateCredential_eCard
     * @return String of concatenated questionCode and answer
     *         (e.g. 01_ThisIsMyAnswer|02_ThisIsMyAnswer|03_ThisIsMyAnswer....)
     */
    public String GetSecurityQuestions(Context context, String idNumber) {
        GlobalVariables.ERROR_MESSAGE = "";
        String resultString = "Error";
        METHOD_NAME = "GetSecurityQuestion";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        try {
            if (new WebServiceClass().isURLReachable(context)) {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("IDNo", idNumber);
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
                httpTransport.call(SOAP_ACTION, envelope);

                if (envelope.bodyIn instanceof SoapFault) {
                    GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault) envelope.bodyIn).getMessage();
                } else {
                    SoapObject soapResult = (SoapObject) envelope.bodyIn;
                    resultString = soapResult.getProperty("GetSecurityQuestionResult").toString();
                }
            } else {
                GlobalVariables.ERROR_MESSAGE = "Slow Network";
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return resultString;
    }

    /**
     * Used to change the card number of the user
     * @param context
     * @param idNumber ID number of the user who wants to change his/her card number
     * @return true if successful, false otherwise
     */
    public boolean ChangeCardNo(Context context, String idNumber) {
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "ChangeCardNo";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        boolean result = false;
        try {
            if (isURLReachable(context)) {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("IDNo", idNumber);
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
                httpTransport.call(SOAP_ACTION, envelope);

                if (envelope.bodyIn instanceof SoapFault) {
                    GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault) envelope.bodyIn).getMessage();
                } else {
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    result = Boolean.parseBoolean(soapObject.getPropertyAsString("ChangeCardNoResult"));
                }
            } else {
                GlobalVariables.ERROR_MESSAGE = "Slow network";
            }
        } catch (Exception e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
        }
        return result;
    }

    /**
     * Used to fetch all affiliated clinics.
     * @return List of ClinicClass object, ArrayList's size is 0 if an error occurred.
     */
    public ArrayList<ClinicClass> GetAllClinic_eCard() {
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "GetAllClinic_eCard";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        ArrayList<ClinicClass> arrayList = new ArrayList<>();
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);

            if(envelope.bodyIn instanceof SoapFault ){
                GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                SoapObject soapArrayString = (SoapObject) soapObject.getProperty("GetAllClinic_eCardResult");
                int stringArrayCount = soapArrayString.getPropertyCount();
                for (int i = 0; i < stringArrayCount; i++) {
                    ClinicClass clinic = new ClinicClass();
                    String[] array = soapArrayString.getPropertyAsString(i).split("\\|", -1);
                    int count = 0;
                    while (count < array.length) {
                        switch (count) {
                            case 0:
                                clinic.setClinicName(array[count].trim());
                                break;
                            case 1:
                                clinic.setClinicCode(array[count].trim());
                                break;
                            case 2:
                                clinic.setAddress(array[count].trim());
                                break;
                            case 3:
                                clinic.setLatitude(array[count].trim());
                                break;
                            case 4:
                                clinic.setLongitude(array[count].trim());
                                break;
                        }
                        count++;
                    }
                    arrayList.add(clinic);
                }
            }
        } catch (XmlPullParserException e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
            return arrayList;
        } catch (IOException e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
            return arrayList;
        } catch (Exception e) {
            GlobalVariables.ERROR_MESSAGE = "Exception: " + e.getMessage();
            return arrayList;
        }

        return arrayList;
    }

    /**
     * Used for getting Letter of Guarantee/Authorization of the user
     * @param mainMemberID user's member ID
     * @return Soap Object containing string.
     */
    public SoapObject SearchLOGList_eCard(String mainMemberID){
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "SearchLOGList_eCard";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        SoapObject soapArrayString = null;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("_strMainMemberID", mainMemberID);

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);
            if(envelope.bodyIn instanceof SoapFault ){
                GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                soapArrayString = (SoapObject) soapObject.getProperty("SearchLOGList_eCardResult");
            }
        }catch (Exception e){
            GlobalVariables.ERROR_MESSAGE = e.getMessage();
        }
        return soapArrayString;
    }

    /**
     * used for downloading LOG/LOA
     * @param appCompatActivity used for checking Manifest permission
     * @param authorizationCode LOG/LOA number of the file chosen
     * @param UserID string used for logging in
     * @return File path of downloaded LOG/LOA, contains "Error:" if SoapFault occurred, contains "Exception:" if an error occurred.
     */
    public String byteLOApdf(AppCompatActivity appCompatActivity, String authorizationCode, String UserID) {
        String filePath = "Error";
        METHOD_NAME = "byteLOApdf";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ClaimNo", authorizationCode);
            request.addProperty("UserID", UserID);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);

            if(envelope.bodyIn instanceof SoapFault ){
                filePath = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject result = (SoapObject) envelope.bodyIn;
                //Create file with LOGnumber
                if (ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File file = new File(GlobalVariables.MAXIFLEX_FOLDER, "LOG_" + authorizationCode + ".pdf");
                    String wholeResult = result.getProperty(0).toString(); // -> byteLOApdfResult
                    FileOutputStream output = new FileOutputStream(file);
                    filePath = file.getPath();
                    byte[] data = Base64.decode(wholeResult, Base64.DEFAULT);
                    output.write(data, 0, data.length); // writing data to file
                    output.flush(); // flushing output
                    output.close();
                } else {
                    ActivityCompat.requestPermissions(appCompatActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                }
            }
            //Convert Object to Base 64
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    //    byteLOApdf (authorizationCode, UserID)

//    public String PrintLOG(AppCompatActivity appCompatActivity, String LOGnumber) {
//        String filePath = "Error";
//        METHOD_NAME = "PrintLOG";
//        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
//
//        try {
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.dotNet = true;
//            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//            request.addProperty("x", LOGnumber);
//            envelope.setOutputSoapObject(request);
//            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
//            httpTransport.call(SOAP_ACTION, envelope);
//
//            if(envelope.bodyIn instanceof SoapFault ){
//                filePath = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
//            }else {
//                SoapObject result = (SoapObject) envelope.bodyIn;
//                //Create file with LOGnumber
//                if (ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    File file = new File(GlobalVariables.MAXIFLEX_FOLDER, "LOG_" + LOGnumber + ".pdf");
//                    String wholeResult = result.getProperty(0).toString(); // -> PrintLOGResult
//                    FileOutputStream output = new FileOutputStream(file);
//                    filePath = file.getPath();
//                    byte[] data = Base64.decode(wholeResult, Base64.DEFAULT);
//                    output.write(data, 0, data.length); // writing data to file
//                    output.flush(); // flushing output
//                    output.close();
//                } else {
//                    ActivityCompat.requestPermissions(appCompatActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
//                }
//            }
//            //Convert Object to Base 64
//            return filePath;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Exception: " + e.getMessage();
//        }
//    }



    /**
     * Used for obtaining all Medical Records of the user
     * @param memberID memberID of user
     * @return soapobject containing all information if converted to String.
     */
    public SoapObject EMR(String memberID){
        GlobalVariables.ERROR_MESSAGE = "";
        METHOD_NAME = "EMR";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);
        SoapObject soapArrayString = null;
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("MemberID", memberID);

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);

            if(envelope.bodyIn instanceof SoapFault ){
                GlobalVariables.ERROR_MESSAGE = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject soapObject = (SoapObject) envelope.bodyIn;
                soapArrayString = (SoapObject) soapObject.getProperty("EMRResult");
            }
        }catch (Exception e){
            GlobalVariables.ERROR_MESSAGE = e.getMessage();
        }
        return soapArrayString;
    }

    /**
     * Used for downloading a user-uploaded file, mostly an image file
     * @param appCompatActivity for runtime-checking of manifest permission that only occurs on API 23 devices (Marshmallow)
     * @param fileName file to be downloaded
     * @return filepath of downloaded file, contains "Error:" if Soapfault happens, contains "Exceptions:" if exception triggered
     */
    public String DownloadFile(AppCompatActivity appCompatActivity, String fileName) {
        String filePath = "Error";
        METHOD_NAME = "EMRpdfFile";
        SOAP_ACTION = new ServiceParametersClass().Service_soap_action(NAMESPACE, METHOD_NAME);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Filename", fileName);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL, 50000);
            httpTransport.call(SOAP_ACTION, envelope);

            if(envelope.bodyIn instanceof SoapFault ){
                filePath = "Error: " + ((SoapFault)envelope.bodyIn).getMessage();
            }else {
                SoapObject result = (SoapObject) envelope.bodyIn;

                if (ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File file = new File(GlobalVariables.MAXIFLEX_FOLDER, fileName);

                    String wholeResult = result.getProperty(0).toString(); // -> EMRpdfFileResult
                    FileOutputStream output = new FileOutputStream(file);
                    filePath = file.getPath();
                    //Convert Object to Base 64
                    byte[] data = Base64.decode(wholeResult, Base64.DEFAULT);
                    output.write(data, 0, data.length); // writing data to file
                    output.flush(); // flushing output
                    output.close();
                } else {
                    ActivityCompat.requestPermissions(appCompatActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                }
            }
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    /**
     * Used for checking ping
     * @param context
     * @return false if slow network connection, true otherwise
     */
    public boolean isURLReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                java.net.URL url = new URL(URL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                //URL url = new URL("http://192.168.254.100/");
                urlc.setConnectTimeout(10 * 1000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}

