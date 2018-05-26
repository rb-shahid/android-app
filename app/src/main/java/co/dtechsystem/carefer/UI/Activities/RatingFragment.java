package co.dtechsystem.carefer.UI.Activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    private String mshopID;
    private String morderID;
    private MainActivity activity;
    private View v;

    public RatingFragment() {
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
         v= inflater.inflate(R.layout.fragment_rating, container, false);

        setUpToolbar();
        GetDataForViews();

        v.findViewById(R.id.add_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRating();
            }
        });
        return  v;
    }

    private void GetDataForViews() {
        Bundle b=getArguments();
        if (b != null) {
            mshopID = b.getString("shopID");
            morderID = b.getString("orderID");
            String mShopName = b.getString("ShopName");
            activity.aQuery.find(R.id.tv_shop_name_rating).text(mShopName);

        }
       // RatingBar prive_ratting=(RatingBar)v.findViewById(R.id.rb_price_rate);
        //v.findViewById(R.id.rb_quality_rate).getRatingBar().setRating(0);
        //v.findViewById(R.id.rb_time_rate).getRatingBar().setRating(0);
    }


    private void APisendRating(final String UserId, final String shopID, final String orderID, final String priceRating,
                               final String qualityRating, final String timeRating, final String comments) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiRatingShop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
//                            //noinspection UnusedAssignment
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            if (result.equals("1")) {
                                ((MainActivity)activity).showToast(getResources().getString(R.string.toast_review_added));
                                activity.finish();
                                activity.loading.close();
                            }
                            activity.loading.close();
                        } catch (JSONException e) {
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            activity.loading.close();
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
                        activity.SendFireBaseError(String.valueOf(error));
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserId);
                params.put("shopID", shopID);
                params.put("orderID", orderID);
                params.put("comments", comments);
                params.put("priceRating", priceRating);
                params.put("qualityRating", qualityRating);
                params.put("timeRating", timeRating);


                return params;
            }
        };
// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void addRating(){
        float price_rate = activity.aQuery.find(R.id.rb_price_rate).getRatingBar().getRating();
        float quality_rate = activity.aQuery.find(R.id.rb_quality_rate).getRatingBar().getRating();
        float time_rate = activity.aQuery.find(R.id.rb_time_rate).getRatingBar().getRating();
        String et_coments_rate = activity.aQuery.find(R.id.et_coments_rate).getText().toString();
        if (et_coments_rate != null && !et_coments_rate.equals("")) {

            if (price_rate != 0.0 || quality_rate != 0.0 || time_rate != 0.0) {
                if (Validations.isInternetAvailable(activity, true)) {
                    activity.loading.show();
                    APisendRating(activity.sUser_ID, mshopID, morderID, String.valueOf(price_rate), String.valueOf(quality_rate),
                            String.valueOf(time_rate), et_coments_rate);
                }
            } else {
                activity.showToast(getResources().getString(R.string.toast_please_give_rating));

            }
        } else {
            activity.showToast(getResources().getString(R.string.toast_please_add_comments));
        }
    }


    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.btn_rate_this_shop);

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
