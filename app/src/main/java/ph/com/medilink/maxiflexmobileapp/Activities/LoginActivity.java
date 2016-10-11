package ph.com.medilink.maxiflexmobileapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.ClinicClass;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.Locator.Locator;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.WebServices.WebServiceClass;

/**
 * Created by kurt_capatan on 3/21/2016.
 */
public class LoginActivity extends AppCompatActivity {
    //    btEnroll, btLocator
    private Button btLogin, btLocator;
    private EditText etUsername, etPassword;
    private TextView tvForgotPassword, tvCardReplacement;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btLocator = (Button) findViewById(R.id.btnLocator);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvCardReplacement = (TextView) findViewById(R.id.tvCardReplacement);

        /** textview tvForgotPassword click listener **/
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check wifi/mobile data if connected
                if (new GlobalFunctions().checkInternetState(LoginActivity.this)) {
                    //create alert dialog with custom view
                    View dialogForgotPassword = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
                    final EditText section1 = (EditText) dialogForgotPassword.findViewById(R.id.section1);
                    final EditText section2 = (EditText) dialogForgotPassword.findViewById(R.id.section2);
                    final EditText section3 = (EditText) dialogForgotPassword.findViewById(R.id.section3);
                    final EditText section4 = (EditText) dialogForgotPassword.findViewById(R.id.section4);
                    AlertDialog.Builder mainbuilder = new AlertDialog.Builder(LoginActivity.this);
                    //attach view (dialogForgotPassword) to dialog
                    mainbuilder.setView(dialogForgotPassword);
                    mainbuilder.setTitle(getResources().getString(R.string.login_resetPassword));
                    mainbuilder.setMessage(getResources().getString(R.string.login_resetPassword_email));
                    mainbuilder.setCancelable(true);
                    // set positive button onClick listener
                    mainbuilder.setPositiveButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // ASYNCTASK -> send new request for password
                                    new ResetPasswordKeystone().execute(section1.getText().toString().trim() + section2.getText().toString().trim() + section3.getText().toString().trim() + section4.getText().toString().trim());
                                }
                            }
                    );
                    // set negative button onClick listener
                    mainbuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                }
                            }
                    );
                    // create alert dialog using Builder.create() then assigns to alertDialog
                    final AlertDialog alertDialog = mainbuilder.create();
                    // section1 edittext text change listener, if string length is equal to 4, change focus to section2 edittext
                    section1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (section1.getText().toString().length() == 4)
                                section2.requestFocus();
                            if (section1.getText().toString().length() == 4 && section2.getText().toString().length() == 4
                                    && section3.getText().toString().length() == 4 && section4.getText().toString().length() == 4)
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            else
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        }
                    });
                    // section2 edittext text change listener, if string length is equal to 4, change focus to section3 edittext
                    section2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (section2.getText().toString().length() == 4)
                                section3.requestFocus();
                            if (section1.getText().toString().length() == 4 && section2.getText().toString().length() == 4
                                    && section3.getText().toString().length() == 4 && section4.getText().toString().length() == 4)
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            else
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        }
                    });
                    // section3 edittext text change listener, if string length is equal to 4, change focus to section4 edittext
                    section3.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (section3.getText().toString().length() == 4)
                                section4.requestFocus();
                            if (section1.getText().toString().length() == 4 && section2.getText().toString().length() == 4
                                    && section3.getText().toString().length() == 4 && section4.getText().toString().length() == 4)
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            else
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        }
                    });
                    // section3 edittext text change listener, if string length is equal to 4 and all section edittexts have 4 string length, SEND button is enabled
                    section4.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (section1.getText().toString().length() == 4 && section2.getText().toString().length() == 4
                                    && section3.getText().toString().length() == 4 && section4.getText().toString().length() == 4)
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            else
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        }
                    });
                    //disable SEND button on alert dialog show
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        }
                    });
                    alertDialog.show();
                } else {
                    // show alert message telling user to enable wifi or mobile data
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.enable_internet));
                }
            }
        });

        /**** show card replacement alert dialog on textview tvCardReplacement click ***/
        tvCardReplacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardReplacement();
            }
        });

        /**** button btLogin click listener ***/
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: for testing purposes only, opens MainActivity immediately, uncomment if you want, up to LoginActivity.this.finish() then comment below code up to new LoginAsynctask().execute(etUsername.getText().toString(), etPassword.getText().toString()); } }
//                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//                LoginActivity.this.finish();

                //check if username and password fields are empty
                if(etUsername.getText().toString().trim().equalsIgnoreCase("")&&etPassword.getText().toString().trim().equalsIgnoreCase("")){
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, "Username and password is required");
                }
                //check if username field is empty
                else if (etUsername.getText().toString().trim().equalsIgnoreCase(""))
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.login_required_username));
                //check if password field is empty
                else if (etPassword.getText().toString().trim().equalsIgnoreCase(""))
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.login_required_password));
                // if both fields are not empty
                else {
                    //check wifi/mobile data if connected
                    if (!new GlobalFunctions().checkInternetState(LoginActivity.this)) {
                        // show alert message telling user to enable wifi or mobile data
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.enable_internet));
                    } else {
                        //execute login asynctask
                        new LoginAsynctask().execute(etUsername.getText().toString(), etPassword.getText().toString());
                    }
                }
            }
        });

        /**** button btLocator click listener ***/
        btLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check wifi/mobile data if connected
                if (!new GlobalFunctions().checkInternetState(LoginActivity.this)) {
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.enable_internet));
                } else {
                    //execute locator asynctask
                    new PrepareLocator().execute();
                }
