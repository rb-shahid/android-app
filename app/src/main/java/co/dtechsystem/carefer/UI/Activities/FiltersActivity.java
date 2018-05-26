package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.Filter.ShopsFilterClass;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

@SuppressWarnings("unchecked")
public class FiltersActivity extends Fragment {
    private ExpandableListView lv_service_type;
    private ExpandableListView lv_brands;
    private ExpandableListView lv_place_type;

    public static String ShopsDataResponse = "";

    private ShopsListModel mShopsListModel;

    private SwitchCompat sw_provide_warranty_filter;
    private SwitchCompat sw_provide_replace_parts_filter;
    private SwitchCompat sw_top_rated_filter;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistBeforeFiltration;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration;
    private String provide_warranty = "";
    private String provide_ReplaceParts = "";
    private String topRated = "";
    private String placeType = "";
    private String brandType = "";
    private String serviceType = "";
    private ArrayAdapter<String> arrayAdapterPlaceType;
    private ArrayAdapter<String> arrayAdapterBrands;
    private ArrayAdapter<String> arrayAdapterServices;
    private int TotalRecord;
    private int FilterRecord;
    private LatLng mLatlngCurrent;
    private ArrayList<Integer> CheckedServices = new ArrayList<>();
    private ArrayList<Integer> CheckedBrands = new ArrayList<>();
    private ArrayList<Integer> CheckedShopTypes = new ArrayList<>();
    private TextView tv_title_filter;

    private MainActivity activity;
    private View view;

