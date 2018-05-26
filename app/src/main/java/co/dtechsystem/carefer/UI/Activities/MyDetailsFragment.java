package co.dtechsystem.carefer.UI.Activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MyDetailsFragment extends Fragment {
    private String mcustomerName;
    private String mCarBrandName;
    private String mCarBrandModel;
    private String mLastOilChange;
    private String mOilKM;
    View view;
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
    EditText name,km_oil;
    TextView brand,model,last_0il;
    Button save;
    MainActivity activity;
    private  Calendar myCalendar ;


    public MyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
        view=inflater.inflate(R.layout.fragment_my_details, container, false);
        setUpToolbar();

        myCalendar = Calendar.getInstance(activity.locale);
        name= (EditText) view.findViewById(R.id.et_user_name_my_details);
        brand= (TextView) view.findViewById(R.id.et_car_brand_my_details);
        model= (TextView) view.findViewById(R.id.et_car_model_my_details);
        last_0il= (TextView) view.findViewById(R.id.et_last_oil_my_details);
        km_oil= (EditText) view.findViewById(R.id.et_oil_change_km_my_details);
        save=(Button)view.findViewById(R.id.save);


        /////////////////////// edit profile action /////////


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitUserData(v);
            }
        });

        /////////////////////////////////////////////////////

        if (Validations.isInternetAvailable(activity, true)) {
            activity.loading.show();
            APiMyDetails(AppConfig.APiGetCustomerDetails + activity.sUser_ID, "getUserDetails", "", "", "", "", "");
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
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
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, activity.localeEn);

                activity.aQuery.find(R.id.et_last_oil_my_details).text(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    @SuppressWarnings("deprecation")
    private void SetData() {

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
        activity.aQuery.id(R.id.sp_brand_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    if (firstBrand) {
                        if (!mCarBrandName.equals("0")) {
                            activity.aQuery.find(R.id.et_car_brand_my_details).text(mCarBrandName);
                            firstBrand = false;
                        } else {
                            firstBrand = false;
                            activity.aQuery.find(R.id.et_car_brand_my_details).text(activity.aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());

                        }
                    } else {
                        activity.aQuery.find(R.id.et_car_brand_my_details).text(activity.aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());
                    }
                } else {
                    activity.aQuery.find(R.id.et_car_brand_my_details).text(activity.aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString());
                    if (Validations.isInternetAvailable(activity, true)) {
                        activity.loading.show();
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
        activity.aQuery.id(R.id.sp_car_model_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {

                    if (firstModel) {
                        if (!(mCarBrandModel.equals("false")||mCarBrandModel.equals("0"))) {
                            firstModel = false;
                            activity.aQuery.find(R.id.et_car_model_my_details).text(mCarBrandModel);

                        } else {
                            firstModel = false;
                            activity.aQuery.find(R.id.et_car_model_my_details).text((activity.aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

                        }
                    } else {
                        activity.aQuery.find(R.id.et_car_model_my_details).text((activity.aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

                    }
                } else {
                    activity.aQuery.find(R.id.et_car_model_my_details).text((activity.aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString()));

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
        RequestQueue queue = Volley.newRequestQueue(activity);
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
                                activity.aQuery.id(R.id.sp_brand_type_shop_details_order).adapter(StringdataAdapterbrands);

                                @SuppressWarnings("unchecked") ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                                activity.aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                activity.loading.close();
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
                                activity.aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                activity.aQuery.find(R.id.et_car_model_my_details).text(getResources().getString(R.string.dp_model));
                                activity.loading.close();

                            }
                            SetSpinnerListener();
                            activity.loading.close();
                        } catch (JSONException e) {
                            activity.loading.close();
                            activity.SendFireBaseError(String.valueOf(e));
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.SendFireBaseError(String.valueOf(error));
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
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



    @SuppressWarnings("UnusedParameters")
    public void submitUserData(View v) {
        if (Validations.isInternetAvailable(activity, true)) {

            String customerName = activity.aQuery.find(R.id.et_user_name_my_details).getText().toString();
            String et_car_brand_my_details = activity.aQuery.find(R.id.et_car_brand_my_details).getText().toString();
            String et_car_model_my_details = activity.aQuery.find(R.id.et_car_model_my_details).getText().toString();
            String et_last_oil_my_details = activity.aQuery.find(R.id.et_last_oil_my_details).getText().toString();
            String et_oil_change_km_my_details = activity.aQuery.find(R.id.et_oil_change_km_my_details).getText().toString();

            if (customerName.equals("") || et_car_brand_my_details.equals("") || et_car_model_my_details.equals("") || et_last_oil_my_details.equals("") || et_oil_change_km_my_details.equals("")) {
                activity.showToast(getResources().getString(R.string.toast_fill_all_fields));
            } else if (et_car_brand_my_details.equals(getResources().getString(R.string.dp_brand)) ||
                    et_car_model_my_details.equals(getResources().getString(R.string.dp_model))) {
                activity.showToast(getResources().getString(R.string.toast_select_one_drop));
            } else {
//                Utils.savePreferences(activity, "CustomerCarBrand", et_car_brand_my_details);
//                Utils.savePreferences(activity, "CustomerCarModel", et_car_model_my_details);
//                Utils.savePreferences(activity, "CustomerCarOilChange", et_last_oil_my_details);
                if (Validations.isInternetAvailable(activity, true)) {
                    activity.loading.show();
                    APiMyDetails(AppConfig.APisetCustomerDetails + activity.sUser_ID, "setUserDetails", customerName, mBrandsId, mModelsId, et_last_oil_my_details, et_oil_change_km_my_details);
                }
            }
        }
    }


    private void APiMyDetails(String URL, final String Type, final String customerName,final String carBrand, final String carModel,
                              final String lastOilChange, final String oilKm) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(activity);
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

                                    activity.aQuery.find(R.id.et_user_name_my_details).text(mcustomerName);

                                    if (!mLastOilChange.equals("null")) {
                                        activity.aQuery.find(R.id.et_last_oil_my_details).text(mLastOilChange);
                                    }
                                    if (!mOilKM.equals("null")) {
                                        activity.aQuery.id(R.id.et_oil_change_km_my_details).text(mOilKM);
                                    }
                                    SetData();
                                    if (Validations.isInternetAvailable(activity, true)) {
                                        APiGetBrandsServiceModelsData(AppConfig.APiBrandData, "Brands", "");
                                    }
                                } else {
                                    activity.showToast(getResources().getString(R.string.no_record_found));
                                }
                            } else {
                                activity.showToast(getResources().getString(R.string.toast_record_updated));
                                activity.finish();
                            }
                        } catch (JSONException e) {

                            activity.loading.close();
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                        activity.loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.SendFireBaseError(String.valueOf(error));
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
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

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.my_details);

        final ImageButton edit=(ImageButton) activity.findViewById(R.id.toolbar_edit);
        edit.setVisibility(View.VISIBLE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                name.setEnabled(true);
                km_oil.setEnabled(true);
                last_0il.setEnabled(true);
                model.setEnabled(true);
                brand.setEnabled(true);

                save.setVisibility(View.VISIBLE);

                last_0il.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.hideKeyboard(activity);
                        ShowDatePicker();
                    }
                }); ;
               brand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                    }
                });
                model.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.aQuery.id(R.id.sp_car_model_order).getSpinner().performClick();
                    }
                });

            }
        });

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);

        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);

    }


}
