package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.RecyclerImagesItemClickListener;
import co.dtechsystem.carefer.Adapters.ShopsImagesPagerAdapter;
import co.dtechsystem.carefer.Adapters.ShopsImagesRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.FragmentUtil;
import co.dtechsystem.carefer.Utils.Validations;
import me.relex.circleindicator.CircleIndicator;

public class ShopDetailsActivity extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ShopsImagesRecycleViewAdapter mShopsImagesRecycleViewAdapter;
    private ShopsDetailsModel mShopsDetailsModel;
    private String mShopID, CityId, mplaceName;
    private int mStatus = 0;
    private LinearLayout lay_full_image;
    private LinearLayout lay_shop_details, lay_specialised_Brand_shop;
    private RecyclerView rv_images_shop_details;

    private ShopsImagesPagerAdapter mShopsImagesPagerAdapter;
    private ViewPager mViewPager;
    @SuppressWarnings("unused")
    public static String responsePublic;
    @SuppressWarnings("unused")
    public static String ShopsListDataResponse;
    @SuppressWarnings("unused")
    private static String citiesNamesIDsResponse;
    @SuppressWarnings("unused")
    private static String isLocationAvail;
    private TextView tv_title_shop_details;
    private TextView tv_reviews_shop_details ;
    private LatLng mLatlngCurrent;
    private String sUser_ID;



    private MainActivity activity;
    private View view;

    public ShopDetailsActivity() {
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
        view = inflater.inflate( R.layout.activity_shop_details, container, false );

        setUpToolbar();
        lay_full_image = (LinearLayout) view.findViewById( R.id.lay_full_image );
        lay_shop_details = (LinearLayout) view.findViewById( R.id.lay_shop_details );
        lay_specialised_Brand_shop = (LinearLayout) view.findViewById( R.id.lay_specialised_Brand_shop );
        rv_images_shop_details = (RecyclerView) view.findViewById( R.id.rv_images_shop_details );
        tv_reviews_shop_details = (TextView) view.findViewById( R.id.tv_reviews_shop_details );
        Button orderNow = (Button)view.findViewById( R.id.button2 ) ;

        tv_reviews_shop_details.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.replaceFragment( getActivity() , new ReviewActivity() , R.id.main_frame );

            }
        } );

        orderNow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.replaceFragment( getActivity() , new OrderNowActivity() , R.id.main_frame );

            }
        } );
