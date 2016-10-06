package ph.com.medilink.maxiflexmobileapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.Fragments.Fragment_ChangePassword;
import ph.com.medilink.maxiflexmobileapp.Fragments.Fragment_ContactInfo;
import ph.com.medilink.maxiflexmobileapp.Fragments.Fragment_Help;
import ph.com.medilink.maxiflexmobileapp.Fragments.Fragment_LetterOfAuthorization;
import ph.com.medilink.maxiflexmobileapp.Fragments.Fragment_eCard;
import ph.com.medilink.maxiflexmobileapp.R;
import ph.com.medilink.maxiflexmobileapp.Utilities.NetworkChangeStateReceiver;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_eCard.OnFragmentInteractionListener, Fragment_LetterOfAuthorization.OnFragmentInteractionListener,
        Fragment_ChangePassword.OnFragmentInteractionListener, Fragment_ContactInfo.OnFragmentInteractionListener, Fragment_Help.OnFragmentInteractionListener
        ,ActivityCompat.OnRequestPermissionsResultCallback {

    private FragmentManager _fragManager;
    private FragmentTransaction _fragTransaction;
    private Toolbar _actionBar;
    private DrawerLayout mDrawer;
    private NavigationView _navView;
    //UPDATE FRAGMENT_COUNT IF U WANT TO ADD/DELETE A FRAGMENT; ADD STATIC INT ALSO TO THE NEW FRAGMENT
    private static final int E_CARD = 0;
    private static final int LETTER_OF_AUTHORIZATION = 1;
    private static final int CHANGE_PASSWORD = 2;
    private static final int CONTACT_INFO = 3;
    private static final int HELP = 4;
    private static final int FRAGMENT_COUNT = 5;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private int currentFragmentPosition = 0;
    private Fragment_LetterOfAuthorization fragment_letterOfAuthorization;

    private IntentFilter networkChangeFilter;
    public static final String NETWORK_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private NetworkChangeStateReceiver networkChangeReceiver;

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get default directory for saving downloaded files
        new GlobalFunctions().getAppFileDirectory(this);
        _actionBar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(_actionBar);
        // checks if drawer layout exists on activity_main.xml because, activity_main.xml for phone has drawer layout while landscape tablet have none
        if (findViewById(R.id.drawer_layout) != null) {
            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            //drawer listener, on slide and on open
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
                    fragment_letterOfAuthorization.hideSearchDrawer();
                    super.onDrawerOpened(drawerView);
                }
            };
            mDrawer.addDrawerListener(toggle);
            toggle.syncState();
        }
        _navView = (NavigationView) findViewById(R.id.nav_view);
        View header = _navView.getHeaderView(0);
        TextView tvMemberNameNavView = (TextView) header.findViewById(R.id.tvMemberNameNavView);
        TextView tvMemberUserIDNavView = (TextView) header.findViewById(R.id.tvMemberUserIDNavView);
        tvMemberNameNavView.setText(GlobalVariables.MEMBER_NAME);
        tvMemberUserIDNavView.setText(GlobalVariables.USER_ID);

        //check permission on runtime, required on android marshmallow
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
        }
        //selects an item on navigation view
        _navView.setNavigationItemSelectedListener(this);
        //highlight the selected item of navigation view
        _navView.getMenu().getItem(0).setChecked(true);
        //instantiate fragments
        _fragManager = getSupportFragmentManager();
        fragments[E_CARD] = _fragManager.findFragmentById(R.id.ecardFragment);
        fragments[LETTER_OF_AUTHORIZATION] = _fragManager.findFragmentById(R.id.logFragment);
        fragments[CHANGE_PASSWORD] = _fragManager.findFragmentById(R.id.cpFragment);
        fragments[CONTACT_INFO] = _fragManager.findFragmentById(R.id.ciFragment);
        fragments[HELP] = _fragManager.findFragmentById(R.id.hFragment);
        _fragTransaction = _fragManager.beginTransaction();
        //hide all fragments
        for (Fragment frag : fragments) {
            _fragTransaction.hide(frag);
        }
        _fragTransaction.commit();
        //for screen orientation change
        if (savedInstanceState != null) {
            showFragment(savedInstanceState.getInt("currentFragment", E_CARD), false);
        }
        //show first fragment if first time open
        else {
            showFragment(E_CARD, false);
        }

        fragment_letterOfAuthorization = (Fragment_LetterOfAuthorization) fragments[LETTER_OF_AUTHORIZATION];
        // WiFi or mobile data disconnection detector
        networkChangeFilter = new IntentFilter(NETWORK_CHANGE_ACTION);
        networkChangeReceiver = new NetworkChangeStateReceiver();

    }

    //interval between two back button press
    //implements twice back button press to exit, interval is 2 seconds between
    private static final int TIME_INTERVAL = 2000;
    private long timeBackPressed;
    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (fragment_letterOfAuthorization.isSearchDrawerOpened()) {
            fragment_letterOfAuthorization.hideSearchDrawer();
        } else {
            if (timeBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, getResources().getString(R.string.back_button), Toast.LENGTH_SHORT).show();
            }
            timeBackPressed = System.currentTimeMillis();
        }
    }

    //inflates menu, appears on right corner of action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        switch (currentFragmentPosition) {
            case LETTER_OF_AUTHORIZATION:
                //if fragment is on LOA, inflate this menu
                getMenuInflater().inflate(R.menu.menu_letter_guarantee, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.main, menu);
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
        // signs out
        if (id == R.id.action_signOut) {
            this.SignOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** navigation item clicks are handled here **/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ecard) {
            showFragment(E_CARD, false);
        } else if (id == R.id.nav_log) {
            showFragment(LETTER_OF_AUTHORIZATION, false);
        } else if (id == R.id.nav_medical_records) {
            if(new GlobalFunctions().checkInternetState(this)) {
                Intent medRecIntent = new Intent(MainActivity.this, MedicalRecord.class);
                startActivity(medRecIntent);
            } else {
                new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.enable_internet));
            }
        } else if (id == R.id.nav_signout) {
            this.SignOut();
        }
        if (mDrawer != null)
            mDrawer.closeDrawer(GravityCompat.START);
        fragment_letterOfAuthorization.hideSearchDrawer();
        return true;
    }


    /**
     * Shows fragment on top of other fragments
     * @param fragmentIndex fragment to be shown
     * @param addToBackStack if true, adds fragment to backstack,
     */
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
            case E_CARD:
                currentFragmentPosition = E_CARD;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(R.string.menu_ecard);
                break;
            case LETTER_OF_AUTHORIZATION:
                currentFragmentPosition = LETTER_OF_AUTHORIZATION;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(R.string.action_log);
                break;
            case CHANGE_PASSWORD:
                currentFragmentPosition = CHANGE_PASSWORD;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle("Change Password");
                break;
            case CONTACT_INFO:
                currentFragmentPosition = CONTACT_INFO;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle("Contact Information");
                break;
            case HELP:
                currentFragmentPosition = HELP;
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle("Help");
                break;
            default:
        }
        transaction.commit();
    }

    /** app will remember which fragment is on top upon orientation change **/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentFragment", currentFragmentPosition);
        super.onSaveInstanceState(outState);
    }


    public void SignOut() {
        DialogInterface.OnClickListener yesButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentFragmentPosition = E_CARD;
                //start login page then finish this activity
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                MainActivity.this.finish();
            }
        };
        DialogInterface.OnClickListener noButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.sign_out), getResources().getString(R.string.yes), yesButton, getResources().getString(R.string.no), noButton);
    }

    @Override
    protected void onResume() {
        //register network change reciever to detect if internet is disconnected
        this.registerReceiver(networkChangeReceiver, networkChangeFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        //unregister network change reciever to disable disconnection detection
        this.unregisterReceiver(networkChangeReceiver);
        super.onPause();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GlobalVariables.REQUEST_EXTERNAL_READ_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        DialogInterface.OnClickListener nega = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };
                        DialogInterface.OnClickListener posi = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalVariables.REQUEST_EXTERNAL_READ_WRITE);
                            }
                        };
                        //show explanation
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
}