//                finish();
            }
        });
    }


    private void CardReplacement() {
        // check if connected to wifi/mobile data
        if (new GlobalFunctions().checkInternetState(LoginActivity.this)) {
            // get view to be attached on alert dialog
            View edittextOnly = getLayoutInflater().inflate(R.layout.edittext_only, null);
            // find edittext inside tis view
            final EditText editText = (EditText) edittextOnly.findViewById(R.id.editText);
            //creation of alert dialog builder
            final AlertDialog.Builder mainbuilder = new AlertDialog.Builder(LoginActivity.this);
            // attach view to builder
            mainbuilder.setView(edittextOnly);
            mainbuilder.setTitle("Card Replacement");
            mainbuilder.setMessage("Enter your ID number");
            //back button or tapping outside of alert dialog will not close the dialog
            mainbuilder.setCancelable(false);
            // next button click listener
            mainbuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //get all answered security questions with answers of user
                            new GetSecurityQuestions().execute(editText.getText().toString());
                        }
                    }
            );
            // closes dialog then hides keyboard
            mainbuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    }
            );
            // create alertdialog using builder.create()
            final AlertDialog alertDialog = mainbuilder.create();
            // editText text change listener, dialog's positive button will disable if edittext is empty
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!editText.getText().toString().isEmpty())
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    else
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            });
            // on show listener of alert dialog, disable alert dialog positive button on show
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            });
            //show alert dialog
            alertDialog.show();
        } else {
            // tell user to enable wifi / mobile data
            new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.enable_internet));
        }
    }


    private class GetSecurityQuestions extends AsyncTask<String, String, String> {

        ArrayList<String> questions;
        ArrayList<String> correctAnswers;
        ProgressDialog progressDialog;
        String mIdNumber = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            questions = new ArrayList<>();
            correctAnswers = new ArrayList<>();
            //show progress dialog with "Please wait..." message
            progressDialog = new GlobalFunctions().showProgressDialog(LoginActivity.this, "Please wait...", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //if progress dialog is canceled using the cancel button, this asynctask is also canceled
                    GetSecurityQuestions.this.cancel(true);
                }
            });
            // lock orientation temporarily
            GlobalFunctions.lockOrientation(LoginActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            mIdNumber = params[0];
            String concatenatedString = new WebServiceClass().GetSecurityQuestions(LoginActivity.this, params[0]);
            if (!GlobalVariables.ERROR_MESSAGE.contains("Error:") && !GlobalVariables.ERROR_MESSAGE.contains("Exception:") && !GlobalVariables.ERROR_MESSAGE.equalsIgnoreCase("Slow Network") && !concatenatedString.equalsIgnoreCase("Error")) {
                if (concatenatedString.equalsIgnoreCase("Error1")) {
                    return "Error1";
                } else if (concatenatedString.equalsIgnoreCase("Error2")) {
                    return "Error2";
                } else {
                    // splits the string using "|" character, obtains string before and after this character
                    String[] questionCodeAnswers = concatenatedString.split("\\|", -1);
                    //for each question code and answer, split using "_" to get the code and answer then publishProgress
                    for (String s : questionCodeAnswers) {
                        String[] questionCodeAnswer = s.split("_", -1);
                        //check if string is correct questionCode_answer format
                        if (questionCodeAnswer.length == 2)
                            publishProgress(s);
                        else
                            return "Error";

                    }
                }
            }
            return concatenatedString;
        }

        /*** a call to publishProgress will call below method **/
        @Override
        protected void onProgressUpdate(String... values) {
            String[] questionCodeAnswer = values[0].split("_", -1);
            //add the code to questions arraylist
            questions.add(questionCodeAnswer[0]);
            //add the answer to correctAnswers arraylist
            correctAnswers.add(questionCodeAnswer[1]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //unlock the orientation
            GlobalFunctions.unlockOrientation(LoginActivity.this);
            //if GlobalVariables.ERROR_MESSAGE has "Error:" or "Exception", show the error message
            if (GlobalVariables.ERROR_MESSAGE.contains("Error:") || GlobalVariables.ERROR_MESSAGE.contains("Exception:")) {
//                new GlobalFunctions().showAlertMessage(LoginActivity.this, result);
                new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
            }
            //if internet is slow
            else if (GlobalVariables.ERROR_MESSAGE.equalsIgnoreCase("Slow Network")) {
                new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.slow_internet));
            }
            //if unknown error occurred
            else if (result.equalsIgnoreCase("Error")) {
                new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
            }
            //if id number did not exists on database
            else if (result.equalsIgnoreCase("Error1")) {
                new GlobalFunctions().showAlertMessage(LoginActivity.this, "You have entered an invalid ID number.");
            }
            //if id number of member is not set for security questions
            else if (result.equalsIgnoreCase("Error2")) {
                new GlobalFunctions().showAlertMessage(LoginActivity.this, "The setup for your security questions is not yet done. Please login to set your security questions.");
            } else {
                /** SHOW QUESTIONS TO USER, USER WILL CHOOSE ONE QUESTION THEN ANSWER IT, IF CORRECT, HIS/HER CARD NUMBER WILL BE CHANGED AND WILL BE SENT VIA EMAIL **/
                // get the custom view
                View questionDialog = getLayoutInflater().inflate(R.layout.dialog_card_replacement_questions, null);
                //initialize questions spinner
                final Spinner sprQuestions = (Spinner) questionDialog.findViewById(R.id.sprQuestions);
                //initialize edittext
                final EditText etAnswer = (EditText) questionDialog.findViewById(R.id.etAnswer);
                // attach questions to spinner
                sprQuestions.setAdapter(new CustomAdapter(questions));
                // initialize alert dialog builder
                AlertDialog.Builder mainbuilder2 = new AlertDialog.Builder(LoginActivity.this);
                //attach view to main dialog
                mainbuilder2.setView(questionDialog);
                mainbuilder2.setTitle("Card Replacement");
                mainbuilder2.setMessage("Choose a question then enter your answer on the field below.");
                mainbuilder2.setCancelable(false);
                mainbuilder2.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //onclick listener is below alertDialog.show();
                    }
                });
                //if canceled dismissed then hide keyboard
                mainbuilder2.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            }
                        }
                );
                //back to card number input
                mainbuilder2.setNeutralButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CardReplacement();
                    }
                });

                final AlertDialog alertDialog = mainbuilder2.create();
                etAnswer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!etAnswer.getText().toString().isEmpty())
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                        else
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                alertDialog.show();
                //custom onCLick listener
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean wantToCloseDialog = false;
                        if (correctAnswers.get(sprQuestions.getSelectedItemPosition()).contentEquals(etAnswer.getText())) {
                            new ChangeCardNumber().execute(mIdNumber);
                            wantToCloseDialog = true;
                        } else {
                            Toast.makeText(LoginActivity.this, "Your answer is incorrect, try again.", Toast.LENGTH_SHORT).show();
                        }
                        if (wantToCloseDialog)
                            alertDialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

            }
            super.onPostExecute(result);
        }
    }

    private class CustomAdapter extends BaseAdapter {

        ArrayList<String> questionCodes;

        CustomAdapter(ArrayList<String> questionCodes) {
            this.questionCodes = questionCodes;
        }

        @Override
        public int getCount() {
            return questionCodes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = getLayoutInflater().inflate(R.layout.textview_only, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textView);
            switch (questionCodes.get(position)) {
                case "01":
                    textView.setText(getResources().getString(R.string.question1));
                    break;
                case "02":
                    textView.setText(getResources().getString(R.string.question2));
                    break;
                case "03":
                    textView.setText(getResources().getString(R.string.question3));
                    break;
                case "04":
                    textView.setText(getResources().getString(R.string.question4));
                    break;
                case "05":
                    textView.setText(getResources().getString(R.string.question5));
                    break;
                case "06":
                    textView.setText(getResources().getString(R.string.question6));
                    break;
                case "07":
                    textView.setText(getResources().getString(R.string.question7));
                    break;
                case "08":
                    textView.setText(getResources().getString(R.string.question8));
                    break;
                case "09":
                    textView.setText(getResources().getString(R.string.question9));
                    break;
                case "10":
                    textView.setText(getResources().getString(R.string.question10));
                    break;
            }
            return rowView;
        }
    }

    /**
     * ASYNCTASKS
     **/

    private class LoginAsynctask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading dialog with cancel button, that cancels asynctask when pressed
            progressDialog = new GlobalFunctions().showProgressDialog(LoginActivity.this, getResources().getString(R.string.login_validate), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LoginAsynctask.this.cancel(true);
                }
            });
            //lock screen orientation temporarily
            GlobalFunctions.lockOrientation(LoginActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            return new WebServiceClass().ValidateCredential_eCard(LoginActivity.this, params[0], params[1]);
        }


        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
            GlobalFunctions.unlockOrientation(LoginActivity.this);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // dismiss the loading dialog
            progressDialog.dismiss();
            //unlock screen orientation
            GlobalFunctions.unlockOrientation(LoginActivity.this);
            //check if this asyncTask is not cancelled
            if (!LoginAsynctask.this.isCancelled()) {
                try {
                    //if login successful
                    if (s.equalsIgnoreCase("Welcome")) {
                        //check if user's security questions are set, if set go to MainActivity, otherwise go to SecurityQuestionAcitivity
                        if (GlobalVariables.HAVE_SECURITY_QUESTIONS.equalsIgnoreCase("Y")) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            LoginActivity.this.finish();
                        } else {
                            Intent secIntent = new Intent(LoginActivity.this, SecurityQuestionAcitivity.class);
                            startActivity(secIntent);
                            LoginActivity.this.finish();
                        }
                    }
                    //if login unsuccessful
                    else if (s.contains("Error:")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                    } else if (s.equalsIgnoreCase("Error")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                    } else if (s.equalsIgnoreCase("Slow Network")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.slow_internet));
                    } else if (s.equalsIgnoreCase("Member not yet approved activated.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, "Member not yet approved or activated.");
                    } else if (s.equalsIgnoreCase("The Clinic where you belong is already inactive.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.inactive_clinic));
                    } else if (s.equalsIgnoreCase("This application is intend for Member only.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.member_only));
                    } else if (s.equalsIgnoreCase("User is Currently logged in.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.already_logged_in));
                    } else if (s.equalsIgnoreCase("Your Account is Deactivated. Please contact your System Admin.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.deactivated));
                    } else if (s.equalsIgnoreCase("Your password has been reset and sent to your registered email address.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.password_reset_sent));
                    } else if (s.equalsIgnoreCase("Username or Password is incorrect.")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.user_pass_incorrect));
                    } else {
//                        new GlobalFunctions().showAlertMessage(LoginActivity.this, s);
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
//                    new GlobalFunctions().showAlertMessage(LoginActivity.this, "Exception:" + ex.getMessage());
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                }
            }
        }
    }

    private class ResetPasswordKeystone extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading dialog with cancel button, that cancels asynctask when pressed
            progressDialog = new GlobalFunctions().showProgressDialog(LoginActivity.this, getResources().getString(R.string.login_forgotpwd_request), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ResetPasswordKeystone.this.cancel(true);
                }
            });
            //lock screen orientation temporarily
            GlobalFunctions.lockOrientation(LoginActivity.this);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new WebServiceClass().ResetPasswordKeystone(LoginActivity.this, params[0]);
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // dismiss the loading dialog
            progressDialog.dismiss();
            //unlock screen orientation
            GlobalFunctions.unlockOrientation(LoginActivity.this);
            //if this asynctask is not cancelled
            if (!ResetPasswordKeystone.this.isCancelled()) {
                try {
                    //if successfull password reset
                    if (result) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.password_reset_success));
                    }
                    //if not successful
                    else if (GlobalVariables.ERROR_MESSAGE.contains("Error") || GlobalVariables.ERROR_MESSAGE.contains("Exception:")) {
//                        new GlobalFunctions().showAlertMessage(LoginActivity.this, GlobalVariables.ERROR_MESSAGE);
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                    } else if (GlobalVariables.ERROR_MESSAGE.equalsIgnoreCase("Slow network")) {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.slow_internet));
                    } else {
                        new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.password_reset_failed));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, "Exception:" + ex.getMessage());
                }
            }
        }
    }

    private class ChangeCardNumber extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading dialog with cancel button, that cancels asynctask when pressed
            progressDialog = new GlobalFunctions().showProgressDialog(LoginActivity.this, "Processing, please wait...");
            //lock screen orientation temporarily
            GlobalFunctions.lockOrientation(LoginActivity.this);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new WebServiceClass().ChangeCardNo(LoginActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            //dismiss loading dialog
            progressDialog.dismiss();
            //unlock orientation
            GlobalFunctions.unlockOrientation(LoginActivity.this);
            try {
                //if card number change successful
                if (result) {
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, "Card replacement successful, you will receive an email concerning your new card number.");
                }
                // if unsuccessful
                else if (GlobalVariables.ERROR_MESSAGE.contains("Error:") || GlobalVariables.ERROR_MESSAGE.contains("Exception:")) {
//                    new GlobalFunctions().showAlertMessage(LoginActivity.this, GlobalVariables.ERROR_MESSAGE);
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
                } else if (GlobalVariables.ERROR_MESSAGE.equalsIgnoreCase("Slow network")) {
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.slow_internet));
                } else {
                    new GlobalFunctions().showAlertMessage(LoginActivity.this, "An error occurred, card replacement failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
//                new GlobalFunctions().showAlertMessage(LoginActivity.this, "Exception:" + ex.getMessage());
            }

        }
    }

    private class PrepareLocator extends AsyncTask<Void, Void, ArrayList<ClinicClass>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new GlobalFunctions().showProgressDialog(LoginActivity.this, getResources().getString(R.string.login_locating_clinics), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    PrepareLocator.this.cancel(true);
                }
            });
            GlobalFunctions.lockOrientation(LoginActivity.this);
        }

        @Override
        protected ArrayList<ClinicClass> doInBackground(Void... params) {
            return new WebServiceClass().GetAllClinic_eCard();
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
            GlobalFunctions.unlockOrientation(LoginActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<ClinicClass> clinicClasses) {
            GlobalVariables.gpin = 0;
//            ArrayList<ClinicClass> clinicClasses1 = new ArrayList<>();
//            clinicClasses1.add(new ClinicClass("Makati Medical Center", "11111111", "No. 2, Amorsolo St., Legaspi Village, Makati City, Makati, 1229 Metro Manila", "14.5591556", "121.0149322"));
//            clinicClasses1.add(new ClinicClass("St. Luke's Medical Center", "22222222", "New Manila, Quezon City, Metro Manila", "14.622538", "121.023738"));
//            clinicClasses1.add(new ClinicClass("The Medical City", "3333333", "Ortigas Ave, Pasig, Metro Manila", "14.5898238", "121.0669722"));
//            Intent i = new Intent(LoginActivity.this, Locator.class);
//            i.putExtra("CLINICS", clinicClasses1);
//            startActivity(i);
//            LoginActivity.this.finish();
//            progressDialog.dismiss();
            GlobalFunctions.unlockOrientation(LoginActivity.this);
            //TODO: UNCOMMENT BELOW, clinicClasses1 is for testing purposes only
            if((clinicClasses.size()==0 && GlobalVariables.ERROR_MESSAGE.contains("Exception"))||GlobalVariables.ERROR_MESSAGE.contains("Error:")){
//                new GlobalFunctions().showAlertMessage(LoginActivity.this, GlobalVariables.ERROR_MESSAGE);
                new GlobalFunctions().showAlertMessage(LoginActivity.this, getResources().getString(R.string.error_occurred));
            }else{
                Intent i = new Intent(LoginActivity.this, Locator.class);
                i.putExtra("CLINICS", clinicClasses);
                startActivity(i);
                progressDialog.dismiss();
            }
        }
    }
}
