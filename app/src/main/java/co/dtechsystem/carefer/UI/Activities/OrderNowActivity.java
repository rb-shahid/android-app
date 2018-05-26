package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class OrderNowActivity extends Fragment {
    private DrawerLayout mDrawerLayout;
    private String mlatitude;
    private String mlongitude;
    private String mshopID;
    private String mServicesId = "";
    private String mBrandsId = "";
    private String mModelsId = "";
    private String morderType = "", mplaceName;
    private String mshopImage = "";
    private String mContact, CityId, ShopsListDataResponse, citiesNamesIDsResponse, isLocationAvail;
    String sUser_ID;
    String sUser_Mobile;
    private ImageView iv_shop_image_blur;
    private CircleImageView iv_shop_profile;
//    private TextView tv_title_order_now;
    private int morderID;
    private boolean mOrderPlaced;
    private LatLng mLatlngCurrent;
    private String mPermissionsNowGiven = "";
    private boolean locationSettings = false;




    private MainActivity activity;
    private View view;

    public OrderNowActivity() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_now, container, false);
        ImageView btn_call_shop = (ImageView) view.findViewById(R.id.btn_call_shop);
        ImageView btn_navigate_shop = (ImageView) view.findViewById(R.id.btn_navigate_shop);

        btn_call_shop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Validations.isInternetAvailable(activity, true)) {
//                    try {
//
//                        if (mContact != null && !mContact.equals("")) {
//                            if (mOrderPlaced == false) {
//                                //noinspection StatementWithEmptyBody
//                                if (morderType != null && morderType.equals("navigate")) {
//                                } else {
//                                    morderType = "call";
//                                }
//
//                                activity.loading.show();
//                                APiPlaceOrder(sUser_ID, mshopID, mServicesId, mBrandsId, mModelsId, morderType, sUser_Mobile);
//
//                            }
//
//                        } else {
//                            activity.showToast(getResources().getString(R.string.toast_no_shop_contact_found));
//                        }
//                    } catch (Exception e) {
//                        activity.SendFireBaseError(String.valueOf(e));
//                        e.printStackTrace();
//                    }
//                }

                Intent intent = new Intent( getActivity() , OrderSuccessActivity.class );
                startActivity( intent );
            }
        } );

        btn_navigate_shop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    dirShopsIntents();
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                        alertDialog.setTitle(getResources().getString(R.string.app_name));
                        alertDialog.setMessage(getResources().getString(R.string.dialog_need_your_permission));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            dialog.dismiss();
                                            locationSettings = true;
                                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        } catch (Exception e) {
                                            dialog.dismiss();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                }

            }
        } );



//        iv_shop_image_blur = (ImageView) view.findViewById(R.id.iv_shop_image_blur);
//        iv_shop_profile = (CircleImageView) view.findViewById(R.id.iv_shop_profile);
//        tv_title_order_now = (TextView) view.findViewById(R.id.tv_title_order_now);
        mOrderPlaced = false;
        setUpToolbar();
//        SetShaderToViews();
//        SetUpLeftbar();
        GetDataForViews();
        SetdataToViews();

        return view ;

    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
