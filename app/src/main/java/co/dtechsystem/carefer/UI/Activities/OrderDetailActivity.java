package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import co.dtechsystem.carefer.Google.Analytics.AnalyticsApplication;
import co.dtechsystem.carefer.Models.OrderDetailModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

public class OrderDetailActivity extends Fragment {

   // private DrawerLayout mDrawerLayout;
    private TextView tv_title_order_detail;
    private AQuery aq;
    private ImageButton ib;

    View view;
    public MainActivity activity;

    private boolean fromstack;

    public OrderDetailActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate( R.layout.activity_order_detail, container, false );

            initialise();
//            SetUpLeftbar();
//            SetShaderToViews();
           setUpToolbar();

            if (Validations.isInternetAvailable(activity, true)) {
                activity.loading.show();
                ApiGetOrderDetails(activity.getIntent().getExtras().getString("orderID"));
            }
        }catch (Exception e){

            AnalyticsApplication.getInstance().trackException(e);
            e.printStackTrace();
            activity.SendFireBaseError(String.valueOf(e));
        }
return view;

    }


    private void initialise(){
        aq = new AQuery(getActivity());
      //  tv_title_order_detail = (TextView) view.findViewById(R.id.tv_title_order_detail);

        aq.id(R.id.tv_order_no_heading).text(activity.getResources().getString(R.string.tv_order_no));
        aq.id(R.id.tv_order_type_heading).text(activity.getResources().getString(R.string.tv_order_type));
        aq.id(R.id.tv_order_date_heading).text(activity.getResources().getString(R.string.tv_date_order));
        aq.id(R.id.tv_comment_heading).text(activity.getResources().getString(R.string.tv_receive_comment));
        aq.id(R.id.tv_moved_shop_heading).text(activity.getResources().getString(R.string.moved_shop_price));
        aq.id(R.id.tv_model_name_heading).text(activity.getResources().getString(R.string.tv_model_name));
        aq.id(R.id.tv_brand_heading).text(activity.getResources().getString(R.string.tv_brand_name));
        aq.id(R.id.tv_service_type_heading).text(activity.getResources().getString(R.string.tv_service_type));
        aq.id(R.id.tv_type_heading).text(activity.getResources().getString(R.string.tv_type));
        aq.id(R.id.tv_shop_heading).text(activity.getResources().getString(R.string.tv_shop));
        aq.id(R.id.tv_location_heading).text(activity.getResources().getString(R.string.tv_location));
//        SetUpLeftbar();
//        SetShaderToViews();

    }


//    //set up left menu bar to show user menu
//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) getActivity() );
//
//    }
//
//    private void SetShaderToViews() {
//        Utils.gradientTextViewLong(tv_title_order_detail, activity);
//
//    }

    //Opens the drawyer menu/leftbar
    @SuppressWarnings("UnusedParameters")
//    public void btn_drawyerMenuOpen(View v) {
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            activity.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        activity.getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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
//            // get menu from navigationView
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


    private void ApiGetOrderDetails(String orderID){

        String url = AppConfig.APIGetOrderDetails + orderID;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jObject) {

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        OrderDetailModel orderDetailModelList = gson.fromJson(String.valueOf(jObject), OrderDetailModel.class);

                        try{

                            if(orderDetailModelList.getOrderDetails().get(0).getOrderNo().isEmpty()){
                                aq.id(R.id.tv_order_no).text("NA");
                                aq.id(R.id.order_no_layout).visibility(View.GONE);
                                aq.id(R.id.order_no_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_order_no).text("0000"+orderDetailModelList.getOrderDetails().get(0).getOrderNo());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getOrderType().isEmpty()){
                                aq.id(R.id.tv_order_type).text("NA");
                                aq.id(R.id.order_type_layout).visibility(View.GONE);
                                aq.id(R.id.order_type_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_order_type).text(orderDetailModelList.getOrderDetails().get(0).getOrderType());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getOrderDate().isEmpty()){
                                aq.id(R.id.tv_order_date).text("NA");
                                aq.id(R.id.order_date_layout).visibility(View.GONE);
                                aq.id(R.id.order_date_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_order_date).text(orderDetailModelList.getOrderDetails().get(0).getOrderDate());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getReceiveCarComments().isEmpty()){
                                aq.id(R.id.tv_comment).text("NA");
                                aq.id(R.id.comment_layout).visibility(View.GONE);
                                aq.id(R.id.comment_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_comment).text(orderDetailModelList.getOrderDetails().get(0).getReceiveCarComments());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getMovedShopPrice().isEmpty()){
                                aq.id(R.id.tv_moved_shop).text("NA");
                                aq.id(R.id.moved_shop_layout).visibility(View.GONE);
                                aq.id(R.id.moved_shop_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_moved_shop).text(orderDetailModelList.getOrderDetails().get(0).getMovedShopPrice());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getModelName().isEmpty()){
                                aq.id(R.id.tv_model_name).text("NA");
                                aq.id(R.id.model_name_layout).visibility(View.GONE);
                                aq.id(R.id.model_name_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_model_name).text(orderDetailModelList.getOrderDetails().get(0).getModelName());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getBrandName().isEmpty()){
                                aq.id(R.id.tv_brand).text("NA");
                                aq.id(R.id.brand_layout).visibility(View.GONE);
                                aq.id(R.id.brand_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_brand).text(orderDetailModelList.getOrderDetails().get(0).getBrandName());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getServiceTypeName().isEmpty()){
                                aq.id(R.id.tv_service_type).text("NA");
                                aq.id(R.id.service_type_layout).visibility(View.GONE);
                                aq.id(R.id.service_type_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_service_type).text(orderDetailModelList.getOrderDetails().get(0).getServiceTypeName());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getType().isEmpty()){
                                aq.id(R.id.tv_type).text("NA");
                                aq.id(R.id.type_layout).visibility(View.GONE);
                                aq.id(R.id.type_view).visibility(View.GONE);
                            }else{
                                if(orderDetailModelList.getOrderDetails().get(0).getType().equals("Receive Car")){
                                    aq.id(R.id.tv_type).text(getResources().getString(R.string.title_receive_car));
                                }else{
                                    aq.id(R.id.tv_type).text(getResources().getString(R.string.title_moved_shop));
                                }
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getShopName().isEmpty()){
                                aq.id(R.id.tv_shop_name).text("NA");
                                aq.id(R.id.shop_name_layout).visibility(View.GONE);
                                aq.id(R.id.shop_name_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_shop_name).text(orderDetailModelList.getOrderDetails().get(0).getShopName());
                            }

                            if(orderDetailModelList.getOrderDetails().get(0).getCustomerLocation().isEmpty()){
                                aq.id(R.id.tv_location).text("NA");
                                aq.id(R.id.location_layout).visibility(View.GONE);
                                aq.id(R.id.location_view).visibility(View.GONE);
                            }else{
                                aq.id(R.id.tv_location).text(orderDetailModelList.getOrderDetails().get(0).getCustomerLocation());
                            }


                        }catch (Exception e){
                            AnalyticsApplication.getInstance().trackException(e);
                            e.printStackTrace();
                            activity.SendFireBaseError(String.valueOf(e));
                        }

                        activity.loading.close();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        activity.loading.close();
                        activity.SendFireBaseError(String.valueOf(error));
                    }
                });

        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonRequest.setRetryPolicy(policy);

        Volley.newRequestQueue(getActivity()).add(jsonRequest);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_order_detail);

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