    public FiltersActivity() {
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
        view= inflater.inflate(R.layout.activity_filters, container, false);

        setUpToolbar();

//        SetDragAbleFilterViews();
//        if (Validations.isInternetAvailable(activity, true)) {
//            activity.loading.show();
//            APiGetShopslistData(AppConfig.APiGetFilterTypes);
//        }
        return view;
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */


    /**
     * Handle all type of intents like receiving strings and bundles from previous activity
     */
    private void GetIntents() {
        if (activity.intent.getStringExtra("provide_warranty") != null && activity.intent.getStringExtra("provide_ReplaceParts") != null &&
                activity.intent.getStringExtra("topRated") != null && activity.intent.getStringExtra("placeType") != null && activity.intent.getStringExtra("brandType") != null && activity.intent.getStringExtra("serviceType") != null && activity.intent.getStringExtra("FilterRecord") != null &&
                activity.intent.getIntegerArrayListExtra("CheckedServices") != null && activity.intent.getIntegerArrayListExtra("CheckedBrands") != null &&
                activity.intent.getIntegerArrayListExtra("CheckedShopTypes") != null) {
            provide_warranty = activity.intent.getStringExtra("provide_warranty");
            provide_ReplaceParts = activity.intent.getStringExtra("provide_ReplaceParts");
            topRated = activity.intent.getStringExtra("topRated");
            placeType = activity.intent.getStringExtra("placeType");
            brandType = activity.intent.getStringExtra("brandType");
            serviceType = activity.intent.getStringExtra("serviceType");
            CheckedServices = activity.intent.getIntegerArrayListExtra("CheckedServices");
            CheckedBrands = activity.intent.getIntegerArrayListExtra("CheckedBrands");
            CheckedShopTypes = activity.intent.getIntegerArrayListExtra("CheckedShopTypes");
            FilterRecord = Integer.parseInt(activity.intent.getStringExtra("FilterRecord"));
            if (provide_warranty.equals("1")) {
                sw_provide_warranty_filter.setChecked(true);
            }
            if (provide_ReplaceParts.equals("1")) {
                sw_provide_replace_parts_filter.setChecked(true);
            }
            if (topRated.equals("5.00")) {
                sw_top_rated_filter.setChecked(true);
            }

            if (!placeType.equals("")) {
                activity.aQuery.find(R.id.tv_place_type_filter).text(placeType);


            }
            if (!brandType.equals("")) {
                activity.aQuery.find(R.id.tv_brand_type_filter).text(brandType);
            }
            if (!serviceType.equals("")) {
                activity.aQuery.find(R.id.tv_service_type_filter).text(serviceType);
            }
            if (FilterRecord < 20) {
                TotalRecord = _ShopslistBeforeFiltration.size();
                activity.aQuery.find(R.id.tv_total_record_found_filter).text(FilterRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
            } else {
                TotalRecord = _ShopslistBeforeFiltration.size();
                activity.aQuery.find(R.id.tv_total_record_found_filter).text(FilterRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
            }
        } else {
            if (_ShopslistBeforeFiltration.size() > 0) {
                TotalRecord = _ShopslistBeforeFiltration.size();
                FilterRecord = TotalRecord;
                activity.aQuery.find(R.id.tv_total_record_found_filter).text(TotalRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                if (FilterRecord < 20) {
                    activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                    activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                } else {
                    activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                    activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                }
            }
        }
    }

    /**
     * Handle list view for filter like brands,service and shop type and initializes their views
     */
    private void SetDragAbleFilterViews() {
        lv_service_type = (ExpandableListView) view.findViewById(R.id.lv_service_type);
        lv_brands = (ExpandableListView) view.findViewById(R.id.lv_brands);
        lv_place_type = (ExpandableListView) view.findViewById(R.id.lv_place_type);
        sw_provide_warranty_filter = (SwitchCompat) view.findViewById(R.id.sw_provide_warranty_filter);
        sw_provide_replace_parts_filter = (SwitchCompat) view.findViewById(R.id.sw_provide_replace_parts_filter);
        sw_top_rated_filter = (SwitchCompat) view.findViewById(R.id.sw_top_rated_filter);

//        ShopsDataResponse = intent.getStringExtra("ShopsDataResponse");
        Bundle bundle = activity.intent.getParcelableExtra("bundle");
        if (bundle != null) {
            mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
        }
        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();

        GetIntents();
    }

    /**
     * Handle first call and recall after setting filter and coming back to filter activity
     */


//    commmmmmment
    private void SetFiltersToViews() {

        sw_provide_warranty_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    provide_warranty = "1";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                } else {
                    provide_warranty = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

                }
            }
        });

        sw_provide_replace_parts_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    provide_ReplaceParts = "1";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

                } else {
                    provide_ReplaceParts = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                }
            }
        });

        sw_top_rated_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    topRated = "5.00";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

                } else {
                    topRated = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                }
            }
        });
        lv_place_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    TextView textView = (TextView) view.findViewById(R.id.lblListItem);
                placeType = parent.getItemAtPosition(position).toString();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activity.aQuery.find(R.id.tv_place_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterPlaceTypeListViewDialog();
            }
        });
        activity.aQuery.find(R.id.tv_brand_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterBrandsListViewDialog();
            }
        });

        activity.aQuery.find(R.id.tv_service_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterServicesListViewDialog();
            }
        });


    }

    /**
     * Web APi for fetch all shops data
     * @param Url Takes string as param to fetch all shops data
     */
    private void APiGetShopslistData(final String Url) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressWarnings("IfCanBeSwitch")
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            List listservices = new ArrayList();
                            //noinspection unchecked
//                            listservices.add(0, getResources().getString(R.string.dp_service_type));
                            List brands = new ArrayList();
                            List placeType = new ArrayList();
                            //noinspection unchecked
//                            brands.add(0, getResources().getString(R.string.dp_brand));
                            JSONArray serviceTypeData = response.getJSONArray("serviceTypeData");
                            for (int i = 0; i < serviceTypeData.length(); i++) {
                                JSONObject jsonObject = serviceTypeData.getJSONObject(i);
                                //noinspection unchecked
                                listservices.add(jsonObject.getString("serviceTypeName"));
                            }
