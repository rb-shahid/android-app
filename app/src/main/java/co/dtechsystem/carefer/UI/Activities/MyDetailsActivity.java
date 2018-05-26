package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

@SuppressWarnings("unchecked")
public class MyDetailsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String mcustomerName;
    private String mcustomerMobile;
    private String mCarBrandName;
    private String mCarBrandModel;
    private String mLastOilChange;
    private String mOilKM;


    private TextView tv_title_my_details;
    private TextView tv_mobile_number_my_details;
    private TextView tv_name_my_details;
    private TextView tv_car_brand_my_details;
    private TextView tv_car_model_my_details;
    private TextView tv_last_oil_my_details, tv_km_my_details;
    private final Calendar myCalendar = Calendar.getInstance(locale);
    private final ArrayList<String> mServicesIdArray = new ArrayList<>();
    private final ArrayList<String> mBrandsIdArray = new ArrayList<>();
    private final ArrayList<String> mModelsIdArray = new ArrayList<>();
    private String mBrandsId;
    private String mModelsId;
    private boolean mModelData;
    private boolean mBrandData;
    private final List brands = new ArrayList();
    private List models = new ArrayList();
    private boolean firstBrand = true;
    private boolean firstModel = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_my_details);
        initializeViews();
        SetUpLeftbar();
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            APiMyDetails(AppConfig.APiGetCustomerDetails + sUser_ID, "getUserDetails", "", "", "", "", "", "", "");
        }

    }

    private void initializeViews() {
        tv_title_my_details = (TextView) findViewById(R.id.tv_title_my_details);
        tv_mobile_number_my_details = (TextView) findViewById(R.id.tv_mobile_number_my_details);
        tv_name_my_details = (TextView) findViewById(R.id.tv_name_my_details);
        tv_car_brand_my_details = (TextView) findViewById(R.id.tv_car_brand_my_details);
        tv_car_model_my_details = (TextView) findViewById(R.id.tv_car_model_my_details);
        tv_last_oil_my_details = (TextView) findViewById(R.id.tv_last_oil_my_details);
        tv_km_my_details = (TextView) findViewById(R.id.tv_km_my_details);
        SetShaderToViews();

    }

    @SuppressWarnings("deprecation")
    private void SetData() {
//        if (!mCarBrandName.equals("")) {
//            aQuery.find(R.id.et_car_brand_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
//
//        }
//        if (!mCarBrandModel.equals("")) {
//            aQuery.find(R.id.et_car_model_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
//
//        }
//        if (!mLastOilChange.equals("")) {
//            aQuery.find(R.id.et_last_oil_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
//
//        }
//        if (!mcustomerName.equals("")) {
//            aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
//
//        }
        aQuery.find(R.id.et_mobile_my_details).getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_mobile_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_edit_hover), null);
                } else {
                    aQuery.find(R.id.et_mobile_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_edit), null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        aQuery.find(R.id.et_user_name_my_details).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_car_brand_my_details).getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(getResources().getString(R.string.dp_brand))) {
                    aQuery.find(R.id.et_car_brand_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_car_brand_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_car_model_my_details).getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(getResources().getString(R.string.dp_model))) {
                    aQuery.find(R.id.et_car_model_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_car_model_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_last_oil_my_details).getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_last_oil_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_last_oil_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_oil_change_km_my_details).getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_oil_change_km_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_oil_change_km_my_details).getTextView().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        aQuery.find(R.id.et_last_oil_my_details).getTextView().setInputType(InputType.TYPE_NULL);
