package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.ReviewsRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ReviewsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class ReviewActivity extends Fragment {
    private RecyclerView rv_shop_reviews;
    private ReviewsRecycleViewAdapter mReviewsRecycleViewAdapter;
    private TextView tv_title_review;
    private String shopRatings = "";

    private MainActivity activity;
    private View view;

    public ReviewActivity() {
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
        view= inflater.inflate(R.layout.activity_review, container, false);

        rv_shop_reviews = (RecyclerView) view.findViewById(R.id.rv_shop_reviews);
//        tv_title_review = (TextView) view.findViewById(R.id.tv_title_review);
        String mshopID = activity.intent.getStringExtra("ShopID");
        shopRatings = activity.intent.getStringExtra("shopRatings");
        if (mshopID != null && !mshopID.equals("")) {
            if (Validations.isInternetAvailable(activity, true)) {
                activity.loading.show();
                APiShopReviews(mshopID);
            }
        }
        setUpToolbar();


        return view;
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
//    private void SetShaderToViews() {
//        Utils.gradientTextView(tv_title_review, activity);
//    }

    private void SetListData(String priceAVG, String qualityAVG, String timeAVG) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_shop_reviews);
        recyclerView.getItemAnimator().setChangeDuration(700);
        recyclerView.setAdapter(mReviewsRecycleViewAdapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mgridLayoutManager);
        float priceRate = 0, qualityRate = 0, timeRate = 0;

        if (priceAVG != null && !priceAVG.equals("")) {
            priceRate = Float.parseFloat(priceAVG);
            activity.aQuery.find(R.id.rb_price_rate).getRatingBar().setRating(priceRate);

        } else {
            activity.aQuery.find(R.id.rb_price_rate).getRatingBar().setRating(0);
        }
        if (qualityAVG != null && !qualityAVG.equals("")) {
            qualityRate = Float.parseFloat(qualityAVG);
            activity.aQuery.find(R.id.rb_quality_rate).getRatingBar().setRating(qualityRate);

        } else {
            activity.aQuery.find(R.id.rb_quality_rate).getRatingBar().setRating(0);
        }
        if (timeAVG != null && !timeAVG.equals("")) {
            timeRate = Float.parseFloat(timeAVG);
            activity.aQuery.find(R.id.rb_time_rate).getRatingBar().setRating(timeRate);

        } else {
            activity.aQuery.find(R.id.rb_time_rate).getRatingBar().setRating(0);
        }
        if (shopRatings != null && !shopRatings.equals("") && Float.parseFloat(shopRatings) > 0) {
            float totalRatingShop = Float.parseFloat(shopRatings);
            String avgRate = String.format("%.01f", totalRatingShop);
            activity.aQuery.id(R.id.tv_avg_rating).text(avgRate);
            if (totalRatingShop > 4.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_excelent));

            } else if (totalRatingShop > 3.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_good));


            } else if (totalRatingShop > 2.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_average));

            } else {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_lower));

            }


        }
    }

    private void APiShopReviews(final String shopID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiGetShopReviews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            ReviewsModel mReviewsModel = activity.gson.fromJson(response, ReviewsModel.class);
                            if (mReviewsModel.getShopReviews() != null && mReviewsModel.getShopReviews().size() > 0) {
                                mReviewsRecycleViewAdapter = new ReviewsRecycleViewAdapter(activity, mReviewsModel.getShopReviews());
                                JSONObject mainObj = new JSONObject(response);
                                JSONArray AVGRatings = mainObj.getJSONArray("AVGRatings");
                                JSONObject jsonObject = AVGRatings.getJSONObject(0);
                                String priceAVG = jsonObject.getString("priceAVG");
                                String qualityAVG = jsonObject.getString("qualityAVG");
                                String timeAVG = jsonObject.getString("priceAVG");
                                SetListData(priceAVG, qualityAVG, timeAVG);
                                activity.loading.close();
                            } else {
                                activity.loading.close();
                                activity.finish();
                                activity.showToast(getResources().getString(R.string.no_record_found));

                            }
                        } catch (Exception c) {
                            activity.loading.close();
                            activity.SendFireBaseError(String.valueOf(c));
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            c.printStackTrace();
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
                params.put("shopID", shopID);


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
        title.setText(R.string.tv_reviews);

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
