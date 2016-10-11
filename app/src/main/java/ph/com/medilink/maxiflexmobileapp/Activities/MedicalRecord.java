package ph.com.medilink.maxiflexmobileapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.AllergyClass;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.MedicalHistoryClass;
import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.UploadedFileClass;
import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.VitalSignClass;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_Allergy;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_ObGyne;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_PastMedical;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_PatientProfile;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_Social;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_UploadFiles;
import ph.com.medilink.maxiflexmobileapp.Fragments.MedicalRecord.Fragment_VitalSign;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.Utilities.NetworkChangeStateReceiver;
import ph.com.medilink.maxiflexmobileapp.WebServices.WebServiceClass;

public class MedicalRecord extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , Fragment_PatientProfile.OnFragmentInteractionListener, Fragment_VitalSign.OnFragmentInteractionListener, Fragment_PastMedical.OnFragmentInteractionListener
        , Fragment_ObGyne.OnFragmentInteractionListener, Fragment_Allergy.OnFragmentInteractionListener
        , Fragment_Social.OnFragmentInteractionListener, Fragment_UploadFiles.OnFragmentInteractionListener
        , ActivityCompat.OnRequestPermissionsResultCallback{

    private FragmentManager _fragManager;
    private FragmentTransaction _fragTransaction;
    private AppBarLayout ablMedicalRecord;
    private Toolbar _actionBar;
    private DrawerLayout mDrawer;
    private NavigationView _navView;

    //UPDATE FRAGMENT_COUNT IF U WANT TO ADD/DELETE A FRAGMENT; ADD STATIC INT ALSO TO THE NEW FRAGMENT
    private static final int PATIENT_PROFILE = 0;
    private static final int VITAL_SIGN = 1;
    private static final int PAST_MEDICAL = 2;
    private static final int ALLERGY = 3;
    private static final int OB_GYN = 4;
    private static final int SOCIAL = 5;
    private static final int UPLOAD_FILE = 6;
    private static final int FRAGMENT_COUNT = 7;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private int currentFragmentPosition = 0;
    private boolean isMenuRefreshClicked = false;

    private LinearLayout lvDownloadProgress;

    private IntentFilter networkChangeFilter;
    public static final String NETWORK_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private NetworkChangeStateReceiver networkChangeReceiver;

    private GetEMR mGetEmrTask;

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        lvDownloadProgress = (LinearLayout)findViewById(R.id.lvDownloadProgress);
        lvDownloadProgress.setVisibility(View.GONE);

        ablMedicalRecord = (AppBarLayout) findViewById(R.id.ablMedicalRecord);
        _actionBar = (Toolbar) findViewById(R.id.tbMedicalRecord);
        setSupportActionBar(_actionBar);
        // check if for phone or tablet landscape
        if (findViewById(R.id.drawer_layout) != null) {
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, _actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    invalidateOptionsMenu();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert getCurrentFocus() != null;
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    super.onDrawerSlide(drawerView, slideOffset);
                }

                @Override
                public void onDrawerOpened(View drawerView) {

                    super.onDrawerOpened(drawerView);
                }
            };
            mDrawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        _navView = (NavigationView) findViewById(R.id.nav_view);
        _navView.setNavigationItemSelectedListener(this);
        _navView.getMenu().getItem(0).setChecked(true);
        _fragManager = getSupportFragmentManager();
        fragments[PATIENT_PROFILE] = _fragManager.findFragmentById(R.id.patientProfileFragment);
        fragments[VITAL_SIGN] = _fragManager.findFragmentById(R.id.vitalSignFragment);
        fragments[PAST_MEDICAL] = _fragManager.findFragmentById(R.id.pastMedicalFragment);
        fragments[ALLERGY] = _fragManager.findFragmentById(R.id.allergyFragment);
        fragments[OB_GYN] = _fragManager.findFragmentById(R.id.obGyneFragment);
        fragments[SOCIAL] = _fragManager.findFragmentById(R.id.socialFragment);
        fragments[UPLOAD_FILE] = _fragManager.findFragmentById(R.id.uploadFilesFragment);
        _fragTransaction = _fragManager.beginTransaction();
        for (Fragment frag : fragments) {
            _fragTransaction.hide(frag);
        }
        _fragTransaction.commit();
        if (savedInstanceState != null) {
            showFragment(savedInstanceState.getInt("currentFragment", PATIENT_PROFILE), false);
        } else {
            showFragment(PATIENT_PROFILE, false);
        }
        networkChangeFilter = new IntentFilter(NETWORK_CHANGE_ACTION);
        networkChangeReceiver = new NetworkChangeStateReceiver();

        if(savedInstanceState==null) {
            mGetEmrTask = new GetEMR();
            mGetEmrTask.execute();
        }


    }

    @Override
    public void onBackPressed() {
        if((DrawerLayout) findViewById(R.id.drawer_layout)!=null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        switch (currentFragmentPosition) {
//            case LETTER_OF_GUARANTEE:
//                getMenuInflater().inflate(R.menu.menu_letter_guarantee, menu);
//                break;
            default:
                getMenuInflater().inflate(R.menu.menu_medical_record, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menuRefresh){
            if (new GlobalFunctions().checkInternetState(this)) {
                ((Fragment_VitalSign) fragments[VITAL_SIGN]).clearItems();
                ((Fragment_PastMedical) fragments[PAST_MEDICAL]).clearItems();
                ((Fragment_Allergy) fragments[ALLERGY]).clearItems();
                ((Fragment_UploadFiles) fragments[UPLOAD_FILE]).clearItems();
                if (!isMenuRefreshClicked) {
                    mGetEmrTask = new GetEMR();
                    mGetEmrTask.execute();
                }
            }else {
                new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.enable_internet));
            }
        } else if (id == R.id.action_signOut) {
            this.Exit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_patientProfile) {
            showFragment(PATIENT_PROFILE, false);

        } else if (id == R.id.nav_vitalSigns) {
            showFragment(VITAL_SIGN, false);

        } else if (id == R.id.nav_pastMedical) {
            showFragment(PAST_MEDICAL, false);

        } else if (id == R.id.nav_allergy) {
            showFragment(ALLERGY, false);

        } else if (id == R.id.nav_obGyne) {
            showFragment(OB_GYN, false);

        } else if (id == R.id.nav_social) {
            showFragment(SOCIAL, false);

        } else if (id == R.id.nav_uploadFiles) {
            showFragment(UPLOAD_FILE, false);

        }else if (id == R.id.nav_exit) {
            if(mGetEmrTask!=null) {
                mGetEmrTask.cancel(true);
                if (mGetEmrTask.isCancelled()) {
                    Exit();
                }
            }else
                Exit();



        }

        if (findViewById(R.id.drawer_layout) != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void hideObGyneOption(){
        Menu menu =  _navView.getMenu();
        for (int i =0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.nav_obGyne){
                menu.getItem(i).setVisible(false);
            }
        }
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        switch (fragmentIndex) {
            case PATIENT_PROFILE:
                currentFragmentPosition = PATIENT_PROFILE;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_patient_profile));
                break;
            case VITAL_SIGN:
                currentFragmentPosition = VITAL_SIGN;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_vital_signs));

                break;
            case PAST_MEDICAL:
                currentFragmentPosition = PAST_MEDICAL;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_medical_history));
                break;
            case ALLERGY:
                currentFragmentPosition = ALLERGY;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_allergy));
                break;
            case OB_GYN:
                currentFragmentPosition = OB_GYN;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_ob_gyn));
                break;
            case SOCIAL:
                currentFragmentPosition = SOCIAL;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_social));
                break;
            case UPLOAD_FILE:
                currentFragmentPosition = UPLOAD_FILE;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_upload_file));
                break;
            default:
        }
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentFragment", currentFragmentPosition);
        super.onSaveInstanceState(outState);
    }

    public void Exit() {
        MedicalRecord.this.finish();
    }

    @Override
    protected void onResume() {
        this.registerReceiver(networkChangeReceiver, networkChangeFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        this.unregisterReceiver(networkChangeReceiver);
        super.onPause();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GlobalVariables.REQUEST_EXTERNAL_READ_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MedicalRecord.this, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        DialogInterface.OnClickListener nega = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };
                        DialogInterface.OnClickListener posi = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MedicalRecord.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                            }
                        };
                        new GlobalFunctions().showAlertMessage(this, "This app needs access to your file storage to check if LOGs or your uploaded files are already saved in your storage. If you decline this access, this app will always download the files that you want to view but you are unable to view them. Tap 'Try again' to show the message one more time."
                                , "Try again", posi, "I really decline", nega);
                    } else {
                        DialogInterface.OnClickListener settings = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                startActivity(myAppSettings);
                            }
                        };
                        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        };
                        new GlobalFunctions().showAlertMessage(this, "You have denied the permission for accessing your file storage, please proceed to Settings and allow storage access.", getResources().getString(R.string.settings), settings, getResources().getString(R.string.cancel), cancel);
                    }
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    class GetEMR extends AsyncTask<Void, HashMap<String, Object[]>, String>{
        @Override
        protected void onPreExecute() {
            isMenuRefreshClicked = true;
            lvDownloadProgress.setVisibility(View.VISIBLE);
            GlobalFunctions.lockOrientation(MedicalRecord.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "error";
            try {
                SoapObject emrSoap = new WebServiceClass().EMR(GlobalVariables.MEMBER_ID);
                if (emrSoap != null) {
                    HashMap<String, Object[]> resultMap;
                    SoapObject tables = (SoapObject) emrSoap.getProperty(1);
                    String wholeEMRString = tables.getPropertyAsString(0);
                    String wholeTableString = wholeEMRString.substring(wholeEMRString.indexOf("Table="));
                    result = wholeTableString;
                    String[] anyTypes = wholeTableString.split("\\{");

                    for (String perBracket : anyTypes) {
                        // PATIENT PROFILE
                        if (perBracket.contains("PatientName") || perBracket.contains("BirthDate") || perBracket.contains("Gender")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            String[] patientInfo = {"", "", ""};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("PatientName"))
                                    patientInfo[0] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("BirthDate")) {
                                    patientInfo[1] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                    GlobalVariables.BIRTHDATE = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                } else if (perSemicolon.contains("Gender")) {
                                    patientInfo[2] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                    GlobalVariables.GENDER = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                }
                            }
                            resultMap.put("Patient_Profile", patientInfo);
                            publishProgress(resultMap);
                        }
                        // VITAL SIGN ITEM INFO
                        if (perBracket.contains("Height") || perBracket.contains("Weight") || perBracket.contains("BP") || perBracket.contains("Temperature") || perBracket.contains("Pulse") || perBracket.contains("Respiratory") || perBracket.contains("SP") || perBracket.contains("Pain") || perBracket.contains("RecordDate")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            VitalSignClass vitaSign = new VitalSignClass();
                            VitalSignClass[] vitArray = {new VitalSignClass()};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("Height"))
                                    vitaSign.setHeight(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("Weight"))
                                    vitaSign.setWeight(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("BP"))
                                    vitaSign.setBloodPressure(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("Temperature"))
                                    vitaSign.setTemperature(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("Pulse"))
                                    vitaSign.setPulseRate(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("Respiratory"))
                                    vitaSign.setRespiratoryRate(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("SP"))
                                    vitaSign.setSP(Double.parseDouble(perSemicolon.substring(perSemicolon.indexOf("=") + 1)));
                                else if (perSemicolon.contains("Pain")) {
                                    String index = "";
                                    if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("0"))
                                        index = " - No Pain";
                                    else if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("1") || perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("2"))
                                        index = " - Mild";
                                    else if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("3") || perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("4"))
                                        index = " - Moderate";
                                    else if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("5") || perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("6"))
                                        index = " - Severe";
                                    else if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("7") || perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("8"))
                                        index = " - Very Severe";
                                    else if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("9") || perSemicolon.substring(perSemicolon.indexOf("=") + 1).contains("10"))
                                        index = " - Worst";
                                    vitaSign.setPainIndex(perSemicolon.substring(perSemicolon.indexOf("=") + 1) + index);
                                } else if (perSemicolon.contains("RecordDate")) {
                                    try {
                                        String dateString = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                        Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(dateString);
                                        vitaSign.setRecDate(new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(date));
                                    } catch (Exception e) {
                                        vitaSign.setRecDate(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                    }
                                }
                            }
                            vitaSign.setBMI();
                            vitArray[0] = vitaSign;
                            resultMap.put("Vital_Sign", vitArray);
                            publishProgress(resultMap);
                        }
                        // MEDICAL HISTORY ITEM INFO
                        else if (perBracket.contains("TYPE") || perBracket.contains("MONTH") || perBracket.contains("YEAR") || perBracket.contains("Reason") || perBracket.contains("Remarks")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            MedicalHistoryClass mediHistory = new MedicalHistoryClass();
                            MedicalHistoryClass[] mediHistories = {new MedicalHistoryClass()};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("TYPE"))
                                    mediHistory.setType(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("MONTH"))
                                    mediHistory.setMonth(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("YEAR"))
                                    mediHistory.setYear(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("Reason"))
                                    mediHistory.setReason(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("Remarks"))
                                    mediHistory.setRemarks(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                            }
                            mediHistories[0] = mediHistory;
                            resultMap.put("Medical_history", mediHistories);
                            publishProgress(resultMap);
                        }
                        // ALLERGY INFO ITEM INFO
                        else if (perBracket.contains("Allergy") || perBracket.contains("Status") || perBracket.contains("Symptoms") || perBracket.contains("Management")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            AllergyClass allergy = new AllergyClass();
                            AllergyClass[] allergies = {new AllergyClass()};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("Allergy"))
                                    allergy.setAllergyName(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("Status"))
                                    if (perSemicolon.substring(perSemicolon.indexOf("=") + 1).startsWith("D"))
                                        allergy.setIsDesensitized(true);
                                    else
                                        allergy.setIsDesensitized(false);
                                else if (perSemicolon.contains("Symptoms"))
                                    allergy.setSymptoms(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("Management"))
                                    allergy.setManagement(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                            }
                            allergies[0] = allergy;
                            resultMap.put("Allergy", allergies);
                            publishProgress(resultMap);
                        }
                        // OB-GYNE
                        else if (perBracket.contains("Menstrual") || perBracket.contains("Obstetrical") || perBracket.contains("Gynecological")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            String[] obgyneInfo = {"", "", ""};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("Menstrual"))
                                    obgyneInfo[0] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Obstetrical"))
                                    obgyneInfo[1] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Gynecological"))
                                    obgyneInfo[2] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                            }
                            resultMap.put("Ob_Gyne", obgyneInfo);
                            publishProgress(resultMap);
                        }
                        // SOCIAL
                        else if (perBracket.contains("Employment") || perBracket.contains("Tobacco") || perBracket.contains("Alcohol") || perBracket.contains("Sexual") || perBracket.contains("Travel") || perBracket.contains("OtherSubs")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            String[] socialInfo = {"", "", "", "", "", ""};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("Employment"))
                                    socialInfo[0] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Tobacco"))
                                    socialInfo[1] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Alcohol"))
                                    socialInfo[2] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Sexual"))
                                    socialInfo[3] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("Travel"))
                                    socialInfo[4] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                else if (perSemicolon.contains("OtherSubs"))
                                    socialInfo[5] = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                            }
                            resultMap.put("Social", socialInfo);
                            publishProgress(resultMap);
                        }
                        // UPLOADED FILES
                        else if (perBracket.contains("FileName") || perBracket.contains("AttachDate")) {
                            resultMap = new HashMap<>();
                            String[] semicolon = perBracket.split(";");
                            UploadedFileClass uploaded = new UploadedFileClass();
                            UploadedFileClass[] uploadeds = {new UploadedFileClass()};
                            for (String perSemicolon : semicolon) {
                                if (perSemicolon.contains("FileName"))
                                    uploaded.setFileName(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                else if (perSemicolon.contains("AttachDate")) {
                                    try {
                                        String dateTime = perSemicolon.substring(perSemicolon.indexOf("=") + 1);
                                        String dateOnly = dateTime.substring(0, dateTime.indexOf("T"));
                                        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateOnly);
                                        uploaded.setUploadDate(new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(date));
                                    } catch (Exception e) {
                                        uploaded.setUploadDate(perSemicolon.substring(perSemicolon.indexOf("=") + 1));
                                    }
                                }
                                if(ActivityCompat.checkSelfPermission(MedicalRecord.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MedicalRecord.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED ) {
                                    File dontiaDirectory = new File(GlobalVariables.MAXIFLEX_FOLDER);
                                    File[] uploadedFiles = dontiaDirectory.listFiles();
                                    for (int f = 0; f < uploadedFiles.length; f++) {
//                                        if (uploadedFiles[f].getName().contains(GlobalVariables.MEMBER_ID+"_"+uploaded.getFileName().trim())) {
//                                            uploaded.setIsSaved(true);
//                                        }
                                        if (uploadedFiles[f].getName().contains(uploaded.getFileName().trim())) {
                                            uploaded.setIsSaved(true);
                                        }
                                    }
                                }
                            }
                            uploadeds[0] = uploaded;
                            resultMap.put("Uploaded_file", uploadeds);
                            publishProgress(resultMap);
                        }
                    }
                } else {
                    result = GlobalVariables.ERROR_MESSAGE;
                }
            }catch (Exception e){
                result = "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(HashMap<String, Object[]>... values) {
            for (HashMap<String, Object[]> val: values) {
                // PATIENT PROFILE
                if(val.containsKey("Patient_Profile")){
                    String[] patientInfos = (String[]) val.get("Patient_Profile");
                    ((Fragment_PatientProfile)fragments[PATIENT_PROFILE]).putInfo(patientInfos[0], patientInfos[1], patientInfos[2]);
                    if(GlobalVariables.GENDER.equalsIgnoreCase("Male"))
                        hideObGyneOption();
                }
                // VITAL SIGN
                else if(val.containsKey("Vital_Sign")){
                    VitalSignClass[] vital_signs = (VitalSignClass[]) val.get("Vital_Sign");
                    ((Fragment_VitalSign)fragments[VITAL_SIGN]).putInfo(vital_signs[0]);
                }
                //MEDICAL HISTORY
                else if(val.containsKey("Medical_history")){
                    MedicalHistoryClass[] meds = (MedicalHistoryClass[]) val.get("Medical_history");
                    ((Fragment_PastMedical)fragments[PAST_MEDICAL]).putInfo(meds[0]);
                }
                else if(val.containsKey("Allergy")){

                    AllergyClass[] allerta = (AllergyClass[]) val.get("Allergy");
                    ((Fragment_Allergy)fragments[ALLERGY]).putInfo(allerta[0]);
                }
                // OB-GYNE
                else if(val.containsKey("Ob_Gyne")){
                    String[] obgyneInfos = (String[]) val.get("Ob_Gyne");
                    ((Fragment_ObGyne)fragments[OB_GYN]).putInfo(obgyneInfos[0], obgyneInfos[1], obgyneInfos[2]);
                }
                // SOCIAL
                else if(val.containsKey("Social")){
                    String[] socialInfos = (String[]) val.get("Social");
                    ((Fragment_Social)fragments[SOCIAL]).putInfo(socialInfos[0], socialInfos[1], socialInfos[2],socialInfos[3], socialInfos[4], socialInfos[5]);
                }
                // UPLOADED FILES
                else if(val.containsKey("Uploaded_file")){
                    UploadedFileClass[] uploads = (UploadedFileClass[]) val.get("Uploaded_file");
                    ((Fragment_UploadFiles)fragments[UPLOAD_FILE]).putInfo(uploads[0]);
                }
            }

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            lvDownloadProgress.setVisibility(View.GONE);
            GlobalFunctions.unlockOrientation(MedicalRecord.this);
            if(result.contains("Error:")||result.contains("error")||result.contains("Exception:"))
//                new GlobalFunctions().showAlertMessage(MedicalRecord.this, result);
                new GlobalFunctions().showAlertMessage(MedicalRecord.this, getResources().getString(R.string.error_occurred));
            isMenuRefreshClicked = false;
            super.onPostExecute(result);
        }
    }


}
