package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

@SuppressWarnings("unchecked")
public class ShopDetailsOrderActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String mshopID, mshopName, mshopType, mshopRating, mlatitude, mlongitude, mshopImage, mContact;
    private final ArrayList<String> mServicesIdArray = new ArrayList<>();
    private final ArrayList<String> mBrandsIdArray = new ArrayList<>();
    private final ArrayList<String> mModelsIdArray = new ArrayList<>();
    private String mServicesId;
    private String mBrandsId, mplaceName;
    private String mModelsId, CityId, ShopsListDataResponse, citiesNamesIDsResponse, isLocationAvail;
    private final List listservices = new ArrayList();
    private final List brands = new ArrayList();
    private List models = new ArrayList();
    private TextView tv_title_shops_details_order;
    private LatLng mLatlngCurrent;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details_order);
        tv_title_shops_details_order = (TextView) findViewById(R.id.tv_title_shops_details_order);
        SetShaderToViews();
        SetUpLeftbar();
        GetDataForViews();
        SetDataTOViews();
        SetSpinnerListener();
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_shops_details_order, activity);
    }

    // Get Views Data
    private void GetDataForViews() {
        if (intent != null) {
            mshopID = intent.getStringExtra("shopID");
            mshopName = intent.getStringExtra("shopName");
            mshopType = intent.getStringExtra("shopType");
            mshopRating = intent.getStringExtra("shopRating");
            mlatitude = intent.getStringExtra("latitude");
            mlongitude = intent.getStringExtra("longitude");
            mshopImage = intent.getStringExtra("shopImage");
            mContact = intent.getStringExtra("contact");
            CityId = intent.getStringExtra("CityId");
            ShopsListDataResponse = intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = intent.getStringExtra("citiesNamesIDsResponse");
            isLocationAvail = intent.getStringExtra("isLocationAvail");
            Bundle bundle = intent.getParcelableExtra("bundle");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = intent.getStringExtra("placeName");
        }
    }

    // Set views data
    private void SetDataTOViews() {
        if (Validations.isInternetAvailable(activity, true)) {
            aQuery.id(R.id.tv_shop_name_shop_details_order).text(mshopName);
            aQuery.id(R.id.tv_shop_type_shop_details_order).text(mshopType);
            aQuery.id(R.id.rb_shop_rating_shop_details_order).rating(Float.parseFloat(mshopRating));

            //Lists initilization
            listservices.clear();
            //noinspection unchecked
            listservices.add(0, getResources().getString(R.string.dp_service_type));
            brands.clear();
            //noinspection unchecked
            brands.add(0, getResources().getString(R.string.dp_brand));

            //noinspection unchecked
            models.clear();
            models.add(0, getResources().getString(R.string.dp_model));

            mServicesIdArray.clear();
            mBrandsIdArray.clear();
            mModelsIdArray.clear();
            mServicesIdArray.add(0, "0");
            mBrandsIdArray.add(0, "0");
            mModelsIdArray.add(0, "0");
            loading.show();
            APiGetBrandsServiceModelsData(AppConfig.APiShopsDetailsData + mshopID + "/cusid/" + sUser_ID, "Services & Brands", "");

        }
    }


    private void APiGetBrandsServiceModelsData(final String Url, final String Type, final String BrandId) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            JSONObject response = new JSONObject(res);
                            if (Type.equals("Services & Brands")) {
                                JSONArray shopServiceTypes = response.getJSONArray("shopServiceTypes");
                                for (int i = 0; i < shopServiceTypes.length(); i++) {
                                    JSONObject jsonObject = shopServiceTypes.getJSONObject(i);
                                    //noinspection unchecked
                                    listservices.add(jsonObject.getString("serviceTypeName"));
                                    mServicesIdArray.add(jsonObject.getString("ID"));
                                }
                                JSONArray brandsData = response.getJSONArray("shopBrands");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    brands.add(jsonObject.getString("brandName"));
                                    mBrandsIdArray.add(jsonObject.getString("ID"));
                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, listservices);
                                aQuery.id(R.id.sp_srvice_type_shop_details_order).adapter(StringdataAdapter);

                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, R.layout.lay_spinner_item, brands);
                                aQuery.id(R.id.sp_brand_type_shop_details_order).adapter(StringdataAdapterbrands);

                                @SuppressWarnings("unchecked") ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                                aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                loading.close();
                            } else {
                                JSONArray modelsData = response.getJSONArray("models");
                                if (modelsData.length() > 0) {
                                    for (int i = 0; i < modelsData.length(); i++) {
                                        JSONObject jsonObject = modelsData.getJSONObject(i);
                                        //noinspection unchecked
                                        models.add(jsonObject.getString("modelName"));
                                        mModelsIdArray.add(jsonObject.getString("ID"));
                                    }
                                } else {
                                    models.clear();
                                    models.add(0, getResources().getString(R.string.dp_model));
                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                                aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                loading.close();

                            }


                            loading.close();
                        } catch (JSONException e) {
                            loading.close();
                            SendFireBaseError(String.valueOf(e));
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        SendFireBaseError(String.valueOf(error));
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!Type.equals("Services & Brands")) {
                    params.put("brandID", BrandId);
                }


                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void SetSpinnerListener() {
        aQuery.id(R.id.sp_srvice_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    aQuery.id(R.id.lay_service).background(R.drawable.dr_corner_black);
                } else {
                    aQuery.id(R.id.lay_service).background(R.drawable.dr_corner_ornage);
                }
                mServicesId = mServicesIdArray.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.id(R.id.sp_brand_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    aQuery.id(R.id.lay_brands).background(R.drawable.dr_corner_black);
                } else {
                    aQuery.id(R.id.lay_brands).background(R.drawable.dr_corner_ornage);
                    if (Validations.isInternetAvailable(activity, true)) {
                        loading.show();

                        APiGetBrandsServiceModelsData(AppConfig.APiGetBrandModels, "ModelYear", mBrandsIdArray.get(position));
                    }
                }
                mBrandsId = mBrandsIdArray.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.id(R.id.sp_car_model_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    aQuery.id(R.id.lay_models).background(R.drawable.dr_corner_black);
                } else {
                    aQuery.id(R.id.lay_models).background(R.drawable.dr_corner_ornage);

                }
                mModelsId = mModelsIdArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @SuppressWarnings("UnusedParameters")
    public void GotoOrderNow(View V) {
        if (Validations.isInternetAvailable(activity, true)) {
            String spServiceTypeText = aQuery.id(R.id.sp_srvice_type_shop_details_order).getSelectedItem().toString();
            String spbrandTypeText = aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString();
            String spmodelTypeText = aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString();
            if (spServiceTypeText.equals("") || spbrandTypeText.equals("") || spmodelTypeText.equals("") ||
                    spServiceTypeText.equals(getResources().getString(R.string.dp_service_type)) || spbrandTypeText.equals(getResources().getString(R.string.dp_brand)) ||
                    spmodelTypeText.equals(getResources().getString(R.string.dp_model))) {
                showToast(getResources().getString(R.string.toast_select_one_drop));
            } else {
                Intent i = new Intent(this, OrderNowActivity.class);
                i.putExtra("latitude", mlatitude);
                i.putExtra("longitude", mlongitude);
                i.putExtra("shopID", mshopID);
                i.putExtra("serviceID", mServicesId);
                i.putExtra("brandID", mBrandsId);
                i.putExtra("modelID", mModelsId);
                i.putExtra("shopImage", mshopImage);
                i.putExtra("contact", mContact);
                if (CityId != null && !CityId.equals("")) {
                    i.putExtra("CityId", CityId);
                    ShopsListActivity.ShopsListDataResponse = ShopsListDataResponse;
//                    i.putExtra("ShopsListDataResponse", ShopsListActivity.ShopsListDataResponse);
                    i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                    i.putExtra("isLocationAvail", isLocationAvail);
                    Bundle args = new Bundle();
                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
                    i.putExtra("placeName", mplaceName);
                    i.putExtra("bundle", args);
                }

                startActivity(i);
            }
        }
    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("UnusedParameters")
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
