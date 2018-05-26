package co.dtechsystem.carefer.UI.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import co.dtechsystem.carefer.Adapters.ShopsListRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.SplashActivity;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.FragmentUtil;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

import static android.app.Activity.RESULT_OK;
import static com.nexmo.sdk.verify.core.service.BaseService.gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopsListFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static ShopsListRecycleViewAdapter mshopsListRecycleViewAdapter;
    private DrawerLayout mDrawerLayout;
    private String mplaceName;
    private EditText et_search_shops_main;
    private TextView tv_total_results_shops_list;
    private TextView tv_title_shops_list;
    private ShopsListModel mShopsListModel;
    private LatLng mLatlngCurrent;
    private SwipeRefreshLayout lay_pull_refresh_shops_list;
    private String ShopsData;
    private RecyclerView recyclerView;
    private Button btn_back_top_shops_list;
    public static String ShopsListDataResponse = "";
    private static String citiesNamesIDsResponse = "";
    private static String CityId = "1"; //set city id 1 for riyadh as default
    private static String isLocationAvail = "";
    private final List listCities = new ArrayList();
    private final List listCitiesId = new ArrayList();
    private int check = 0;
    private boolean found = false;
    private String CityName = "";
    private String provide_warranty = "";
    private String provide_ReplaceParts = "";
    private String topRated = "";
    private String placeType = "";
    private String brandType = "";
    private String serviceType = "";
    private String FilterRecord = "";
    private Intent FilteredData;
    private ArrayList<Integer> CheckedServices = new ArrayList<>();
    private ArrayList<Integer> CheckedBrands = new ArrayList<>();
    private ArrayList<Integer> CheckedShopTypes = new ArrayList<>();
    private String callType = "";
    private boolean locationSettings = false;
    private LinearLayoutManager mLinearLayoutManager;
    View view;
    Bundle b;
    MainActivity activity;

    public ShopsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shops_list, container, false);
       recyclerView = (RecyclerView) view.findViewById(R.id.rv_shop_list);
        recyclerView.setItemViewCacheSize(15);
       recyclerView.setDrawingCacheEnabled(true);
       recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        tv_total_results_shops_list = (TextView) view.findViewById(R.id.tv_total_results_shops_list);
        lay_pull_refresh_shops_list = (SwipeRefreshLayout) view.findViewById(R.id.lay_pull_refresh_shops_list);
        lay_pull_refresh_shops_list.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorHeliotrope));
      //  lay_pull_refresh_shops_list.setOnRefreshListener(activity);
        Utils.hideKeyboard(activity);
        //  SetShaderToViews();
        //  SetUpLeftbar();
        setUpToolbar();
        b = getArguments();
        if (b != null) {
            ShopsListDataResponse = b.getString("ShopsListDataResponse");
            citiesNamesIDsResponse = b.getString("citiesNamesIDsResponse");
            CityId = b.getString("CityId");
            if (CityId.isEmpty())
                CityId = "1";
            isLocationAvail = b.getString("isLocationAvail");
            if (b.getString("callType") != null) {
                callType = b.getString("callType");
            } else {
                callType = "";
            }
        }
        //getDataForView();
        // setDataToView();
        // setFilterDataToViews();
        if (ShopsListDataResponse != null && !ShopsListDataResponse.equals("") && citiesNamesIDsResponse != null && !citiesNamesIDsResponse.equals("")) {
            ShopsData = ShopsListDataResponse;
            activity.loading.show();
//                SetListData("List", mShopsListModel.getShopsList().size());
            AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
            asyncTaskRunner.execute("List", "");
            if (citiesNamesIDsResponse != null && !citiesNamesIDsResponse.equals("")) {
                if (listCities != null) {
                    listCities.clear();
                }
                if (mplaceName != null && !mplaceName.equals("")) {
//                        listCities.add(0, mplaceName);
                } else {
                    listCities.add(0, getResources().getString(R.string.tv_city));
                    listCitiesId.add(0, "0");
                }


            }

        }
        return view;
    }

    private void setUpToolbar() {
        TextView title = (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.shops_list);

        final ImageButton sort = (ImageButton) activity.findViewById(R.id.toolbar_sort);
        final ImageButton search = (ImageButton) activity.findViewById(R.id.toolbar_search);
        final ImageButton filter = (ImageButton) activity.findViewById(R.id.toolbar_filter);
        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });
        sort.setVisibility(View.VISIBLE);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSortingDialog();
            }
        });

        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        filter.setVisibility(View.VISIBLE);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText( getContext() , "tttt", Toast.LENGTH_SHORT ).show();
                FragmentUtil.replaceFragmentWithBackStack(activity, new FiltersActivity(), R.id.main_frame, null);

