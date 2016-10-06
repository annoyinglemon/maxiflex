package ph.com.medilink.maxiflexmobileapp.Locator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ph.com.medilink.maxiflexmobileapp.PlainOldJavaObjects.ClinicClass;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalFunctions;
import ph.com.medilink.maxiflexmobileapp.Globals.GlobalVariables;
import ph.com.medilink.maxiflexmobileapp.Activities.LoginActivity;
import ph.com.medilink.maxiflexmobileapp.R;

public class Locator extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private RadioGroup rgViews;
    private Toolbar mActionBar;
    private MenuItem searchMenu;
    private SearchView mSearchView;
    private SupportMapFragment mapFragment;
    private LinearLayout llmapFragment;
    private RecyclerView rvClinics;
    private ClinicAdapter mAdapter;
    private DrawerLayout dlMap;
    private TextView tvDrawerClinicName;
    private TextView tvDrawerClinicAddress;
    private Button btShowRoute;

    private LatLng userLatLng, Goto, ClickedInfo;
    private CameraUpdate cameraUpdate;
    private LocationManager locationManager;


    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private final LatLng MANILA_LATLNG = new LatLng(14.598152,120.9446319); // <-- this is MANILA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        mActionBar = (Toolbar) findViewById(R.id.mapToolbar);
        mActionBar.setTitle(getResources().getString(R.string.login_locator));
        mActionBar.setTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        setSupportActionBar(mActionBar);

        /**
         * for provider list/search
         */
        rvClinics = (RecyclerView) findViewById(R.id.rvClinics);
        rvClinics.setLayoutManager(new LinearLayoutManager(this));
        rvClinics.addItemDecoration(new GlobalFunctions().new DividerItemDecoration(this));
        if(savedInstanceState==null)
            mAdapter = new ClinicAdapter((ArrayList<ClinicClass>) getIntent().getSerializableExtra("CLINICS"));
        else
            mAdapter =  new ClinicAdapter((ArrayList<ClinicClass>) savedInstanceState.getSerializable("CLINICS"));
        rvClinics.setAdapter(mAdapter);
        rvClinics.addOnItemTouchListener(new RecyclerTouchListener(this, rvClinics));
        /****/

        rgViews = (RadioGroup) findViewById(R.id.rg_views);

        llmapFragment = (LinearLayout) findViewById(R.id.llmapFragment);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        dlMap = (DrawerLayout) findViewById(R.id.dlMap);
        dlMap.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        tvDrawerClinicName = (TextView) findViewById(R.id.tvDrawerClinicName);

        tvDrawerClinicAddress = (TextView) findViewById(R.id.tvDrawerClinicAddress);

        btShowRoute = (Button) findViewById(R.id.btShowRoute);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        /**
         *  runtime permission check of android marshmallow
         *  if permission is enabled, locationManager requests for location updates
         *  otherwise, app will request permission, (a dialog will pop pop saying "MaxiFlex wants to ... " with "Accept" and "Decline" button)
         */
        if (ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, (LocationListener) this);
        }else {
            ActivityCompat.requestPermissions(Locator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GlobalVariables.REQUEST_LOCATION_FINE_COARSE);
        }

        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ClickedInfo = marker.getPosition();
                ClinicClass clinic = mAdapter.searchClinic(marker.getPosition());
                tvDrawerClinicName.setText(clinic.getClinicName());
                tvDrawerClinicAddress.setText(clinic.getAddress());
                dlMap.openDrawer(GravityCompat.END);
            }
        });

        /**
            set map type using radio buttons
         */
        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_normal) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (checkedId == R.id.rb_satellite) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (checkedId == R.id.rb_terrain) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
        });

        btShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRouteToMap();
            }
        });

        /**
         * add all provider/hospital markers
         */
        addMarkersToMap();

        try {
            /**
             * get user's location then animate camera
             */
            if (GlobalFunctions.isLocationEnabled(this)) {
                if (ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    userLatLng = new LatLng(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude());
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15f);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }catch (Settings.SettingNotFoundException e){
            Toast.makeText(this, getResources().getString(R.string.locator_features_unavailable), Toast.LENGTH_LONG).show();
        } catch (SecurityException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15f);
        mMap.animateCamera(cameraUpdate);

        if (ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

        if (GlobalVariables.gpin == 2 && Goto !=null && userLatLng != null) {
            String url = getDirectionsUrl(userLatLng, Goto);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        try {
            if (!GlobalFunctions.isLocationEnabled(this)) {
                new GlobalFunctions().showAlertMessage(this, "Please enable your device's location provider.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }
                });
                return false;
            }
        }catch (Settings.SettingNotFoundException exc){
            new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.locator_features_unable));
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_locator, menu);
        /**
         * search button on action bar
         */
        searchMenu = menu.findItem(R.id.menuSearch);
        mSearchView = (SearchView) searchMenu.getActionView();
        // Assumes current activity is the searchable activity
        mSearchView.setIconifiedByDefault(true); //  iconify the widget; expand it when clicked
        mSearchView.setQueryHint(getResources().getString(R.string.locator_search_clinic));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.searchClinic(newText);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            /**
             * on search menu close, show map
             */
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mActionBar.setBackgroundColor(ContextCompat.getColor(Locator.this, R.color.colorPrimary));
                llmapFragment.setVisibility(View.VISIBLE);
                rgViews.setVisibility(View.VISIBLE);
                rvClinics.setVisibility(View.GONE);
                mAdapter.refreshList();
                return true;
            }
            /**
             * on search menu open, show list of provider/hospitals
             */
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mActionBar.setBackgroundColor(ContextCompat.getColor(Locator.this, R.color.md_white_1000));
                llmapFragment.setVisibility(View.GONE);
                rgViews.setVisibility(View.GONE);
                rvClinics.setVisibility(View.VISIBLE);
                return true;
            }
        });
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /**
             * on refresh, map will be cleared then all provider markers will be added
             */
            case R.id.menuRefresh:
                addMarkersToMap();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * if drawer is open, close it,
     * otherwise, exit this activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dlMap.isDrawerOpen(GravityCompat.END)){
            dlMap.closeDrawer(dlMap);
        } else {
            Intent i = new Intent(Locator.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * map will be cleared then all provider markers will be added
     */
    private void addMarkersToMap() {
        mMap.clear();
        GlobalVariables.gpin = 0;
        for (ClinicClass clinic: mAdapter.getItems()) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(clinic.getLatitude()), Double.parseDouble(clinic.getLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    .title(clinic.getClinicName())
                    .snippet(clinic.getAddress());
            mMap.addMarker(markerOptions);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(MANILA_LATLNG, 9f);
        mMap.animateCamera(cameraUpdate);
    }

    /**
     * add a single provider marker in map
     * @param position provider position on Arraylist<ClinicClass>
     */
    private void addMarkerToMap(int position){
        mMap.clear();
        GlobalVariables.gpin = 1;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(Double.parseDouble(mAdapter.getItem(position).getLatitude()), Double.parseDouble(mAdapter.getItem(position).getLongitude())))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .title(mAdapter.getItem(position).getClinicName())
                .snippet(mAdapter.getItem(position).getAddress());
        mMap.addMarker(markerOptions);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mAdapter.getItem(position).getLatitude()), Double.parseDouble(mAdapter.getItem(position).getLongitude())), 17f);
        mMap.animateCamera(cameraUpdate);
    }

    /**
     * add rout to map, from user location to chosen provider
     */
    private void addRouteToMap(){
        dlMap.closeDrawer(GravityCompat.END);
        try {
            if (!GlobalFunctions.isLocationEnabled(this)) {
                new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.locator_enable_location), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }
                });
            }else{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Locator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GlobalVariables.REQUEST_LOCATION_FINE_COARSE);
                }else {
                    GlobalVariables.gpin = 2;
                    userLatLng = new LatLng(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude());
                    Goto = ClickedInfo;

                    mMap.clear();

                    String url = getDirectionsUrl(userLatLng, Goto);
//                String url = getDirectionsUrl(new LatLng(41.671595, 88.220712), new LatLng(38.443604, 113.005868));
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                }
            }
        }catch (Settings.SettingNotFoundException exc){
            new GlobalFunctions().showAlertMessage(this, getResources().getString(R.string.locator_features_unable));
        }
    }


    /**
     * Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions(Activity, String[], int).
     * Note: It is possible that the permissions request interaction with the user is interrupted. In this case you will receive empty permissions and results arrays which should be treated as a cancellation.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GlobalVariables.REQUEST_LOCATION_FINE_COARSE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Locator.this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        DialogInterface.OnClickListener nega = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };
                        DialogInterface.OnClickListener posi = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Locator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GlobalVariables.REQUEST_LOCATION_FINE_COARSE);
                            }
                        };
                        new GlobalFunctions().showAlertMessage(this, "This app needs access to your current location to give you directions to your chosen clinic/hospital. If you decline, this app will not able to generate routes and give directions. Tap 'Try again' to show the message one more time."
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
                        new GlobalFunctions().showAlertMessage(this, "You have denied the permission for accessing your current location, please proceed to Settings and allow location access.", getResources().getString(R.string.settings), settings, getResources().getString(R.string.cancel), cancel);
                    }
                }else{
                    if (ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                    } else {
                        ActivityCompat.requestPermissions(Locator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GlobalVariables.REQUEST_LOCATION_FINE_COARSE);
                    }
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //========================================================================================================================================================================
    //PolyLine Properties
    //========================================================================================================================================================================
    private String getDirectionsUrl(LatLng latLng, LatLng Goto) {

        String mSource = "origin=" + latLng.latitude + "," + latLng.longitude;
        String mDestination = "destination=" + Goto.latitude + "," + Goto.longitude;
        String mSensor = "sensor=false";
        String mParameters = mSource + "&" + mDestination + "&" + mSensor;
        String mOutput = "json";
        String murl = "https://maps.googleapis.com/maps/api/directions/" + mOutput + "?" + mParameters;
        return murl;
    }

    private String downloadUrl(String mUrl) throws IOException {
        String mdata = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(mUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String mLine = "";
            while ((mLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(mLine);
            }
            mdata = stringBuffer.toString();
            bufferedReader.close();
        } catch (Exception e) {

        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return mdata;
    }




    /**
     * RECYCLERVIEW ADAPTER AND TOUCH LISTENER
     */

    public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.CustomViewHolder> implements Serializable{

        private ArrayList<ClinicClass> mClinics, mClinicsCopy;


        public ClinicAdapter(ArrayList<ClinicClass> clinicClasses){
            this.mClinics = clinicClasses;
            this.mClinicsCopy = clinicClasses;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView tvClinicName;
            public TextView tvClinicAddress;

            public CustomViewHolder(View view) {
                super(view);
                tvClinicName = (TextView) view.findViewById(R.id.tvClinicName);
                tvClinicAddress = (TextView) view.findViewById(R.id.tvClinicAddress);
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(Locator.this).inflate(R.layout.clinic_search_item, parent, false);
            return new CustomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            ClinicClass clinic = mClinics.get(position);
            holder.tvClinicName.setText(clinic.getClinicName());
            holder.tvClinicAddress.setText(clinic.getAddress());
        }

        @Override
        public int getItemCount() {
            return mClinics.size();
        }

        public ClinicClass getItem(int position){
            return mClinics.get(position);
        }

        public ClinicClass getItem(LatLng position){
            for (ClinicClass c: mClinics) {
                if(Double.parseDouble(c.getLatitude())==position.latitude && Double.parseDouble(c.getLongitude())==position.longitude) {
                    return c;
                }
            }
            return null;
        }

        public ArrayList<ClinicClass> getItems(){
            return mClinicsCopy;
        }

        public void searchClinic(String query){
            ArrayList<ClinicClass> cc = new ArrayList<>();
            for(int i = 0; i < mClinicsCopy.size(); i++){
                if(mClinicsCopy.get(i).getClinicName().toLowerCase().contains(query)||mClinicsCopy.get(i).getAddress().toLowerCase().contains(query)){
                  cc.add(mClinicsCopy.get(i));
                }
            }
            mClinics = cc;
            if(mClinics.size()==0){
                rvClinics.setVisibility(View.GONE);
            }else {
                rvClinics.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();
        }

        public ClinicClass searchClinic(LatLng location) {
            ClinicClass c = new ClinicClass();
            String latitude = Double.toString(location.latitude);
            String longitude = Double.toString(location.longitude);
            for (ClinicClass clinic: mClinics) {
                if(clinic.getLatitude().contains(latitude)&&clinic.getLongitude().contains(longitude)){
                    c = clinic;
                    break;
                }
            }
            return c;
        }

        public void refreshList(){
            mClinics = mClinicsCopy;
            notifyDataSetChanged();
        }
    }



    /**
     * on clinic item touch, shows location of provider
     */

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetectorCompat gestureDetectorCompat;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView) {
            gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                private int itemSelected = 0;

                @Override
                public void onLongPress(MotionEvent e) {
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    itemSelected = recyclerView.getChildAdapterPosition(child);

                    if(itemSelected>-1) {
                        MenuItemCompat.collapseActionView(searchMenu);
                        addMarkerToMap(itemSelected);
                    }
                    return false;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetectorCompat.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    /**
     * ASYNCTASKS
     */

    private ProgressDialog pdRoute;

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pdRoute = new GlobalFunctions().showProgressDialog(Locator.this, getResources().getString(R.string.locator_finding_route), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    DownloadTask.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... url) {
            String mData = "";
            try {
                mData = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("Background Task", mData);
            return mData;
        }

        @Override
        protected void onCancelled() {
            pdRoute.dismiss();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdRoute.dismiss();
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, ArrayList<LatLng>> {

        @Override
        protected void onPreExecute() {
            pdRoute = new GlobalFunctions().showProgressDialog(Locator.this, getResources().getString(R.string.fragment_apply_route), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ParserTask.this.cancel(true);
                }
            });
        }

        @Override
        protected ArrayList<LatLng> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            ArrayList<LatLng> points = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();
                routes = directionsJSONParser.parse(jObject);

                Log.d(getClass().getSimpleName(), "Result: " + routes.toArray().toString() + " Result size: " + routes.size());

                for (int i = 0; i < routes.size(); i++) {
                    Log.d(getClass().getSimpleName(), "Loop: " + i);
                    points = new ArrayList<LatLng>();

                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        Log.d(getClass().getSimpleName(), "Loop inner: " + j);
                        HashMap<String, String> latLngss = path.get(j);

                        double lat = Double.parseDouble(latLngss.get("lat"));
                        double lng = Double.parseDouble(latLngss.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        Log.d(getClass().getSimpleName(), "Hash : " + latLngss.toString());
                        points.add(position);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return points;
        }

        @Override
        protected void onCancelled() {
            pdRoute.dismiss();
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> points) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .width(5)
                    .color(Color.BLUE));
            pdRoute.dismiss();
            ClinicClass clinic = mAdapter.getItem(Goto);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(Goto)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    .title(clinic.getClinicName())
                    .snippet(clinic.getAddress());
            mMap.addMarker(markerOptions);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLatLng, 15f);
            mMap.animateCamera(cameraUpdate);

        }
    }


}