//        tv_title_shop_details = (TextView) view.findViewById( R.id.tv_title_shop_details );
//        setUpToolbar();
//        SetUpLeftbar();
//        SetShaderToViews();
//        getIntents();
//        favouriteClicks();
        return view;
    }

    private void getIntents() {
        Intent mIntent = activity.getIntent();
        if (mIntent != null) {
            mShopID = mIntent.getStringExtra("ShopID");
            CityId = mIntent.getStringExtra("CityId");
            ShopsListDataResponse = activity.intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = activity.intent.getStringExtra("citiesNamesIDsResponse");
            isLocationAvail = activity.intent.getStringExtra("isLocationAvail");
            Bundle bundle = activity.intent.getParcelableExtra("bundle");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = activity.intent.getStringExtra("placeName");
            if (Validations.isInternetAvailable(activity, true)) {
                activity.loading.show();
                if (mShopID != null && !mShopID.equals("")) {
                    APiGetShopsDetailsData(mShopID);
                }
            }
        }
    }

    public void btn_reviews_click(View v) {
        Intent i = new Intent(activity, ReviewActivity.class);
        i.putExtra("ShopID", mShopID);
        i.putExtra("shopRatings", mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
        startActivity(i);
    }

//    private void SetShaderToViews() {
//        Utils.gradientTextView(tv_title_shop_details, activity);
//    }
//    public static void getRecylerPosition(int position) {
//        ShopDetailsActivity shopDetailsActivit = new ShopDetailsActivity();
//        shopDetailsActivit.initPagerImages(position);
//    }

    private void initPagerImages() {
        rv_images_shop_details.addOnItemTouchListener(
                new RecyclerImagesItemClickListener(activity, new RecyclerImagesItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
                            lay_full_image.setVisibility(View.VISIBLE);
                            lay_shop_details.setVisibility(View.GONE);
                            ArrayList<ShopsDetailsModel.ShopsImagessRecord> mShopsImages = new ArrayList<ShopsDetailsModel.ShopsImagessRecord>();

                            mShopsImages.addAll(mShopsDetailsModel.getShopImages());
                            Collections.reverse(mShopsImages);

                            mShopsImagesPagerAdapter = new ShopsImagesPagerAdapter(activity,mShopsImages, mShopID);
                            mViewPager = (ViewPager) view.findViewById(R.id.pager);
                            mViewPager.setAdapter(mShopsImagesPagerAdapter);
                            mViewPager.setCurrentItem(mShopsImages.size()-position-1);
//                            mShopsImagesPagerAdapter.notifyDataSetChanged();
                            final CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
                            indicator.setViewPager(mViewPager);
//                            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                                @Override
//                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                                    mShopsImagesPagerAdapter = new ShopsImagesPagerAdapter(activity, mShopsDetailsModel.getShopImages(), mShopID, 121);
//                                    mViewPager = (ViewPager) findViewById(R.id.pager);
//                                    mViewPager.setAdapter(mShopsImagesPagerAdapter);
////                            mShopsImagesPagerAdapter.notifyDataSetChanged();
//                                    final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//                                    indicator.setViewPager(mViewPager);
//                                }
//
//                                @Override
//                                public void onPageSelected(int position) {
//
//                                }
//
//                                @Override
//                                public void onPageScrollStateChanged(int state) {
//
//                                }
//                            });
                        } else {
                            lay_full_image.setVisibility(View.GONE);
                            lay_shop_details.setVisibility(View.VISIBLE);
                        }

                    }
                })
        );

    }


//    private void favouriteClicks() {
//        activity.aQuery.find(R.id.iv_fav_shop_list).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (mStatus) {
//                    case 0:
//                        if (Validations.isInternetAvailable(activity, true)) {
//                            activity.loading.show();
//                            APiShopFavourite(sUser_ID, mShopID, "add");
//                            mStatus = 1;
//                        }
//                        break;
//                    case 1:
//                        if (Validations.isInternetAvailable(activity, true)) {
//                            activity.loading.show();
//                            APiShopFavourite(sUser_ID, mShopID, "del");
//                            mStatus = 0;
//                        }
//                        break;
//                }
//
//
//            }
//
//        });
//
//    }

    public void GotoShopDetailsOrder(@SuppressWarnings("UnusedParameters") View V) {

        FragmentUtil.replaceFragment( getActivity() , new OrderNowActivity() , R.id.main_frame );

//        if (Validations.isInternetAvailable(activity, true)) {
//            Intent i = new Intent(getActivity(), OrderNowActivity.class);
//            if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {
//                i.putExtra("shopID", mShopID);
//                i.putExtra("shopName", mShopsDetailsModel.getShopsDetail().get(0).getShopName());
//                i.putExtra("shopType", mShopsDetailsModel.getShopsDetail().get(0).getShopType());
//                i.putExtra("shopRating", mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
//                i.putExtra("latitude", mShopsDetailsModel.getShopsDetail().get(0).getLatitude());
//                i.putExtra("longitude", mShopsDetailsModel.getShopsDetail().get(0).getLongitude());
//                i.putExtra("contact", mShopsDetailsModel.getShopsDetail().get(0).getContactNumber());
//                if (CityId != null && !CityId.equals("")) {
//                    i.putExtra("CityId", CityId);
//                    ShopsListActivity.ShopsListDataResponse = ShopsListDataResponse;
//                    i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
//                    i.putExtra("isLocationAvail", isLocationAvail);
//                    Bundle args = new Bundle();
//                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
//                    i.putExtra("placeName", mplaceName);
//                    i.putExtra("bundle", args);
//                }
//                if (mShopsDetailsModel.getShopsDetail().get(0).getShopImage() != null && !mShopsDetailsModel.getShopsDetail().get(0).getShopImage().equals("null")) {
//                    final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/thumbnails/";
//                    i.putExtra("shopImage", Url + mShopsDetailsModel.getShopsDetail().get(0).getShopImage());
//                } else {
//                    i.putExtra("shopImage", "");
//                }
//
//                startActivity(i);
//            } else {
//                activity.showToast(getResources().getString(R.string.some_went_wrong));
//            }
//        }
    }
