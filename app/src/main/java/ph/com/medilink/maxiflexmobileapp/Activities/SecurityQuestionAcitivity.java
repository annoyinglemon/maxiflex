package ph.com.medilink.maxiflexmobileapp.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.WebServices.WebServiceClass;

public class SecurityQuestionAcitivity extends AppCompatActivity {

    private TextView etQ01, etQ02, etQ03, etQ04, etQ05, etQ06, etQ07, etQ08, etQ09, etQ10;
    private Toolbar tbSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        tbSecurity = (Toolbar) findViewById(R.id.tbSecurity);
        tbSecurity.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        setSupportActionBar(tbSecurity);

        etQ01 = (TextView) findViewById(R.id.etQ01);
        etQ02 = (TextView) findViewById(R.id.etQ02);
        etQ03 = (TextView) findViewById(R.id.etQ03);
        etQ04 = (TextView) findViewById(R.id.etQ04);
        etQ05 = (TextView) findViewById(R.id.etQ05);
        etQ06 = (TextView) findViewById(R.id.etQ06);
        etQ07 = (TextView) findViewById(R.id.etQ07);
        etQ08 = (TextView) findViewById(R.id.etQ08);
        etQ09 = (TextView) findViewById(R.id.etQ09);
        etQ10 = (TextView) findViewById(R.id.etQ10);


