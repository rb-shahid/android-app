package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import co.dtechsystem.carefer.Adapters.MyOrdersRecycleViewAdapter;
import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MyOrdersActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private MyOrdersRecycleViewAdapter mMyOrdersRecycleViewAdapter;
    private TextView tv_title_my_orders;
    String callType = "";
    private RecyclerView myOrdersRecylerView;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        myOrdersRecylerView = (RecyclerView) findViewById(R.id.rv_my_orders);
        tv_title_my_orders = (TextView) findViewById(R.id.tv_title_my_orders);
//        if (intent.getExtras() != null) {
//            callType = intent.getStringExtra("callType");
//        } else {
//            callType = "";
//        }
        SetShaderToViews();
        SetUpLeftbar();

    }

    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextViewShort(tv_title_my_orders, activity);
    }

    private void SetListData() {
        myOrdersRecylerView.getItemAnimator().setChangeDuration(700);
        myOrdersRecylerView.setAdapter(mMyOrdersRecycleViewAdapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(this, 1);
        myOrdersRecylerView.setLayoutManager(mgridLayoutManager);
    }

    @Override
    protected void onResume() {
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            APiGetMyOrdersListData();
        }
        super.onResume();
    }

    private void APiGetMyOrdersListData() {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiMyOrdersList + sUser_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        MyOrdersModel mMyOrdersModel = gson.fromJson(response.toString(), MyOrdersModel.class);
                        if (mMyOrdersModel.getOrdersData() != null && mMyOrdersModel.getOrdersData().size() > 0) {
                            mMyOrdersRecycleViewAdapter = new MyOrdersRecycleViewAdapter(activity, mMyOrdersModel.getOrdersData());
                            myOrdersRecylerView.setVisibility(View.VISIBLE);
                            aQuery.find(R.id.tv_no_record_found).getTextView().setVisibility(View.GONE);
                            SetListData();
                            loading.close();

                        } else {
                            loading.close();
                            myOrdersRecylerView.setVisibility(View.GONE);
                            aQuery.find(R.id.tv_no_record_found).getTextView().setVisibility(View.VISIBLE);
                            showToast(getResources().getString(R.string.no_record_found));

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
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
//            if (callType != null && callType.equals("Navigation") || callType.equals("callOrder")) {
//                intent = new Intent(activity, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
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
//            Intent i = new Intent(this, MyOrdersActivity.class);
//            startActivity(i);

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