//                if (mShopsListModel != null && mShopsListModel.getShopsList().size() > 0) {
//                    Bundle args = new Bundle();
//                    Intent intent = new Intent(activity, FiltersActivity.class);
//
//                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
//                    FiltersActivity.ShopsDataResponse = ShopsListDataResponse;
//                    if (FilteredData != null && FilteredData.getExtras() != null) {
//                        intent.putExtra("provide_warranty", provide_warranty);
//                        intent.putExtra("provide_ReplaceParts", provide_ReplaceParts);
//                        intent.putExtra("topRated", topRated);
//                        intent.putExtra("placeType", placeType);
//                        intent.putExtra("brandType", brandType);
//                        intent.putExtra("serviceType", serviceType);
//                        intent.putExtra("FilterRecord", FilterRecord);
//                        intent.putIntegerArrayListExtra("CheckedBrands", CheckedBrands);
//                        intent.putIntegerArrayListExtra("CheckedShopTypes", CheckedShopTypes);
//                        intent.putIntegerArrayListExtra("CheckedServices", CheckedServices);
//
//                        args.putString("provide_warranty", provide_warranty);
//                        args.putString("provide_ReplaceParts", provide_ReplaceParts);
//                        args.putString("topRated", topRated);
//                        args.putString("placeType", placeType);
//                        args.putString("brandType", brandType);
//                        args.putString("serviceType", serviceType);
//                        args.putString("FilterRecord", FilterRecord);
//                        args.putIntegerArrayList("CheckedBrands", CheckedBrands);
//                        args.putIntegerArrayList("CheckedShopTypes", CheckedShopTypes);
//                        args.putIntegerArrayList("CheckedServices", CheckedServices);
//                    }
//                    startActivityForResult(intent, 1);
//
//                } else {
//
//                }

            }
        });

        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);


    }

    private void APiGetShopslistData(final String Url, final String Type, final String City, final String CityID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response: ", response);

                        try {
                            if (Type.equals("Shops") && City.equals("City") || City.equals("")) {
                                ShopsData = response.toString();
                                ShopsListDataResponse = response.toString();
//                                mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
//                                if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
//                                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList(), mLatlngCurrent, btn_back_top_shops_list, isLocationAvail);
                                if (CityName != null && !CityName.equals("")) {
                                    activity.aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
                                } else {
                                    activity.aQuery.id(R.id.tv_location_name_shops_list).text(mplaceName);

                                }
                                AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                                asyncTaskRunner.execute("List", "");
//                                    SetListData("List", mShopsListModel.getShopsList().size());
//                                    loading.close();
//                                } else {
//                                    if (lay_pull_refresh_shops_list.isRefreshing()) {
//                                        lay_pull_refresh_shops_list.setRefreshing(false);
//                                    }
//                                    loading.close();
//                                    showToast(getResources().getString(R.string.no_record_found));
//                                }
                                if (City.equals("City")) {
                                    APiGetShopslistData(AppConfig.APiGetCitiesList, "", "City", CityID);
                                }
                            } else if (Type.equals("") && City.equals("City")) {
                                if (mplaceName != null && !mplaceName.equals("")) {
                                    if (listCities != null) {
                                        listCities.clear();
                                    }
//                                    listCities.add(0, mplaceName);
                                } else {
                                    citiesNamesIDsResponse = response.toString();
                                    listCities.add(0, getResources().getString(R.string.tv_city));
                                    listCitiesId.add(0, "0");
                                }
                                JSONArray jsonArray = new JSONArray(citiesNamesIDsResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String ID = jsonObject.getString("ID");
                                    String name = jsonObject.getString("name");
                                    listCities.add(name);
                                    listCitiesId.add(ID);
                                }
                                setCityDropDownData();
                                activity.loading.close();
                            }
//                                setSpinnerFilter();


                        } catch (JSONException e) {
                            activity.loading.close();
                            if (lay_pull_refresh_shops_list.isRefreshing()) {
                                lay_pull_refresh_shops_list.setRefreshing(false);
                            }
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                            activity.SendFireBaseError(String.valueOf(e));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (lay_pull_refresh_shops_list.isRefreshing()) {
                            lay_pull_refresh_shops_list.setRefreshing(false);
                        }
                        activity.loading.close();
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                        error.printStackTrace();
                        activity.SendFireBaseError(String.valueOf(error));
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cityID", CityID);
                return params;
            }
        };
// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ArrayList<String> prams = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {
            if (ShopsListDataResponse != null && !ShopsListDataResponse.equals("")) {
                mShopsListModel = gson.fromJson(ShopsListDataResponse, ShopsListModel.class);
            } else {
                ShopsListDataResponse = "{\"shopsList\":[{}]}";
            }
            prams.clear();
            prams.add(0, params[0]);
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
                if (!prams.get(0).equals("Filter")) {
                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList(), mLatlngCurrent, isLocationAvail);
                }
                SetListData(prams.get(0), mShopsListModel.getShopsList().size());
                activity.loading.close();
            } else {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
                activity.loading.close();
                activity.showToast(getResources().getString(R.string.no_record_found));
            }
            // execution of result of Long time consuming operation

        }
    }


    //Setting Shops List Data
    @SuppressLint("SetTextI18n")
    private void SetListData(String Type, int Size) {
        try {
            tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + Integer.toString(Size));
//            recyclerView.getItemAnimator().setChangeDuration(700);
            mshopsListRecycleViewAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mshopsListRecycleViewAdapter);
            if (!Type.equals("Filter")) {
                if (isLocationAvail != null && isLocationAvail.equals("Yes")) {
                    mshopsListRecycleViewAdapter.SortFilterDistanceDefault();
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                } else {
                    ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Ascending", mLatlngCurrent, "");
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                    }
                }

            } else {
                if (mshopsListRecycleViewAdapter != null) {
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
            }


            if (!Type.equals("Filter")) {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
            }


            btn_back_top_shops_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.smoothScrollToPosition(0);
                    if (btn_back_top_shops_list.getVisibility() == View.VISIBLE) {
                        btn_back_top_shops_list.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            if (!Type.equals("Filter")) {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
            }
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                FilteredData = data;
                ArrayList<String> ShopsIds = data.getStringArrayListExtra("ShopslistAfterFiltration");
                String response = ShopsListDataResponse;

                provide_warranty = data.getStringExtra("provide_warranty");
                provide_ReplaceParts = data.getStringExtra("provide_ReplaceParts");
                topRated = data.getStringExtra("topRated");
                placeType = data.getStringExtra("placeType");
                brandType = data.getStringExtra("brandType");
                serviceType = data.getStringExtra("serviceType");
                FilterRecord = data.getStringExtra("FilterRecord");
                mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                CheckedServices = data.getIntegerArrayListExtra("CheckedServices");
                CheckedBrands = data.getIntegerArrayListExtra("CheckedBrands");
                CheckedShopTypes = data.getIntegerArrayListExtra("CheckedShopTypes");


                List<ShopsListModel.ShopslistRecord> _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration = new ArrayList<>();
                if (_ShopslistBeforeFiltration != null) {

//                    if (_ShopslistBeforeFiltration.size() == 0) {
//                        _ShopslistBeforeFiltration.addAll(_ShopslistAfterFiltration);
//                    }
//                    _ShopslistAfterFiltration.clear();
                    for (int i = 0; i < ShopsIds.size(); i++) {
                        for (int j = 0; j < _ShopslistBeforeFiltration.size(); j++) {
                            if (ShopsIds.get(i).toString().equals(_ShopslistBeforeFiltration.get(j).getID().toString())) {
                                _ShopslistAfterFiltration.add(_ShopslistBeforeFiltration.get(j));
                                break;
                            } else {
//                                _ShopslistAfterFiltration.remove(i);
//                                break;
//                                _ShopslistBeforeFiltration.remove(i);
//                                break;
                            }
                        }
                    }
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter = null;
//                mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    }

                }
                mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, _ShopslistAfterFiltration, mLatlngCurrent, isLocationAvail);
                AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                asyncTaskRunner.execute("Filter", String.valueOf(mShopsListModel.getShopsList().size()));
//                SetListData("Filter", _ShopslistAfterFiltration.size());

            }
        }
    }


    private void setCityDropDownData() {

        final ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, listCities);
        activity.aQuery.id(R.id.sp_city_name_shops_list).adapter(StringModeldataAdapter);

        activity.aQuery.id(R.id.sp_city_name_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.aQuery.id(R.id.sp_city_name_shops_list).getSpinner().performClick();
            }
        });
        activity.aQuery.id(R.id.tv_location_name_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.aQuery.id(R.id.sp_city_name_shops_list).getSpinner().performClick();
            }
        });

        activity.aQuery.id(R.id.sp_city_name_shops_list).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    CityName = activity.aQuery.id(R.id.sp_city_name_shops_list).getSpinner().getSelectedItem().toString();
                    if (CityName.equals(getResources().getString(R.string.tv_city))) {
                        activity.aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                        ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("City", "", mLatlngCurrent, "");
//                        if (mshopsListRecycleViewAdapter != null) {
//                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
//                            aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                           tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results)+" "+mshopsListRecycleViewAdapter.getItemCount() );
//
//
//                        }
                    } else {
                        CityId = listCitiesId.get(position).toString();
                        if (CityId != null && !CityId.equals("")) {
                            if (Validations.isInternetAvailable(activity, true)) {
                                activity.loading.show();
                                APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, "Shops", "", CityId);

                            }
                        } else {
                            APiGetShopslistData(AppConfig.APiGetCitiesList, "", "City", CityId);

                        }
                    }