        getSupportActionBar().setTitle("Security Question");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) setAppbarlayoutElevation(6);

        final String blockCharacterSet = "-|";

        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };

        etQ01.setFilters(new InputFilter[] { filter });
        etQ02.setFilters(new InputFilter[] { filter });
        etQ03.setFilters(new InputFilter[] { filter });
        etQ04.setFilters(new InputFilter[] { filter });
        etQ05.setFilters(new InputFilter[] { filter });
        etQ06.setFilters(new InputFilter[] { filter });
        etQ07.setFilters(new InputFilter[] { filter });
        etQ08.setFilters(new InputFilter[] { filter });
        etQ09.setFilters(new InputFilter[] { filter });
        etQ10.setFilters(new InputFilter[] { filter });

        if(savedInstanceState!=null){
            etQ01.setText(savedInstanceState.getString(etQ01.getResources().getResourceName(etQ01.getId())));
            etQ02.setText(savedInstanceState.getString(etQ02.getResources().getResourceName(etQ02.getId())));
            etQ03.setText(savedInstanceState.getString(etQ03.getResources().getResourceName(etQ03.getId())));
            etQ04.setText(savedInstanceState.getString(etQ04.getResources().getResourceName(etQ04.getId())));
            etQ05.setText(savedInstanceState.getString(etQ05.getResources().getResourceName(etQ05.getId())));
            etQ06.setText(savedInstanceState.getString(etQ06.getResources().getResourceName(etQ06.getId())));
            etQ07.setText(savedInstanceState.getString(etQ07.getResources().getResourceName(etQ07.getId())));
            etQ08.setText(savedInstanceState.getString(etQ08.getResources().getResourceName(etQ08.getId())));
            etQ09.setText(savedInstanceState.getString(etQ09.getResources().getResourceName(etQ09.getId())));
            etQ10.setText(savedInstanceState.getString(etQ10.getResources().getResourceName(etQ10.getId())));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(etQ01.getResources().getResourceName(etQ01.getId()), etQ01.getText().toString());
        outState.putString(etQ02.getResources().getResourceName(etQ02.getId()), etQ02.getText().toString());
        outState.putString(etQ03.getResources().getResourceName(etQ03.getId()), etQ03.getText().toString());
        outState.putString(etQ04.getResources().getResourceName(etQ04.getId()), etQ04.getText().toString());
        outState.putString(etQ05.getResources().getResourceName(etQ05.getId()), etQ05.getText().toString());
        outState.putString(etQ06.getResources().getResourceName(etQ06.getId()), etQ06.getText().toString());
        outState.putString(etQ07.getResources().getResourceName(etQ07.getId()), etQ07.getText().toString());
        outState.putString(etQ08.getResources().getResourceName(etQ08.getId()), etQ08.getText().toString());
        outState.putString(etQ09.getResources().getResourceName(etQ09.getId()), etQ09.getText().toString());
        outState.putString(etQ10.getResources().getResourceName(etQ10.getId()), etQ10.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_security_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_skip:
                Intent mainIntent = new Intent(SecurityQuestionAcitivity.this, MainActivity.class);
                startActivity(mainIntent);
                SecurityQuestionAcitivity.this.finish();
                break;
            case R.id.action_submit:
                if (new GlobalFunctions().checkInternetState(SecurityQuestionAcitivity.this)) {
                    if (checkFieldsIfEmpty()) {
//                        String answers =  assembleAnswers();
//                        new GlobalFunctions().showAlertMessage(this, answers);
                        new SetSecurityQuestionAsynctask().execute(assembleAnswers());
                    } else {
                        new GlobalFunctions().showAlertMessage(this, "Please answer at least two(2) security questions.");
                    }
                }else {
                    new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, getResources().getString(R.string.enable_internet));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setAppbarlayoutElevation(float value) {
        getSupportActionBar().setElevation(value);
    }


    boolean checkFieldsIfEmpty(){
        ArrayList<TextView> allTV = new ArrayList<>();
        allTV.add(etQ01);
        allTV.add(etQ02);
        allTV.add(etQ03);
        allTV.add(etQ04);
        allTV.add(etQ05);
        allTV.add(etQ06);
        allTV.add(etQ07);
        allTV.add(etQ08);
        allTV.add(etQ09);
        allTV.add(etQ10);
        int count = 0;
        for (TextView tv: allTV) {
            if(!tv.getText().toString().isEmpty()){
                count++;
            }
        }
        return count >= 2;
    }

    String assembleAnswers(){
        String answers = "";
        ArrayList<TextView> allTV = new ArrayList<>();
        allTV.add(etQ01);
        allTV.add(etQ02);
        allTV.add(etQ03);
        allTV.add(etQ04);
        allTV.add(etQ05);
        allTV.add(etQ06);
        allTV.add(etQ07);
        allTV.add(etQ08);
        allTV.add(etQ09);
        allTV.add(etQ10);
        int count = 1;
        for (TextView tv: allTV) {
            if(!tv.getText().toString().isEmpty()){
                String countString = Integer.toString(count);
                if(count<10){
                    countString = "0"+count;
                }
                if(!answers.isEmpty())
                    answers = answers.concat("|"+GlobalVariables.ID_NUMBER+"_"+countString+"_"+tv.getText().toString());
                else
                    answers = answers.concat(GlobalVariables.ID_NUMBER+"_"+countString+"_"+tv.getText().toString());
            }
            count++;
        }
        return answers;
    }


    private class SetSecurityQuestionAsynctask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new GlobalFunctions().showProgressDialog(SecurityQuestionAcitivity.this, "Please wait...");
            GlobalFunctions.lockOrientation(SecurityQuestionAcitivity.this);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new WebServiceClass().SetSecurityQuestion(SecurityQuestionAcitivity.this, params[0]);
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            GlobalFunctions.unlockOrientation(SecurityQuestionAcitivity.this);
            if (!SetSecurityQuestionAsynctask.this.isCancelled()) {
                try {
                    if (result) {
                        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mainIntent = new Intent(SecurityQuestionAcitivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                SecurityQuestionAcitivity.this.finish();
                            }
                        };
                        new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, "Your answers are submitted successfully.", listener);
                    } else if (GlobalVariables.ERROR_MESSAGE.contains("Error:") || GlobalVariables.ERROR_MESSAGE.contains("Exception:")) {
//                        new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, GlobalVariables.ERROR_MESSAGE);
                        new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, getResources().getString(R.string.error_occurred));
                    } else if (GlobalVariables.ERROR_MESSAGE.equalsIgnoreCase("Slow network")) {
                        new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, getResources().getString(R.string.slow_internet));
                    } else {
                        new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, getResources().getString(R.string.error_occurred));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, getResources().getString(R.string.error_occurred));
//                    new GlobalFunctions().showAlertMessage(SecurityQuestionAcitivity.this, "Exception:" + ex.getMessage());
                }
            }
        }
    }

}