//    private void SetShaderToViews() {
//        Utils.gradientTextViewLong(tv_title_order_now, activity);
//    }

    // Get Views Data
    private void GetDataForViews() {
        if (activity.intent != null) {
            mlatitude = activity.intent.getStringExtra("latitude");
            mlongitude = activity.intent.getStringExtra("longitude");
            mshopID = activity.intent.getStringExtra("shopID");
            mServicesId = activity.intent.getStringExtra("serviceID");
            mBrandsId = activity.intent.getStringExtra("brandID");
            mModelsId = activity.intent.getStringExtra("modelID");
            mshopImage = activity.intent.getStringExtra("shopImage");
            mContact = activity.intent.getStringExtra("contact");
            CityId = activity.intent.getStringExtra("CityId");
            ShopsListDataResponse = activity.intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = activity.intent.getStringExtra("citiesNamesIDsResponse");
            isLocationAvail = activity.intent.getStringExtra("isLocationAvail");
            Bundle bundle = activity.intent.getParcelableExtra("bundle");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = activity.intent.getStringExtra("placeName");
        }
    }

    private void SetdataToViews() {
        if (mshopImage != null && !mshopImage.equals("")) {
            activity.aQuery.find(R.id.pg_shop_image_blur).visibility(View.VISIBLE);
//            Glide.with(activity).load(mshopImage)
//                    .into(iv_shop_profile);
            Glide.with(activity)
                    .load(mshopImage)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            // Do something with bitmap here.
                            activity.aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
//                            Blurry.with(activity).from(bitmap).into(iv_shop_image_blur);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            activity.aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });

        }
    }

    private void APiPlaceOrder(final String UserId, final String shopID, final String serviceID, final String brandID,
                               final String modelID, final String orderType, final String customerMobileNo) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiSaveOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            morderID = jsonObject.getInt("orderID");
                            if (morderID != 0) {
                                if (orderType.equals("call")) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + mContact));
                                    startActivity(intent);
                                } else {
                                    Intent i = new Intent(activity, NavigationsActivity.class);
                                    i.putExtra("latitude", mlatitude);
                                    i.putExtra("longitude", mlongitude);
                                    i.putExtra("shopID", mshopID);
                                    i.putExtra("mPermissionsNowGiven", mPermissionsNowGiven);
                                    if (CityId != null && !CityId.equals("")) {
                                        i.putExtra("CityId", CityId);
                                        ShopsListActivity.ShopsListDataResponse = ShopsListDataResponse;
//            i.putExtra("ShopsListDataResponse", ShopsListActivity.ShopsListDataResponse);
                                        i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                                        i.putExtra("isLocationAvail", isLocationAvail);
                                        Bundle args = new Bundle();
                                        args.putParcelable("LatLngCurrent", mLatlngCurrent);
                                        i.putExtra("placeName", mplaceName);
                                        i.putExtra("bundle", args);


                                    }
                                    startActivity(i);
                                }
                                /*
                                -feedback to user when order is placed
                                -commented out by hh on the request of client
                                showToast(getResources().getString(R.string.toast_order_placed));
                                 */

                                mOrderPlaced = true;
                                if (activity.loading != null) {
                                    activity.loading.close();
                                }
                            }
                        } catch (JSONException e) {
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            activity.SendFireBaseError(String.valueOf(e));
                            activity.loading.close();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        activity.SendFireBaseError(String.valueOf(error));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                @SuppressWarnings("Convert2Diamond") Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserId);
                params.put("shopID", shopID);
//                params.put("serviceTypeID", serviceID);
//                params.put("brandID", brandID);
//                params.put("modelID", modelID);
                params.put("orderServiceType", "shops");
                params.put("orderType", orderType);
//                params.put("customerMobileNo", customerMobileNo);


                return params;
            }
        };
// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


//    @SuppressWarnings({"PointlessBooleanExpression", "UnusedParameters"})
//    public void CAllToShop(View V) {
//        if (Validations.isInternetAvailable(activity, true)) {
//            try {
//
//                if (mContact != null && !mContact.equals("")) {
//                    if (mOrderPlaced == false) {
//                        //noinspection StatementWithEmptyBody
//                        if (morderType != null && morderType.equals("navigate")) {
//                        } else {
//                            morderType = "call";
//                        }
//
//                        activity.loading.show();
//                        APiPlaceOrder(sUser_ID, mshopID, mServicesId, mBrandsId, mModelsId, morderType, sUser_Mobile);
//
//                    }
//
//                } else {
//                    activity.showToast(getResources().getString(R.string.toast_no_shop_contact_found));
//                }
//            } catch (Exception e) {
//                activity.SendFireBaseError(String.valueOf(e));
//                e.printStackTrace();
//            }
//        }
//    }

    @SuppressWarnings({"PointlessBooleanExpression", "UnusedParameters"})
    public void DirectionsToShop(View v) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            dirShopsIntents();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle(getResources().getString(R.string.app_name));
                alertDialog.setMessage(getResources().getString(R.string.dialog_need_your_permission));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dialog.dismiss();
                                    locationSettings = true;
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
    }

    private void dirShopsIntents() {
        if (!mOrderPlaced) {
            //noinspection StatementWithEmptyBody
            if (morderType != null && morderType.equals("call")) {
            } else {
                morderType = "navigate";
            }
            if (Validations.isInternetAvailable(activity, true)) {
                activity.loading.show();
                APiPlaceOrder(sUser_ID, mshopID, mServicesId, mBrandsId, mModelsId, morderType, sUser_Mobile);
            }
        }

    }