//
                            JSONArray brandsData = response.getJSONArray("brandsData");
                            for (int i = 0; i < brandsData.length(); i++) {
                                JSONObject jsonObject = brandsData.getJSONObject(i);
                                //noinspection unchecked
                                brands.add(jsonObject.getString("brandName"));
                            }

                            JSONArray placeTypeData = response.getJSONArray("placeType");
                            placeType.add(0, getResources().getString(R.string.spinner_place_type));
                            for (int i = 0; i < placeTypeData.length(); i++) {
                                JSONObject jsonObject = placeTypeData.getJSONObject(i);
                                //noinspection unchecked
                                placeType.add(jsonObject.getString("name"));
                            }
                            arrayAdapterServices = new ArrayAdapter<>(getActivity(),
                                    R.layout.list_bottom_sheet_view , R.id.row , listservices);
                            arrayAdapterBrands = new ArrayAdapter<>(getActivity(),
                                    R.layout.list_bottom_sheet_view, R.id.row , brands);
                            arrayAdapterPlaceType = new ArrayAdapter<>(getActivity(),
                                    R.layout.list_bottom_sheet_view, R.id.row , placeType);
                            SetFiltersToViews();

                            mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                            _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                            if (_ShopslistBeforeFiltration.size() > 0) {
                                TotalRecord = _ShopslistBeforeFiltration.size();
                                FilterRecord = TotalRecord;
                                activity.aQuery.find(R.id.tv_total_record_found_filter).text(TotalRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                            }
                            activity.loading.close();

                        } catch (JSONException e) {
                            activity.loading.close();
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                            activity.SendFireBaseError(String.valueOf(e));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        // add it to the RequestQueue

        queue.add(getRequest);
    }

    /**
     * Show user a dialog of place types list where use choose for types
     */

    private void ShowFilterPlaceTypeListViewDialog() {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View parentView = activity.getLayoutInflater().inflate(R.layout.lay_dialog_list_filter , null);
        TextView service_type = (TextView) parentView.findViewById(R.id.service_type);
        Button searchServiceBtn = (Button) parentView.findViewById(R.id.searchServiceBtn);
        final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);
        service_type.setText(R.string.spinner_place_type);
        lv_filter_list.setAdapter(arrayAdapterPlaceType);
        for (int i = 0; i < CheckedShopTypes.size(); i++) {

            if (CheckedShopTypes != null && CheckedShopTypes.size() > 0) {
                lv_filter_list.setItemChecked(CheckedShopTypes.get(i), true);
            }
        }

//        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv_filter_list.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById( R.id.row );
                tv.setTextColor( getContext().getResources().getColor( R.color.colorAccent ) );
                tv.setCompoundDrawablesWithIntrinsicBounds( getContext().getResources().getDrawable(  R.drawable.check ) , null,null,null );

            }
        });
        bottomSheetDialog.setContentView(parentView);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , 400 , getResources().getDisplayMetrics()));
        bottomSheetDialog.show();


        // custom dialog
//        final Dialog dialog = new Dialog(activity);
//        dialog.setContentView(R.layout.lay_dialog_list_filter);
//        dialog.setTitle(getResources().getString(R.string.app_name));
//        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
//        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
//        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
//        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);



//        final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);
//        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        lv_filter_list.setAdapter(arrayAdapterPlaceType);


        searchServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = lv_filter_list.getCheckedItemPosition();
                if (id > 0) {
                    CheckedShopTypes.add(id);
                    activity.aQuery.find(R.id.tv_place_type_filter).text(lv_filter_list.getItemAtPosition(id).toString());
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }

                    placeType = lv_filter_list.getItemAtPosition(id).toString();
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
//                    dialog.hide();
                    bottomSheetDialog.hide();
                } else {
                    if (id != -1) {
                        CheckedShopTypes.add(id);
                        activity.aQuery.find(R.id.tv_place_type_filter).text(lv_filter_list.getItemAtPosition(id).toString());

                        placeType = "";
                        if (_ShopslistAfterFiltration != null) {
                            _ShopslistAfterFiltration.clear();
                        }
                        if (_ShopslistBeforeFiltration.size() == 0) {
                            mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                            _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                        }
                        _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                                _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                        activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                        FilterRecord = _ShopslistAfterFiltration.size();
                        if (FilterRecord < 20) {
                            activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                            activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                        } else {
                            activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                            activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                        }
//                        dialog.hide();
                        bottomSheetDialog.hide();
                    }
                }
            }
        });



//        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.hide();
//            }
//        });

