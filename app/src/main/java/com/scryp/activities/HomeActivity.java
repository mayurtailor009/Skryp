package com.scryp.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.scryp.R;
import com.scryp.dto.FilterDTO;
import com.scryp.fragments.HomeFragment;
import com.scryp.utility.Utils;

public class HomeActivity extends BaseActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private DrawerLayout drawerLayout;
    private LinearLayout sideDrawer;
    private HomeFragment homeFragment;
    private String order="1",sortVar="post_date";
    private double latitude, longitude;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;


    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 120 sec
    private static int FATEST_INTERVAL = 5000; // 10 sec
    private static int DISPLACEMENT = 50; // 50 meters

    private ImageView ivHeader, ivSearch, ivClose;
    private EditText etSearch;

    ProgressDialog pdialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }

    private void init(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (checkPlayServices()) {
                pdialog = new ProgressDialog(this);
                pdialog.setMessage("Getting Location...");
                pdialog.setCancelable(false);
                pdialog.show();
                // Building the GoogleApi client
                buildGoogleApiClient();
            }
        }else{
            Toast.makeText(HomeActivity.this, "Couldn't get the location. Make sure location is enabled on the device.", Toast.LENGTH_LONG).show();
            setTabs();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setLeft(R.drawable.menu_bar);
        setRight();
        setClick(R.id.iv_close);
        sideDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        ivHeader = (ImageView) findViewById(R.id.iv_header);
        ivSearch = (ImageView) findViewById(R.id.iv_right);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        etSearch = (EditText) findViewById(R.id.et_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (homeFragment != null && homeFragment.fragmentList.size() > 0)
                    homeFragment.fragmentList.get(homeFragment.viewPager.getCurrentItem())
                            .doSearch(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    Utils.hideKeyboard(HomeActivity.this);
                }
                return false;
            }
        });

        //setFooterClick();
        setClick(R.id.tv_coupons);
        setClick(R.id.tv_profile);
        setTouchNClick(R.id.et_min_discount);
        setTouchNClick(R.id.et_max_discount);
        setTouchNClick(R.id.et_min_price);
        setTouchNClick(R.id.et_max_price);
        setTouchNClick(R.id.et_proximity);
        setTouchNClick(R.id.btn_search);
        setTouchNClick(R.id.btn_cancel);

        setClick(R.id.cb_latest);
        setClick(R.id.cb_popularity);
        setClick(R.id.cb_price);
        setClick(R.id.cb_discount);
        setClick(R.id.cb_asc);
        setClick(R.id.cb_proximity);
        setClick(R.id.cb_desc);
        setViewSelected(R.id.cb_asc, true);
        setViewSelected(R.id.cb_latest, true);
        setHomeSelected();
    }

    public void setTabs(){
        homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitude+"");
        bundle.putString("longitude", longitude + "");
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, homeFragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_left:
                handleMenuClick();
                break;
            case R.id.et_min_discount:
                showDiscountDialog(R.id.et_min_discount, "Choose Min Discount");
                break;
            case R.id.et_max_discount:
                showDiscountDialog(R.id.et_max_discount,"Choose Max Discount");
                break;
            case R.id.et_min_price:
                showPriceDialog(R.id.et_min_price, "Choose Min Price");
                break;
            case R.id.et_max_price:
                showPriceDialog(R.id.et_max_price,"Choose Max Price");
                break;
            case R.id.et_proximity:
                showProximityDialog();
                break;
            case R.id.btn_search:
                doSearch(false);
                handleMenuClick();
                break;
            case R.id.btn_cancel:
                clearFilter();
                //doSearch(true);
                //handleMenuClick();
                break;
            case R.id.cb_asc:
                order="1";
                setViewSelected(R.id.cb_asc, true);
                setViewSelected(R.id.cb_desc, false);
                break;
            case R.id.cb_desc:
                order="0";
                setViewSelected(R.id.cb_asc, false);
                setViewSelected(R.id.cb_desc, true);
                break;
            case R.id.cb_latest:
                sortVar="post_date";
                setViewSelected(R.id.cb_latest, true);
                setViewSelected(R.id.cb_popularity, false);
                setViewSelected(R.id.cb_price, false);
                setViewSelected(R.id.cb_discount, false);
                setViewSelected(R.id.cb_proximity, false);
                break;
            case R.id.cb_popularity:
                sortVar="download";
                setViewSelected(R.id.cb_latest, false);
                setViewSelected(R.id.cb_popularity, true);
                setViewSelected(R.id.cb_price, false);
                setViewSelected(R.id.cb_discount, false);
                setViewSelected(R.id.cb_proximity, false);
                break;
            case R.id.cb_price:
                sortVar="price";
                setViewSelected(R.id.cb_latest, false);
                setViewSelected(R.id.cb_popularity, false);
                setViewSelected(R.id.cb_price, true);
                setViewSelected(R.id.cb_discount, false);
                setViewSelected(R.id.cb_proximity, false);
                break;
            case R.id.cb_discount:
                sortVar="discount";
                setViewSelected(R.id.cb_latest, false);
                setViewSelected(R.id.cb_popularity, false);
                setViewSelected(R.id.cb_price, false);
                setViewSelected(R.id.cb_discount, true);
                setViewSelected(R.id.cb_proximity, false);
                break;
            case R.id.cb_proximity:
                sortVar="dist_min";
                setViewSelected(R.id.cb_latest, false);
                setViewSelected(R.id.cb_popularity, false);
                setViewSelected(R.id.cb_price, false);
                setViewSelected(R.id.cb_discount, false);
                setViewSelected(R.id.cb_proximity, true);
                break;
            case R.id.iv_right:
                ivClose.setVisibility(View.VISIBLE);
                ivHeader.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.GONE);
                break;
            case R.id.iv_close:
                Utils.hideKeyboard(this);
                etSearch.setText("");
                ivClose.setVisibility(View.GONE);
                ivHeader.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.GONE);
                ivSearch.setVisibility(View.VISIBLE);
                break;
        }
        super.onClick(arg0);
    }

    public void doSearch(boolean isClear) {
        if (homeFragment != null && homeFragment.fragmentList.size() > 0) {
            homeFragment.viewPager.setCurrentItem(0, true);
            FilterDTO filter = new FilterDTO();
            filter.setFilterRadius("filterRadius");
            filter.setLat(latitude + "");
            filter.setLng(longitude + "");
            filter.setDistance(getViewText(R.id.et_proximity));

            filter.setFilterPrice("price");
            filter.setPriceMin(getViewText(R.id.et_min_price));
            filter.setPriceMax(getViewText(R.id.et_max_price));

            filter.setFilterDiscount("discount");
            filter.setDiscountMin(getViewText(R.id.et_min_discount));
            filter.setDiscountMax(getViewText(R.id.et_max_discount));

            filter.setSortvar(sortVar);
            filter.setAsc(order);
            if(isClear)
                filter = null;
            homeFragment.fragmentList.get(0).refreshList(filter);
        }
    }

    public void clearFilter(){
        setViewText(R.id.et_max_discount,"");
        setViewText(R.id.et_min_discount,"");
        setViewText(R.id.et_max_price,"");
        setViewText(R.id.et_min_price,"");
        setViewText(R.id.et_proximity, "");
    }
    public void handleMenuClick() {
        if(drawerLayout.isDrawerOpen(sideDrawer))
            drawerLayout.closeDrawer(sideDrawer);
        else
            drawerLayout.openDrawer(sideDrawer);
    }

    public void showDiscountDialog(final int id, String title){
        final CharSequence[] items = {
                "None","10", "20", "30","40","50","60","70","80","90"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(item!=0)
                setViewText(id, items[item].toString());
                else
                    setViewText(id, "");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showPriceDialog(final int id, String title){
        final CharSequence[] items = {
                "None","0", "5", "10","20","50","100","200","500","1000"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(item!=0)
                setViewText(id, items[item].toString());
                else
                    setViewText(id, "");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showProximityDialog(){
        final CharSequence[] items = {
                "None","0.1", "0.5", "1.0","2.0","5.0","10.0"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Proximity");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(item!=0)
                setViewText(R.id.et_proximity, items[item].toString());
                else
                    setViewText(R.id.et_proximity, "");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if(mGoogleApiClient.isConnected())
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        /*Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();*/

        // Displaying the new location on UI
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        //mGoogleApiClient.connect();
        pdialog.dismiss();
        setTabs();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        pdialog.dismiss();
        setTabs();
    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            pdialog.dismiss();
            setTabs();
            stopLocationUpdates();
        } /*else {
            Toast.makeText(HomeActivity.this, "Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG).show();
            pdialog.dismiss();
            setTabs();
        }*/
    }



    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}
