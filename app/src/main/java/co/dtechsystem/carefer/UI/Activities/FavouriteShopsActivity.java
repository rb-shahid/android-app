package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import co.dtechsystem.carefer.Adapters.FavouriteShopsRecycleViewAdapter;
import co.dtechsystem.carefer.Google.Analytics.AnalyticsApplication;
import co.dtechsystem.carefer.Models.FavouriteShopsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

public class FavouriteShopsActivity extends Fragment{
    private FavouriteShopsRecycleViewAdapter mFavouriteShopsRecycleViewAdapter;
    private DrawerLayout mDrawerLayout;
    private TextView tv_title_fav_shops;
    private RecyclerView favShopssRecylerView;
    private String sUser_ID;

    private MainActivity activity;
    private View view;

    public FavouriteShopsActivity() {
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
        view= inflater.inflate(R.layout.activity_favourite_shops, container, false);

        setUpToolbar();
        favShopssRecylerView = (RecyclerView) view.findViewById(R.id.rv_fav_shops);
//        tv_title_fav_shops = (TextView) view.findViewById(R.id.tv_title_fav_shops);
//        SetShaderToViews();
//        SetUpLeftbar();
        if (Validations.isInternetAvailable(activity, true)) {
            activity.loading.show();
            APiGetFavShopslistData(sUser_ID);
        }

        return view;
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
//    private void SetShaderToViews() {
//        Utils.gradientTextView(tv_title_fav_shops, activity);
//    }
    /**
     * Handle api call for user favourite shops list
     * @param User_ID Takes String as param of user id and fetch data on the basis of this id

     */
    private void APiGetFavShopslistData(String User_ID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiMyFavouriteShopsList + User_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        FavouriteShopsModel mFavouriteShopsModel = activity.gson.fromJson(response.toString(), FavouriteShopsModel.class);
                        if (mFavouriteShopsModel.getFavouriteShops() != null && mFavouriteShopsModel.getFavouriteShops().size() > 0) {
                            mFavouriteShopsRecycleViewAdapter = new FavouriteShopsRecycleViewAdapter(activity, mFavouriteShopsModel.getFavouriteShops());
                            favShopssRecylerView.setVisibility(View.VISIBLE);
                            activity.aQuery.find(R.id.tv_no_record_found).getTextView().setVisibility(View.GONE);
                            SetListData();
                            activity.loading.close();
                        } else {
                            favShopssRecylerView.setVisibility(View.GONE);
                            activity.aQuery.find(R.id.tv_no_record_found).getTextView().setVisibility(View.VISIBLE);
                            activity.loading.close();
                            activity.showToast(getResources().getString(R.string.no_record_found));

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AnalyticsApplication.getInstance().trackException(error);
                        activity.loading.close();
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                        activity.SendFireBaseError(String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
    }

    /**
     * Handle recyler view data to show user a list of his favourite shops
     */
    private void SetListData() {
        favShopssRecylerView.getItemAnimator().setChangeDuration(700);
        favShopssRecylerView.setAdapter(mFavouriteShopsRecycleViewAdapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(getActivity(), 1);
        favShopssRecylerView.setLayoutManager(mgridLayoutManager);
    }

    /**
     * Handle left bar menu
     */
//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener();
//    }

    /**
     * Handle app left menu for user when he clicks on menu button
//     * @param v
//     */
//    @SuppressWarnings("UnusedParameters")
//    @SuppressLint("RtlHardcoded")
//    public void btn_drawyerMenuOpen(View v) {
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
//    }
//
//    /**
//     * Handle user generic button back pressed
//     */
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
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
//            // Handle the camera action
//        } else if (id == R.id.nav_my_orders) {
//            Intent i = new Intent(this, MyOrdersActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_fav_shops) {
////            Intent i = new Intent(this, FavouriteShopsActivity.class);
////            startActivity(i);
//
//        } else if (id == R.id.nav_share) {
//            Intent i = new Intent(this, ShareActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_about_us) {
//            Intent i = new Intent(this, ContactUsActivity.class);
//            startActivity(i);
//        }
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
//        return true;
//    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_fav_shops);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);

        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });
        final ImageButton search=(ImageButton) activity.findViewById(R.id.toolbar_search);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.findViewById(R.id.searchfiled).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.toolbar_back).setVisibility(View.GONE);

            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }
}