//                    i.putExtra("ShopsListDataResponse", ShopsListActivity.ShopsListDataResponse);

//    @Override
//    protected void onRestart() {
////        getIntents();
//        onRestart();
//    }



//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) this );
//    }

    private void APiGetShopsDetailsData(final String ShopID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiShopsDetailsData + ShopID + "/cusid/" + sUser_ID, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        responsePublic = response.toString();
                        mShopsDetailsModel = activity.gson.fromJson(response.toString(), ShopsDetailsModel.class);
                        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
                            mShopsImagesRecycleViewAdapter = new ShopsImagesRecycleViewAdapter(activity,
                                    mShopsDetailsModel.getShopImages(), ShopID);
                            SetImagesListData();
                        }
//                        else {
//                            loading.close();
//                            showToast("No Images Record found yet!");
//                        }
                        if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {
                            SetShopsDetailsData();
                            activity.loading.close();
                        }
                        activity.loading.close();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        mShopsDetailsModel = activity.gson.fromJson("{shopsDetail: []}", ShopsDetailsModel.class);

                        activity.SendFireBaseError(String.valueOf(error));
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void APiShopFavourite(final String UserId, final String shopID, final String action) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiShopFavourite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (action.equals("del")) {
                                String success = jsonObject.getString("success");
                                if (success != null && success.equals("true")) {
                                    activity.aQuery.find(R.id.toolbar_fav).background(R.drawable.ic_fav_star_empty);
                                    activity.showToast(getResources().getString(R.string.toast_shop_deleted_fav));
                                }
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("userFavouriteShop");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String ID = jsonObject1.getString("ID");
                                if (ID != null && !ID.equals("")) {
                                    activity.aQuery.find(R.id.toolbar_fav).background(R.drawable.ic_fav_star_fill);
                                    activity.showToast(getResources().getString(R.string.toast_shop_added_fav));
                                }
                            }
                            activity.loading.close();
                        } catch (JSONException e) {
                            activity.loading.close();
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
                params.put("action", action);


                return params;
            }
        };
// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void SetImagesListData() {
        rv_images_shop_details.getItemAnimator().setChangeDuration(700);
        rv_images_shop_details.setAdapter(mShopsImagesRecycleViewAdapter);
        LinearLayoutManager mgridLayoutManager = new LinearLayoutManager(getActivity());
        mgridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mgridLayoutManager.setReverseLayout(true);
        rv_images_shop_details.setLayoutManager(mgridLayoutManager);
        activity.aQuery.find(R.id.toolbar_fav).background(R.drawable.ic_fav_star_fill);
        initPagerImages();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetShopsDetailsData() {
        activity.aQuery.id(R.id.tv_shop_name_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopName());
        activity.aQuery.id(R.id.tv_shop_service_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopType());
        if(mShopsDetailsModel.getShopsDetail().get(0).getIsDiscounted().equalsIgnoreCase("1")){
            activity.aQuery.id(R.id.lay_discount_value).visible();
            String amountPercent="%"+mShopsDetailsModel.getShopsDetail().get(0).getDiscountPercent()+"";
            activity.aQuery.id(R.id.tv_discoun_value).text(amountPercent);
        }

        if (mShopsDetailsModel.getShopsDetail().get(0).getShopRating() != null && !mShopsDetailsModel.getShopsDetail().get(0).getShopRating().equals("")) {
            activity. aQuery.id(R.id.rb_shop_rating_shop_details).rating(Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating()));
        }
        activity.aQuery.id(R.id.tv_shop_des_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopDescription());