//                        ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("City", "", mLatlngCurrent, CityName);
//                        if (mshopsListRecycleViewAdapter != null) {
//                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
//                            aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                           tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results)+" "+mshopsListRecycleViewAdapter.getItemCount() );
//
//
//                        }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < listCities.size(); i++) {
            String name = listCities.get(i).toString().toLowerCase(activity.locale);
            if (name.contains(mplaceName.toLowerCase(activity.locale))) {
                activity.aQuery.id(R.id.sp_city_name_shops_list).getSpinner().setSelection(i);
                found = true;
                break;
            }
        }

    }

    //Name Sorting dialog fun
    private void CustomNameSortingDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_sort_name);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_name_sorting = (Button) dialog.findViewById(R.id.btn_name_sorting);
        Button btn_rating_sorting = (Button) dialog.findViewById(R.id.btn_rating_sorting);
        Button btn_cancel_sorting = (Button) dialog.findViewById(R.id.btn_cancel_sorting);
        // if button is clicked, close the custom dialog

        btn_name_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Ascending", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
                dialog.dismiss();
            }
        });
        btn_rating_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Descending", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
                dialog.dismiss();
            }
        });
        btn_cancel_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Sorting dialog fun
    private void CustomSortingDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_sorting);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_name_sorting = (Button) dialog.findViewById(R.id.btn_name_sorting);
        Button btn_rating_sorting = (Button) dialog.findViewById(R.id.btn_rating_sorting);
        Button btn_distance_sorting = (Button) dialog.findViewById(R.id.btn_distance_sorting);
//        Button btn_cancel_sorting = (Button) dialog.findViewById(R.id.btn_cancel_sorting);

        // if button is clicked, close the custom dialog

        btn_name_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomNameSortingDialog();

                dialog.dismiss();
            }
        });
        btn_rating_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Rating", "", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
                dialog.dismiss();
            }
        });
        btn_distance_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationAvail != null && isLocationAvail.equals("Yes")) {
                    ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Distance", "", mLatlngCurrent, "");
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                    }
                    dialog.dismiss();
                } else {
//                    showToast(getResources().getString(R.string.toast_location_not_found));
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage(getResources().getString(R.string.dialog_need_your_permission));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

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
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
            }
        });
