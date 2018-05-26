package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.dtechsystem.carefer.Google.DirectionsJSONParser;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.SplashActivity;
import co.dtechsystem.carefer.Utils.Utils;

public class NavigationsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private DrawerLayout mDrawerLayout;
    private GoogleMap mMap;
    private final ArrayList markerPoints = new ArrayList();
    private boolean firstCAll = false;
    private SupportMapFragment mapFragment;
    private LatLng mShopLatlng, mUserLatlng;
    private String CityId;
    private String ShopsListDataResponse;
    private String citiesNamesIDsResponse;
    private String isLocationAvail;
    private String mplaceName;
    private String mPermissionsNowGiven="";
    private TextView tv_title_navigation;
    private LatLng mLatlngCurrent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigations);
        tv_title_navigation = (TextView) findViewById(R.id.tv_title_navigation);
        SetShaderToViews();
        SetUpLeftbar();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GetDataForViews();

    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_navigation, activity);
    }

    // Get Views Data
    private void GetDataForViews() {
        if (intent != null) {
            String mlatitude = intent.getStringExtra("latitude");
            String mlongitude = intent.getStringExtra("longitude");
            CityId = intent.getStringExtra("CityId");
            ShopsListDataResponse = intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = intent.getStringExtra("citiesNamesIDsResponse");
            isLocationAvail = intent.getStringExtra("isLocationAvail");
            Bundle bundle = intent.getParcelableExtra("bundle");
            mPermissionsNowGiven = intent.getStringExtra("mPermissionsNowGiven");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = intent.getStringExtra("placeName");
            if (mlatitude != null && mlongitude != null) {

                mShopLatlng = new LatLng(Double.parseDouble(mlatitude), Double.parseDouble(mlongitude));
            }
        }
    }

    public void GotoShopsList(View v) {
        if (mPermissionsNowGiven != null && mPermissionsNowGiven.equals("true")) {
            intent= new Intent(activity, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();
        } else {
            intent = new Intent(activity, ShopsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("callType", "Navigation");
            if (CityId != null && !CityId.equals("")) {
                intent.putExtra("CityId", CityId);
                intent.putExtra("ShopsListDataResponse", ShopsListDataResponse);
                intent.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                intent.putExtra("isLocationAvail", isLocationAvail);
                Bundle args = new Bundle();
                args.putParcelable("LatLngCurrent", mLatlngCurrent);
                intent.putExtra("placeName", mplaceName);
                intent.putExtra("bundle", args);
            }
            startActivity(intent);
            finish();
        }
    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void directionsToShop(View v) {
        try {
            String UserLatlng = mUserLatlng.latitude + "," + mUserLatlng.longitude;
            String ShopLatlng = mShopLatlng.latitude + "," + mShopLatlng.longitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + UserLatlng + "&daddr=" + ShopLatlng));
            startActivity(intent);
        } catch (Exception e) {
            showToast(getResources().getString(R.string.toast_location_not_found));
            e.printStackTrace();
        }
    }

    public void openGoogleMaps(View v) {
        try {
            String uri = "http://maps.google.com/";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } catch (Exception e) {
            showToast(getResources().getString(R.string.toast_location_not_found));
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @SuppressWarnings("PointlessBooleanExpression")
            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub
                if (firstCAll != true) {
                    mUserLatlng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 13));
                    firstCAll = true;
                    AddMarkerForRoute(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                    AddMarkerForRoute(mShopLatlng);
                }
            }
        });
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                AddMarkerForRoute(latLng);
//
//            }
//        });

    }

    private void AddMarkerForRoute(final LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);
//        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_order);
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }

        // Adding new item to the ArrayList
        //noinspection unchecked
        markerPoints.add(latLng);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(latLng);

        //noinspection StatementWithEmptyBody
        if (markerPoints.size() == 1) {
//            options.icon(icon2);
        } else if (markerPoints.size() == 2) {
            options.icon(icon);
        }

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            LatLng origin = (LatLng) markerPoints.get(0);
            LatLng dest = (LatLng) markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, 13));

        }

    }
//DrawPolyLines Function

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points;
            PolylineOptions lineOptions = null;
            try {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        //noinspection unchecked
                        points.add(position);
                    }

                    //noinspection unchecked
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(getResources().getColor(R.color.colorOrange));
                    lineOptions.geodesic(true);

                }

// Drawing polyline in the Google Map for the i-th route

                if (lineOptions != null) {

                    mMap.addPolyline(lineOptions);
                } else {
                    showToast("No specific route found.");
                }
            } catch (Exception c) {
                c.printStackTrace();
            }

        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service


        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    /**
     * A method to download json data from url
     */
    @SuppressWarnings({"StringBufferMayBeStringBuilder", "ConstantConditions"})
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        //noinspection TryFinallyCanBeTryWithResources
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            //noinspection ConstantConditions
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                mapFragment.getMapAsync(this);
            } else {
                // User refused to grant permission. You can add AlertDialog here
                showToast("You didn't give permission to access device location");
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    public void btn_drawyerMenuOpen(View v) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (mPermissionsNowGiven != null && mPermissionsNowGiven.equals("true")) {
                intent= new Intent(activity, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            } else {
                intent = new Intent(activity, ShopsListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("callType", "Navigation");
                if (CityId != null && !CityId.equals("")) {
                    intent.putExtra("CityId", CityId);
                    intent.putExtra("ShopsListDataResponse", ShopsListDataResponse);
                    intent.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                    intent.putExtra("isLocationAvail", isLocationAvail);
                    Bundle args = new Bundle();
                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
                    intent.putExtra("placeName", mplaceName);
                    intent.putExtra("bundle", args);
                }
                startActivity(intent);
                finish();
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings({"StatementWithEmptyBody", "NullableProblems"})
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_details) {
            Intent i = new Intent(this, MyDetailsActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_my_orders) {
            Intent i = new Intent(this, MyOrdersActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_fav_shops) {
            Intent i = new Intent(this, FavouriteShopsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this, ShareActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(this, ContactUsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