//        aQuery.id(R.id.tv_shop_des_shop_details).text(getResources().getString(R.string.lorem_ispum));
        TextView tv_shop_des_shop_details = (TextView) view.findViewById(R.id.tv_shop_des_shop_details);
        if (tv_shop_des_shop_details.getLayout() != null && tv_shop_des_shop_details.getLayout().getLineCount() >= 4) {
//            makeTextViewResizable(tv_shop_des_shop_details, 4, getResources().getString(R.string.tv_read_full_des), true);
            activity.aQuery.id(R.id.tv_shop_des_view_more_shop_details).getTextView().setVisibility(View.VISIBLE);

        } else {
            activity.aQuery.id(R.id.tv_shop_des_view_more_shop_details).getTextView().setVisibility(View.GONE);

        }
//        aQuery.id(R.id.tv_shop_des_shop_details).getTextView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                aQuery.id(R.id.tv_shop_des_shop_details).getTextView().getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
        if (mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand() != null && !mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand().equals("")) {
            activity.aQuery.id(R.id.tv_specialised_Brand_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand());
            lay_specialised_Brand_shop.setVisibility(View.VISIBLE);

        } else {
            lay_specialised_Brand_shop.setVisibility(View.GONE);
        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getBrands() != null) {
//            String[] serviceArray = mShopsDetailsModel.getShopsDetail().get(0).getServiceType().split(",");
            String brands =mShopsDetailsModel.getShopsDetail().get(0).getBrands();
//            activity.aQuery.id(R.id.tv_brands_shops).text(mShopsDetailsModel.getShopsDetail().get(0).getBrands());

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getNationality() != null) {
//            String[] nationalityArray = mShopsDetailsModel.getShopsDetail().get(0).getNationality().split(",");
            activity.aQuery.id(R.id.tv_nationality_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getNationality());

        }

        activity.aQuery.id(R.id.tv_city_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getCity());
        if (mShopsDetailsModel.getShopsDetail().get(0).getProvideWarranty().equals("1")) {
//            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_yes));
            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.btn_green));

        } else {
//            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_no));
            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.btn_red));

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getProvideReplaceParts().equals("1")) {
//            aQuery.id(R.id.iv_replace_parts_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_yes));
            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.btn_green));

        } else {
//            aQuery.id(R.id.iv_replace_parts_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_no));
            activity.aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.btn_red));

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getFavourite() != null &&
                mShopsDetailsModel.getShopsDetail().get(0).getFavourite().equals("true")) {
            mStatus = 1;
           activity.aQuery.find(R.id.toolbar_fav).background(R.drawable.ic_fav_star_fill);
        } else {
            mStatus = 0;
            activity.aQuery.find(R.id.toolbar_fav).background(R.drawable.ic_fav_star_empty);
        }

        if (mShopsDetailsModel.getShopsDetail().get(0).getShopRating() != null && Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating()) > 0) {
            activity.aQuery.id(R.id.tv_reviews_shop_details).text(getResources().getString(R.string.tv_reviews));
            activity.aQuery.id(R.id.tv_avg_rating).getTextView().setVisibility(View.VISIBLE);
            activity.aQuery.id(R.id.tv_total_rating).getTextView().setVisibility(View.VISIBLE);
            activity.aQuery.id(R.id.tv_rating_type).getTextView().setVisibility(View.VISIBLE);
            float totalRatingShop = Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
            String avgRate = String.format("%.01f", totalRatingShop);
            activity.aQuery.id(R.id.tv_avg_rating).text(avgRate + "/5");
            activity.aQuery.id(R.id.tv_total_rating).text(getResources().getString(R.string.tv_see_all) + " " + mShopsDetailsModel.getShopsDetail().get(0).getReviewCount() + " " + getResources().getString(R.string.tv_reviews));
            if (totalRatingShop > 4.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_excelent));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRoundRect("", Color.rgb(8, 145, 28), 90);
                activity.aQuery.id(R.id.iv_bk_avg_rating).getImageView().setImageDrawable(drawable);