//        btn_cancel_sorting.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }


    @Override
    public void onResume() {
        if (locationSettings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(activity, SplashActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                activity.finishAffinity();
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Utils.isLocationServiceEnabled(activity)) {
                    Intent i = new Intent(activity, SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    activity.finishAffinity();
                }
            }
        }
        super.onResume();
    }


    /**
     * Handle shades of text view title of activity in multicolor
     */
    /*
    private void SetShaderToViews() {
//        Utils.gradientTextViewLong(tv_title_shops_list, activity);
    }*/

   private void setFilterDataToViews() {
        // create spinner list elements for Filter
        ArrayAdapter<String> adapterFilter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item);

        //added searching
        adapterFilter.add(getResources().getString(R.string.spinner_provided_warranty));
        adapterFilter.add(getResources().getString(R.string.spinner_provided_replace_parts));
        adapterFilter.add(getResources().getString(R.string.spinner_place_type));
        adapterFilter.add(getResources().getString(R.string.spinner_top_rated));
        adapterFilter.add(getResources().getString(R.string.spinner_distance));
        activity.aQuery.find(R.id.tv_providers_shop_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sp_providers_shop_list.performClick();
                if (mShopsListModel != null && mShopsListModel.getShopsList().size() > 0) {

                    Intent intent = new Intent(activity, FiltersActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
                    FiltersActivity.ShopsDataResponse = ShopsListDataResponse;
                    if (FilteredData != null && FilteredData.getExtras() != null) {
                        intent.putExtra("provide_warranty", provide_warranty);
                        intent.putExtra("provide_ReplaceParts", provide_ReplaceParts);
                        intent.putExtra("topRated", topRated);
                        intent.putExtra("placeType", placeType);
                        intent.putExtra("brandType", brandType);
                        intent.putExtra("serviceType", serviceType);
                        intent.putExtra("FilterRecord", FilterRecord);
                        intent.putIntegerArrayListExtra("CheckedBrands", CheckedBrands);
                        intent.putIntegerArrayListExtra("CheckedShopTypes", CheckedShopTypes);
                        intent.putIntegerArrayListExtra("CheckedServices", CheckedServices);
                    }
                    startActivityForResult(intent, 1);
                } else {

                }
            }
        });


        activity.aQuery.find(R.id.tv_sorting_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSortingDialog();
            }
        });

        //Searching with name in list
        et_search_shops_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activity.aQuery.find(R.id.iv_search_close_shops_list).visibility(View.VISIBLE);
                    activity.aQuery.find(R.id.iv_search_shops_list).visibility(View.GONE);
                } else {
                    activity.aQuery.find(R.id.iv_search_close_shops_list).visibility(View.GONE);
                    activity.aQuery.find(R.id.iv_search_shops_list).visibility(View.VISIBLE);
                }
                if (mShopsListModel != null) {
                    ShopsListRecycleViewAdapter.filterShopsName(s.toString(), mLatlngCurrent);
                }
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
//        aQuery.find(R.id.iv_search_shops_list).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                aQuery.find(R.id.lay_title_shops_list).visibility(View.GONE);
//                aQuery.find(R.id.lay_et_search_shops_list).visibility(View.VISIBLE);
//            }
//        });
       activity.aQuery.find(R.id.iv_search_close_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.aQuery.find(R.id.iv_search_shops_list).visibility(View.VISIBLE);
                activity.aQuery.find(R.id.iv_search_close_shops_list).visibility(View.GONE);
                et_search_shops_main.setText("");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }

            }
        });

    }

    private void getDataForView() {
      //  if (intent != null) {

          //  Bundle bundle = intent.getParcelableExtra("bundle");
            if (b != null) {
                mLatlngCurrent = b.getParcelable("LatLngCurrent");
            }
            mplaceName = b.getString("placeName");
        }


    private void setDataToView() {
        if (mplaceName != null && !mplaceName.equals("")) {
            activity.aQuery.find(R.id.tv_location_name_shops_list).text(mplaceName);
        }

    }


    }