//        dialog.show();
        bottomSheetDialog.show();
    }
    /**
     * Show user a dialog of brands list where use choose for types
     */

    private void ShowFilterBrandsListViewDialog() {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View parentView = activity.getLayoutInflater().inflate(R.layout.lay_dialog_list_filter , null);
        TextView service_type = (TextView) parentView.findViewById(R.id.service_type);
        Button searchServiceBtn = (Button) parentView.findViewById(R.id.searchServiceBtn);
        final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);

        service_type.setText(R.string.ph_brand);
        lv_filter_list.setAdapter(arrayAdapterBrands);
        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv_filter_list.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById( R.id.row );
                tv.setTextColor( getContext().getResources().getColor( R.color.colorAccent ) );
                tv.setCompoundDrawablesWithIntrinsicBounds( getContext().getResources().getDrawable(  R.drawable.check ) , null,null,null );

            }
        });
        bottomSheetDialog.setContentView(parentView);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , 400 , getResources().getDisplayMetrics()));
        bottomSheetDialog.show();



        // custom dialog
//        final Dialog dialog = new Dialog(activity);
//        dialog.setContentView(R.layout.lay_dialog_list_filter);
//        dialog.setTitle(getResources().getString(R.string.app_name));
//        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
//        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
//        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
//        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);
//        final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);
//        lv_filter_list.setAdapter(arrayAdapterBrands);
//        for (int i = 0; i < CheckedBrands.size(); i++) {
//            if (CheckedBrands != null && CheckedBrands.size() > 0) {
//                lv_filter_list.setItemChecked(CheckedBrands.get(i), true);
//            }
//        }
        searchServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                ArrayList<String> brands = new ArrayList<>();
                CheckedBrands.clear();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {

                        long id = lv_filter_list.getItemIdAtPosition(i);
//                        if (id != 0) {
                        CheckedBrands.add((int) id);
                        brands.add(lv_filter_list.getItemAtPosition(i).toString());
//                        }
                    }

                }
                String Brands = brands.toString();
                if (Brands.startsWith("[") && Brands.endsWith("]")) {
                    Brands = Brands.replace("[", "");
                    Brands = Brands.replace("]", "");
                }
                if (brands.size() > 0) {
                    activity.aQuery.find(R.id.tv_brand_type_filter).text(Brands);
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel =activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }


                    brandType = Brands;
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
//                    dialog.hide();
                    bottomSheetDialog.hide();
                } else {
                    CheckedBrands.clear();
                    activity.aQuery.find(R.id.tv_brand_type_filter).text(getResources().getString(R.string.dp_brand));

                    brandType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
//                    dialog.hide();
                    bottomSheetDialog.hide();                }
            }
        });


//        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.hide();
//            }
//        });

//                    dialog.hide();
        bottomSheetDialog.hide();
    }
    /**
     * Show user a dialog of services list where use choose for types
     */
    private void ShowFilterServicesListViewDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View parentView = activity.getLayoutInflater().inflate(R.layout.lay_dialog_list_filter , null);
        TextView service_type = (TextView) parentView.findViewById(R.id.service_type);
        Button searchServiceBtn = (Button) parentView.findViewById(R.id.searchServiceBtn);
        final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);

        service_type.setText(R.string.ph_service_type);
        lv_filter_list.setAdapter(arrayAdapterBrands);
        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv_filter_list.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById( R.id.row );
                tv.setTextColor( getContext().getResources().getColor( R.color.colorAccent ) );
                tv.setCompoundDrawablesWithIntrinsicBounds( getContext().getResources().getDrawable(  R.drawable.check ) , null,null,null );

            }
        });
        bottomSheetDialog.setContentView(parentView);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , 400 , getResources().getDisplayMetrics()));
        bottomSheetDialog.show();



        // custom dialog
//        final Dialog dialog = new Dialog(activity);
//        dialog.setContentView(R.layout.lay_dialog_list_filter);
//        dialog.setTitle(getResources().getString(R.string.app_name));
//        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
//        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
//        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
//        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);

//        for (int i = 0; i < CheckedServices.size(); i++) {
//            if (CheckedServices != null && CheckedServices.size() > 0) {
//                lv_filter_list.setItemChecked(CheckedServices.get(i), true);
//            }
//        }

        searchServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                CheckedServices.clear();
                ArrayList<String> services = new ArrayList<>();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        long id = lv_filter_list.getItemIdAtPosition(i);