//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) this );
//    }
//
//    @SuppressWarnings("UnusedParameters")
//    @SuppressLint("RtlHardcoded")
//    public void btn_drawyerMenuOpen(View v) {
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
//    }

    @SuppressWarnings({"UnnecessaryReturnStatement", "NullableProblems"})
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dirShopsIntents();
                    return;
                } else {
//                    showToast(getResources().getString(R.string.toast_location_not_found));
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage(getResources().getString(R.string.dialog_need_your_permission));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                            dialog.dismiss();
                                            locationSettings = true;
                                        } else {
                                            dialog.dismiss();
                                            locationSettings = true;
                                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    } catch (Exception e) {
                                        dialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }


                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.dialog_cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }
                return;
            }

        }
    }

    @Override
    public void onResume() {
        if (morderType.equals("call")) {
            activity.intent = new Intent(activity, ShopsListActivity.class);
            activity.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.intent.putExtra("callType", "callOrder");
            if (CityId != null && !CityId.equals("")) {
                activity.intent.putExtra("CityId", CityId);
                activity.intent.putExtra("ShopsListDataResponse", ShopsListDataResponse);
                activity.intent.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                activity.intent.putExtra("isLocationAvail", isLocationAvail);
                Bundle args = new Bundle();
                args.putParcelable("LatLngCurrent", mLatlngCurrent);
                activity.intent.putExtra("placeName", mplaceName);
                activity.intent.putExtra("bundle", args);
            }
            startActivity(activity.intent);
            activity.finish();
        } else {
            if (locationSettings) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsNowGiven = "true";
                } else {
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Utils.isLocationServiceEnabled(activity)) {
                        mPermissionsNowGiven = "true";
                    }
                }
            }
        }
        super.onResume();
    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            if (morderType.equals("call")) {
//                activity.intent = new Intent(activity, ShopsListActivity.class);
//                activity.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                activity.intent.putExtra("callType", "callOrder");
//                if (CityId != null && !CityId.equals("")) {
//                    activity.intent.putExtra("CityId", CityId);
//                    activity.intent.putExtra("ShopsListDataResponse", ShopsListDataResponse);
//                    activity.intent.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
//                    activity.intent.putExtra("isLocationAvail", isLocationAvail);
//                    Bundle args = new Bundle();
//                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
//                    activity.intent.putExtra("placeName", mplaceName);
//                    activity.intent.putExtra("bundle", args);
//                }
//                startActivity(activity.intent);
//                activity.finish();
//            }
//            getActivity().onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_my_details) {
//            Intent i = new Intent(this, MyDetailsActivity.class);
//            startActivity(i);
//            // Handle the camera action
//        } else if (id == R.id.nav_my_orders) {
//            Intent i = new Intent(this, MyOrdersActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_fav_shops) {
//            Intent i = new Intent(this, FavouriteShopsActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_share) {
//            Intent i = new Intent(this, ShareActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_about_us) {
//            Intent i = new Intent(this, ContactUsActivity.class);
//            startActivity(i);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
//        return true;
//    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_order_now);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }

}
