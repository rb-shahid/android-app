package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Google.Analytics.AnalyticsApplication;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Constants;
import co.dtechsystem.carefer.Utils.Validations;
import co.dtechsystem.carefer.Widget.ListSpinnerDialog;
import co.dtechsystem.carefer.services.FetchAddressIntentService;

@SuppressWarnings("unchecked")
public class ReceiveCarActivity extends Fragment {
    private static final int PLACE_PICKER_REQUEST = 1001;
    private DrawerLayout mDrawerLayout;


    private TextView tv_car_brand_my_details;
    private TextView tv_car_model_my_details;

    private TextView tv_brands;
    private final Calendar myCalendar = Calendar.getInstance();
    private final ArrayList<String> mBrandsIdArray = new ArrayList<>();
    private final ArrayList<String> mModelsIdArray = new ArrayList<>();
    private String mBrandsId;
    private String mModelsId;
    private boolean mModelData;
    private String sUser_ID;
    private boolean mBrandData;
    private ArrayList <String>brands = new ArrayList<String>();
    private ArrayList <String> models = new ArrayList();
    ArrayAdapter<String> brandAdapter ;
    ArrayAdapter<String> modelsAdapter ;
    private boolean firstBrand = true;
    private boolean firstModel = true;
    private boolean firstService = true;
    private ArrayAdapter StringModeldataAdapter;
    private LatLng latLng;
    private String mAddress;
    private MainActivity activity;
    private View view;

    public ReceiveCarActivity() {
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
        view = inflater.inflate(R.layout.activity_receive_car, container, false);

        setUpToolbar();

        initializeViews();

//        SetUpLeftbar();

        //SetSpinnerListener();

        getbrandsfromserver();

        startGetLocationIntentService();

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == activity.RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(getActivity(),data);
                latLng=selectedPlace.getLatLng();
                mAddress= selectedPlace.getAddress().toString();
                activity.aQuery.id(R.id.tv_address).text(selectedPlace.getAddress());
            }
        }
    }

    private void initializeViews() {



        tv_car_brand_my_details = (TextView) view.findViewById(R.id.tv_car_brand_my_details);
        tv_car_model_my_details = (TextView) view.findViewById(R.id.tv_car_model_my_details);

        tv_brands = (TextView) view.findViewById(R.id.et_car_brand_my_details);
        tv_brands.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (brands == null || brands.isEmpty()) {
                    getbrandsfromserver();
                } else{
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                    View parentView = activity.getLayoutInflater().inflate(R.layout.lay_dialog_list_filter , null);
                    TextView service_type = (TextView) parentView.findViewById(R.id.service_type);
                    Button searchServiceBtn = (Button) parentView.findViewById(R.id.searchServiceBtn);
                    ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);
                    brandAdapter = new ArrayAdapter<String>( getActivity() , R.layout.list_bottom_sheet_view ,R.id.row , brands );
                    service_type.setText(R.string.ph_brand);
                    lv_filter_list.setAdapter(brandAdapter );
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



//                    ListSpinnerDialog dialog = new ListSpinnerDialog(getActivity(), brands);
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            int selected=((ListSpinnerDialog) dialog).getSelected();
//                            if (selected>=0)
//                            {
//                                tv_brands.setText(brands.get(selected));
//                                if (Validations.isInternetAvailable(activity, true)) {
//                                    activity.loading.show();
//                                    APiGetBrandsServiceModelsData(AppConfig.APiGetBrandModels, "ModelYear", mBrandsIdArray.get(selected));
//                                    activity.aQuery.find(R.id.et_car_model_my_details).text("");
//
//                                }
//                                mBrandsId = mBrandsIdArray.get(selected);
//                                mModelsId="";
//                            }
//                        }
//                    });
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.show();
                }

                // mSpinner_brands.showDialog();

            }
        });
        activity.aQuery.id(R.id.et_car_model_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brands == null || models.isEmpty()) {
                    getbrandsfromserver();
                } else{


                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                    View parentView = activity.getLayoutInflater().inflate(R.layout.lay_dialog_list_filter , null);
                    TextView service_type = (TextView) parentView.findViewById(R.id.service_type);
                    Button searchServiceBtn = (Button) parentView.findViewById(R.id.searchServiceBtn);
                    final ListView lv_filter_list = (ListView) parentView.findViewById(R.id.lv_filter_list);

                    modelsAdapter =  new ArrayAdapter<String>( getActivity() , R.layout.list_bottom_sheet_view , R.id.row , models );
                    service_type.setText(R.string.ph_model);
                    lv_filter_list.setAdapter(modelsAdapter);
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

//                    ListSpinnerDialog dialog = new ListSpinnerDialog(getActivity(), models);
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            int selected=((ListSpinnerDialog) dialog).getSelected();
//                            if (selected>=0)
//                            {
//                                activity.aQuery.find(R.id.et_car_model_my_details).text(models.get(selected));
//                                mModelsId = mModelsIdArray.get(selected);
//
//                            }
//                        }
//                    });
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.show();
                }

            }
        });

        activity.aQuery.id(R.id.tv_edit_location).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