//                aQuery.id(R.id.tv_avg_rating).backgroundColor(Color.rgb(8,145,28));

            } else if (totalRatingShop > 3.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_good));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRoundRect("", Color.GREEN, 90);
                activity.aQuery.id(R.id.iv_bk_avg_rating).getImageView().setImageDrawable(drawable);


            } else if (totalRatingShop > 2.4) {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_average));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRoundRect("", Color.BLUE, 90);
                activity.aQuery.id(R.id.iv_bk_avg_rating).getImageView().setImageDrawable(drawable);

            } else {
                activity.aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_lower));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRoundRect("", Color.GRAY, 90);
                activity.aQuery.id(R.id.iv_bk_avg_rating).getImageView().setImageDrawable(drawable);

            }

        } else {
            activity.aQuery.id(R.id.tv_avg_rating).getTextView().setVisibility(View.GONE);
            activity.aQuery.id(R.id.tv_total_rating).getTextView().setVisibility(View.GONE);
            activity.aQuery.id(R.id.tv_rating_type).getTextView().setVisibility(View.GONE);
            activity.aQuery.id(R.id.tv_reviews_shop_details).text(getResources().getString(R.string.tv_not_enough_reviews));

        }

    }

    public void showDescriptionActivity(View c) {
        if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {

            activity.intent = new Intent(activity, ShopDescriptionActivity.class);
            activity.intent.putExtra("shopName", mShopsDetailsModel.getShopsDetail().get(0).getShopName());
            activity.intent.putExtra("shopDescription", mShopsDetailsModel.getShopsDetail().get(0).getShopDescription());
            startActivity(activity.intent);
        }
    }

    //    public void loadImagesSliderbulits() {
//        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
//            for (int i = 0; i < mShopsDetailsModel.getShopImages().size(); i++) {
//
//                ImageView myButton = new ImageView(this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//                params.setMargins(10, 10, 10, 10);
//                myButton.setLayoutParams(params);
//                myButton.setBackgroundResource(R.drawable.dr_round_icon);
//                lay_builts_images.addView(myButton);
//            }
//        }
//
//
//    }
    private void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                     final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, getResources().getString(R.string.tv_read_less_des), false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 4, getResources().getString(R.string.tv_read_full_des), true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @SuppressLint("RtlHardcoded")
    public void btn_drawyerMenuOpen(@SuppressWarnings("UnusedParameters") View v) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            if (lay_full_image.getVisibility() == View.VISIBLE) {
//                lay_full_image.setVisibility(View.GONE);
//                lay_shop_details.setVisibility(View.VISIBLE);
//            } else {
//                super.onBackPressed();
//            }
//        }
//    }

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


    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.shop_details);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        final ImageButton fav=(ImageButton) activity.findViewById(R.id.toolbar_fav);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//                activity.aQuery.id(R.id.toolbar_fav).getImageView().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.white));

                switch (mStatus) {
                    case 0:
                        if (Validations.isInternetAvailable(activity, true)) {
                            activity.loading.show();
                            APiShopFavourite(sUser_ID, mShopID, "add");
                            mStatus = 1;
                            fav.setBackground( getContext().getResources().getDrawable(  R.drawable.ic_favorite_fill ) );
                        }
                        break;
                    case 1:
                        if (Validations.isInternetAvailable(activity, true)) {
                            activity.loading.show();
                            APiShopFavourite(sUser_ID, mShopID, "del");
                            mStatus = 0;
                            fav.setBackground( getContext().getResources().getDrawable (R.color.none) );

                        }
                        break;
                }




            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }


}