//                        if (id != 0) {
                        CheckedServices.add((int) id);
                        services.add(lv_filter_list.getItemAtPosition(i).toString());
//                        }
                    }
                }
                String Services = services.toString();
                if (Services.startsWith("[") && Services.endsWith("]")) {
                    Services = Services.replace("[", "");
                    Services = Services.replace("]", "");
                }
                if (services.size() > 0) {
                    activity.aQuery.find(R.id.tv_service_type_filter).text(Services);
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }


                    serviceType = Services;
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
//                    dialog.hide();
                    bottomSheetDialog.hide();
                } else {
//                    services.clear();
                    CheckedServices.clear();
                    activity.aQuery.find(R.id.tv_service_type_filter).text(getResources().getString(R.string.dp_service_type));
                    serviceType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = activity.gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

//                    dialog.hide();
                    bottomSheetDialog.hide();
                }
            }
        });

//        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.hide();
//            }
//        });

        //                    dialog.hide();
        bottomSheetDialog.hide();
    }

    /**
     * Handle button click when user click on apply filter
     * @param v
     */
    public void setApplyFilterBtnClick(View v) {
        ShopsListActivity shopsListActivity = new ShopsListActivity();
        if (_ShopslistAfterFiltration != null && _ShopslistAfterFiltration.size() > 0) {
            ArrayList<String> ShopsIds = new ArrayList<>();
            for (int i = 0; i < _ShopslistAfterFiltration.size(); i++) {
                ShopsIds.add(_ShopslistAfterFiltration.get(i).getID());
            }

//            JSONArray jsonObject = new JSONArray(_ShopslistAfterFiltration);

//            ShopsListRecycleViewAdapter mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, _ShopslistBeforeFiltration, mLatlngCurrent);
//            shopsListActivity.SetFilters(mshopsListRecycleViewAdapter);
//            finish();
            Intent intent = new Intent();
            intent.putStringArrayListExtra("ShopslistAfterFiltration", ShopsIds);
            ShopsListActivity.ShopsListDataResponse = ShopsDataResponse;
//            intent.putExtra("response", ShopsDataResponse);
            intent.putExtra("provide_warranty", provide_warranty);
            intent.putExtra("provide_ReplaceParts", provide_ReplaceParts);
            intent.putExtra("topRated", topRated);
            intent.putExtra("placeType", placeType);
            intent.putExtra("brandType", brandType);
            intent.putExtra("serviceType", serviceType);
            intent.putExtra("FilterRecord", String.valueOf(FilterRecord));
            intent.putIntegerArrayListExtra("CheckedBrands", CheckedBrands);
            intent.putIntegerArrayListExtra("CheckedShopTypes", CheckedShopTypes);
            intent.putIntegerArrayListExtra("CheckedServices", CheckedServices);
            activity.setResult(activity.RESULT_OK, intent);
            activity.finish();
        } else {
            activity.showToast(getResources().getString(R.string.no_record_found));
//            finish();
        }

    }
    /**
     * Handle button click when user click on reset filter
     * @param v
     */
    public void setResetFilterBtnClick(View v) {
        provide_warranty = "";
        provide_ReplaceParts = "";
        topRated = "";
        placeType = "";
        brandType = "";
        serviceType = "";
        _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
        activity.aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
        FilterRecord = _ShopslistAfterFiltration.size();
        if (FilterRecord < 20) {
            activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
            activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
        } else {
            activity.aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
            activity.aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
        }
        sw_provide_warranty_filter.setChecked(false);
        sw_provide_replace_parts_filter.setChecked(false);
        sw_top_rated_filter.setChecked(false);
        activity.aQuery.find(R.id.tv_place_type_filter).text(getResources().getString(R.string.spinner_place_type));
        activity.aQuery.find(R.id.tv_brand_type_filter).text(getResources().getString(R.string.dp_brand));
        activity.aQuery.find(R.id.tv_service_type_filter).text(getResources().getString(R.string.dp_service_type));
    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.tv_filter);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();


            }
        });

        final ImageButton refresh=(ImageButton) activity.findViewById(R.id.toolbar_refresh);
        refresh.setVisibility(View.VISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }

}