//        SetShaderToViews();



    }
    private void getbrandsfromserver(){
        if (Validations.isInternetAvailable(activity, true)) {
            activity.loading.show();
            activity.loading2.show();
            //  APiMyDetails(AppConfig.APiGetCustomerDetails + sUser_ID, "getUserDetails", "", "", "", "", "", "", "");
            APiGetBrandsServiceModelsData(AppConfig.APiBrandData, "Brands", "");
            APIgetDescription(AppConfig.APiGetReceiveCarDesc);
        }
    }


    private void APiGetBrandsServiceModelsData(final String Url, final String Type, final String BrandId) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        //mBrandsIdArray.clear();
                        mModelsIdArray.clear();
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

                                //@SuppressWarnings("unchecked")
                                //ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, R.layout.lay_spinner_item, brands);

                                //aQuery.id(R.id.sp_brand_type_shop_details_order).adapter(StringdataAdapterbrands);

                                //StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, models);
                                //aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                // aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                               // mSpinner_brands.setList(brands.toArray(new CharSequence[brands.size()]));
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
                                //mSpinner_models.setList(models.toArray(new CharSequence[models.size()]));
                                //aQuery.find(R.id.et_car_model_my_details).text(getResources().getString(R.string.dp_model));
                                activity.loading.close();

                            }
                            //SetSpinnerListener();
                            activity.loading.close();
                        } catch (JSONException e) {
                            activity.loading.close();
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
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

     /**
     * Handle shades of text view title of activity in multicolor
     */
//    private void SetShaderToViews() {
//
//        Utils.gradientTextViewShort(tv_car_brand_my_details, activity);
//        Utils.gradientTextViewShort(tv_car_model_my_details, activity);
//        Utils.gradientTextViewShort(activity.aQuery.id(R.id.tv_problem_desc).getTextView(), activity);
//        Utils.gradientTextViewShort(activity.aQuery.id(R.id.tv_location).getTextView(), activity);
//
//    }

//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) this );
//    }
//
//
//    @SuppressWarnings("UnusedParameters")
//    @SuppressLint("RtlHardcoded")
//    public void btn_drawyerMenuOpen(View v) {
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
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
//
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
//            Intent i = new Intent(this, AboutUsActivity.class);
//            startActivity(i);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
//        return true;
//    }

    /**
     * compares the starting of the brands with the given string
     * @param sequence
     * @return
     */
    ArrayList<String> getSearchResult(CharSequence sequence){
        ArrayList <String> resultant = new ArrayList<String>();
        String seq = sequence.toString();
        for(String brand: brands){
            if(brand.startsWith(seq))
                resultant.add(brand);
        }

        return resultant;
    }


    public void submitUserData(View view){
       if(validateEntries()){
           activity.loading.show();
           String userComments=activity.aQuery.id(R.id.et_problem_desc).getEditText().getText().toString();

           APiSendReceiveCarOrder(AppConfig.APiSaveOrder,mBrandsId,mModelsId,userComments,sUser_ID,""+latLng.latitude,""+latLng.longitude,mAddress);

       }

    }

    private boolean validateEntries() {

        if (mBrandsId == null || mBrandsId.isEmpty()) {
            Toast.makeText(getActivity(), R.string.toast_select_brand_type, Toast.LENGTH_LONG).show();
            return false;

        }
        if (mModelsId  == null || mModelsId.isEmpty()) {
            Toast.makeText(getActivity(), R.string.toast_select_model_type, Toast.LENGTH_LONG).show();
            return false;

        }

        if (latLng == null) {
            if (mLastLocation==null){
            Toast.makeText(getActivity(), R.string.select_location, Toast.LENGTH_LONG).show();
            return false;}
            else {
                latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            }
        }

        return true;

    }

    /**
     *
     * @param Url
     */
    private void APiSendReceiveCarOrder(final String Url, final String brandID, final String modelId, final String comments, final String customerId, final String lat, final String lng, final String address) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int morderID = jsonObject.getInt("orderID");
                            if (morderID != 0) {
                                orderPlaced(""+morderID);
                            }


                            // aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                            activity.loading.close();


                        } catch (JSONException e) {
                            activity.loading.close();
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        AnalyticsApplication.getInstance().trackException(error);
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
                params.put("shopID","0");
                params.put("comment",comments);
                params.put("brandId",brandID);
                params.put("customerID",customerId);
                params.put("modelId",modelId);
                params.put("orderServiceType","receivedCar");
                params.put("orderStatus","1");
                params.put("address",address);
                params.put("lat",lat);
                params.put("lng",lng);
                return params;
            }
        };

// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    /**
     * this function will print the orderid on a dialog and will finish the activity on dismiss as well
     * @param orderId
     */
    private void orderPlaced(String orderId){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(getResources().getString(R.string.toast_order_placed)+" "+"0000"+ orderId);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
//                ReceiveCarActivity.this.finish();
            }
        });
        alertDialog.show();;
    }
    public void APIgetDescription(String Url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            String description = jsonObject.getString("description");
                            if (description != null) {
//                                activity.aQuery.id(R.id.tv_moved_shop_receive_car_description).text(description);
                            }

                            // aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                            activity.loading2.close();

                        } catch (JSONException e) {
                            activity.loading2.close();
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading2.close();
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


                return params;
            }
        };
        queue.add(postRequest);
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            mAddress=mAddressOutput;
            displayAddressOutput(mAddressOutput);

        }
    }

    private void displayAddressOutput(String address) {
        activity.aQuery.id(R.id.tv_address).text(address);

    }

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;

    protected void startGetLocationIntentService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }else{
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                mLastLocation=location;
                                Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
                                mResultReceiver = new AddressResultReceiver(new Handler());
                                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                                intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
                                activity.startService(intent);

                            }else{
                                Toast.makeText(activity,getString(R.string.toast_location_not_found),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_receive_car);

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