//        aQuery.find(R.id.et_mobile_my_details).getEditText().setInputType(InputType.TYPE_NULL);

        aQuery.find(R.id.et_last_oil_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(activity);
                ShowDatePicker();
            }
        });
        aQuery.find(R.id.et_car_brand_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
            }
        });
        aQuery.find(R.id.et_car_model_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.id(R.id.sp_car_model_order).getSpinner().performClick();
            }
        });


        //Lists initilization

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

    }

    private void SetSpinnerListener() {
        aQuery.id(R.id.sp_brand_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    if (firstBrand) {
                        if (!mCarBrandName.equals("0")) {
                            aQuery.find(R.id.et_car_brand_my_details).text(mCarBrandName);
                            firstBrand = false;
                        } else {
                            firstBrand = false;
                            aQuery.find(R.id.et_car_brand_my_details).text(aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());

                        }
                    } else {
                        aQuery.find(R.id.et_car_brand_my_details).text(aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());
                    }
                } else {
                    aQuery.find(R.id.et_car_brand_my_details).text(aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());
                    if (Validations.isInternetAvailable(activity, true)) {
                        loading.show();
                        APiGetBrandsServiceModelsData(AppConfig.APiGetBrandModels, "ModelYear", mBrandsIdArray.get(position));
                    }
                }
                if (mBrandData) {
                    mBrandData = false;
                } else {
                    mBrandsId = mBrandsIdArray.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.id(R.id.sp_car_model_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {

                    if (firstModel) {
                        if (!(mCarBrandModel.equals("false")||mCarBrandModel.equals("0"))) {
                            firstModel = false;
                            aQuery.find(R.id.et_car_model_my_details).text(mCarBrandModel);

                        } else {
                            firstModel = false;
                            aQuery.find(R.id.et_car_model_my_details).text((aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

                        }
                    } else {
                        aQuery.find(R.id.et_car_model_my_details).text((aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

                    }
                } else {
                    aQuery.find(R.id.et_car_model_my_details).text((aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

                }
                if (mModelData) {
                    mModelData = false;
                } else {
                    mModelsId = mModelsIdArray.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                            if (Type.equals("Brands")) {

                                JSONArray brandsData = response.getJSONArray("brandsData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    brands.add(jsonObject.getString("brandName"));
                                    mBrandsIdArray.add(jsonObject.getString("ID"));
                                }

                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, R.layout.lay_spinner_item, brands);
                                aQuery.id(R.id.sp_brand_type_shop_details_order).adapter(StringdataAdapterbrands);

                                @SuppressWarnings("unchecked") ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                                aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                loading.close();
                            } else {
                                JSONArray modelsData = response.getJSONArray("models");
                                models.clear();
                                if(modelsData.length()==0) {
//                                    models.add(0, getResources().getString(R.string.dp_model));

                                    //aQuery.find(R.id.sp_brand_type_shop_details_order).text(aQuery.find(R.string.toast_select_one_drop).getText());

                                }
                                for (int i = 0; i < modelsData.length(); i++) {
                                    JSONObject jsonObject = modelsData.getJSONObject(i);
                                    //noinspection unchecked
                                    models.add(jsonObject.getString("modelName"));
                                    mModelsIdArray.add(jsonObject.getString("ID"));
                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                               // if(aQuery.id(R.id.sp_car_model_order).getSpinner().getAdapter()!=null)
                                 //   aQuery.id(R.id.sp_car_model_order).getSpinner().removeAllViews();
                                aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                aQuery.find(R.id.et_car_model_my_details).text(getResources().getString(R.string.dp_model));
                                loading.close();

                            }
                            SetSpinnerListener();
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
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
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
        queue.add(postRequest);
    }


    private void ShowDatePicker() {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, localeEn);

                aQuery.find(R.id.et_last_oil_my_details).text(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_my_details, activity);
        Utils.gradientTextViewShort(tv_mobile_number_my_details, activity);
        Utils.gradientTextViewShort(tv_name_my_details, activity);
        Utils.gradientTextViewShort(tv_car_brand_my_details, activity);
        Utils.gradientTextViewShort(tv_car_model_my_details, activity);
        Utils.gradientTextViewShort(tv_last_oil_my_details, activity);
        Utils.gradientTextViewLong(tv_km_my_details, activity);


    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("UnusedParameters")
    public void submitUserData(View v) {
        if (Validations.isInternetAvailable(activity, true)) {

            String customerName = aQuery.find(R.id.et_user_name_my_details).getText().toString();
            String customerMobile = aQuery.find(R.id.et_mobile_my_details).getText().toString();
            String et_car_brand_my_details = aQuery.find(R.id.et_car_brand_my_details).getText().toString();
            String et_car_model_my_details = aQuery.find(R.id.et_car_model_my_details).getText().toString();
            String et_last_oil_my_details = aQuery.find(R.id.et_last_oil_my_details).getText().toString();
            String et_oil_change_km_my_details = aQuery.find(R.id.et_oil_change_km_my_details).getText().toString();
            if (!customerMobile.equals(sUser_Mobile)) {
                if (Utils.ValidateNumberFromLibPhone(activity, customerMobile))
                    showMobileChangeAlert(customerMobile);
                return;
            }
            if (customerName.equals("") || customerMobile.equals("") || et_car_brand_my_details.equals("") ||
                    et_car_model_my_details.equals("") || et_last_oil_my_details.equals("") || et_oil_change_km_my_details.equals("")) {
                showToast(getResources().getString(R.string.toast_fill_all_fields));
            } else if (et_car_brand_my_details.equals(getResources().getString(R.string.dp_brand)) ||
                    et_car_model_my_details.equals(getResources().getString(R.string.dp_model))) {
                showToast(getResources().getString(R.string.toast_select_one_drop));
            } else {
//                Utils.savePreferences(activity, "CustomerCarBrand", et_car_brand_my_details);
//                Utils.savePreferences(activity, "CustomerCarModel", et_car_model_my_details);
//                Utils.savePreferences(activity, "CustomerCarOilChange", et_last_oil_my_details);
                if (Validations.isInternetAvailable(activity, true)) {
                    loading.show();
                    APiMyDetails(AppConfig.APisetCustomerDetails + sUser_ID, "setUserDetails", customerName, customerMobile, sUser_Mobile_Varify, mBrandsId, mModelsId, et_last_oil_my_details, et_oil_change_km_my_details);
                }
            }
        }
    }

    private void showMobileChangeAlert(final String customerMobile) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.dialog_message))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Utils.savePreferences(activity, "User_Mobile", sUser_Mobile);
//                        Utils.savePreferences(activity, "User_Mobile_varify", "");
//                        Utils.savePreferences(activity, "User_privacy_check", "");
                        //Utils.savePreferences(activity, "User_ID", sUser_ID);
//                        Intent intent = new Intent(activity, MobileNumVerifyActivity.class);
////                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("User_Mobile", User_Mobile);
//                        intent.putExtra("User_ID", sUser_ID);
//                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Validations.isInternetAvailable(activity, true)) {
                            loading.show();
                            APiChangeUserPhone(customerMobile);
                        }
                    }
                }).create().show();
    }

    private void APiMyDetails(String URL, final String Type, final String customerName,
                              final String customerMobile, final String isVerified, final String carBrand, final String carModel,
                              final String lastOilChange, final String oilKm) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            if (Type.equals("getUserDetails")) {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("customerDetail");
                                if (jsonObject1.length() > 0) {
                                    mcustomerName = jsonObject1.getString("customerName");
                                    mcustomerMobile = jsonObject1.getString("customerMobile");
                                    mCarBrandName = jsonObject1.getString("carBrand");
                                    mCarBrandModel = jsonObject1.getString("carModel");
                                    mLastOilChange = jsonObject1.getString("lastOilChange");
                                    mBrandsId = jsonObject1.getString("carBrandId");
                                    mModelsId = jsonObject1.getString("carModelId");
                                    mOilKM = jsonObject1.getString("oilKM");
                                    if (mBrandsId != null && !mBrandsId.equals("0")) {
                                        mModelData = true;
                                    }
                                    if (mModelsId != null && !mModelsId.equals("0")) {
                                        mBrandData = true;
                                    }

                                    aQuery.find(R.id.et_user_name_my_details).text(mcustomerName);
                                    aQuery.find(R.id.et_mobile_my_details).text(mcustomerMobile);

                                    if (!mLastOilChange.equals("null")) {
                                        aQuery.find(R.id.et_last_oil_my_details).text(mLastOilChange);
                                    }
                                    if (!mOilKM.equals("null")) {
                                        aQuery.id(R.id.et_oil_change_km_my_details).text(mOilKM);
                                    }
                                    SetData();
                                    if (Validations.isInternetAvailable(activity, true)) {
                                        APiGetBrandsServiceModelsData(AppConfig.APiBrandData, "Brands", "");
                                    }
                                } else {
                                    showToast(getResources().getString(R.string.no_record_found));
                                }
                            } else {
                                showToast(getResources().getString(R.string.toast_record_updated));
                                finish();
                            }
                        } catch (JSONException e) {

                            loading.close();
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!Type.equals("getUserDetails")) {
                    params.put("customerName", customerName);
                    params.put("customerMobile", customerMobile);
                    params.put("isVerified", isVerified);
                    params.put("carBrand", carBrand);
                    params.put("carModel", carModel);
                    params.put("lastOilChange", lastOilChange);
                    params.put("oilKM", oilKm);


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

    private void APiChangeUserPhone(final String customerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiChangeUserPhone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject customerDetails = jsonObject.getJSONObject("customer");
                            String smsAPIResponse = customerDetails.getString("APIResponse");
//                            String customerMobile = customerDetails.getString("customerMobile");
                            if (smsAPIResponse != null && !smsAPIResponse.equals("SMS sent successfully.")) {
                                showToast(smsAPIResponse);
                            } else {
                                Intent intent = new Intent(activity, MobileNumVerifyActivity.class);
                                intent.putExtra("customerMobile", customerMobile);
                                startActivity(intent);
                                showToast(getResources().getString(R.string.toast_verfication_sent_mobile));
                                loading.close();
                            }
                        } catch (JSONException e) {
                            SendFireBaseError(String.valueOf(e));
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileNumber", customerMobile);
                params.put("customerID", sUser_ID);


                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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
//            Intent i = new Intent(this, MyDetailsActivity.class);
//            startActivity(i);
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
