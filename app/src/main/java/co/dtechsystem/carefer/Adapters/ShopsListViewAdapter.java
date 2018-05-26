package co.dtechsystem.carefer.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Sorting.ArabicNamesSortingModel;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;
import co.dtechsystem.carefer.Utils.AppConfig;

@SuppressWarnings("unchecked")
class ShopsListViewAdapter extends BaseAdapter {
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    private int lastPosition;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressWarnings("unused")
    private static Boolean expand;
    private static LatLng mLatlngCurrent;
    private Button btn_back_top_shops_list;
    private String isLocationAvail;
    private int selectedPosition = -2;
    private int currentShopId;
    private boolean stateMaintain = false;
    ArrayList<Integer> totalIds = new ArrayList();
    private static LayoutInflater inflater = null;


    public ShopsListViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList,
                                LatLng mLatlngCurrent, Button btn_back_top_shops_list, String isLocationAvail) {
        ShopsListViewAdapter._ShopslistRecordList = _ShopslistRecordList;
        _ShopslistRecordListFilter = new ArrayList<>();
        _ShopslistRecordListFilter.addAll(_ShopslistRecordList);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ShopsListViewAdapter.activity = activity;

        this.expand = false;
        this.mLatlngCurrent = mLatlngCurrent;
        this.btn_back_top_shops_list = btn_back_top_shops_list;
        this.isLocationAvail = isLocationAvail;

    }

    public int getCount() {
        return _ShopslistRecordList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_ShopslistRecordList.get(position).getID());
    }

    private void setViews(View v, ViewHolder viewHolder) {
        viewHolder.tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
        viewHolder.tv_service_type_shop_list = (TextView) v.findViewById(R.id.tv_service_type_shop_list);
        viewHolder.tv_desc_shop_list = (TextView) v.findViewById(R.id.tv_desc_shop_list);
        viewHolder.tv_desc_short_shop_list = (TextView) v.findViewById(R.id.tv_desc_short_shop_list);
        viewHolder.rb_shop_shop_list = (RatingBar) v.findViewById(R.id.rb_shop_shop_list);
        viewHolder.rb_shop_large__shop_list = (RatingBar) v.findViewById(R.id.rb_shop_large__shop_list);
        viewHolder.lay_shop_item = (LinearLayout) v.findViewById(R.id.lay_shop_item);
        viewHolder.iv_fav_shop_list = (ImageView) v.findViewById(R.id.toolbar_fav);
        viewHolder.lay_shops_names = (LinearLayout) v.findViewById(R.id.lay_shops_names);
        viewHolder.lay_details = (LinearLayout) v.findViewById(R.id.lay_details);
        viewHolder.lay_expand = (LinearLayout) v.findViewById(R.id.lay_expand);
        viewHolder.lay_collpase = (LinearLayout) v.findViewById(R.id.lay_collapse);
        viewHolder.btn_details_shops_list = (Button) v.findViewById(R.id.btn_details_shops_list);
        viewHolder.iv_drop_shop_details = (ImageView) v.findViewById(R.id.iv_drop_shop_details);
        viewHolder.iv_drop_details_shop_details = (ImageView) v.findViewById(R.id.iv_drop_details_shop_details);
        viewHolder.iv_shop_detail_large_expand = (ImageView) v.findViewById(R.id.iv_shop_detail_large_expand);
        viewHolder.iv_item_top_shops_list = (SelectableRoundedImageView) v.findViewById(R.id.iv_item_top_shops_list);
        viewHolder.pg_image_load = (ProgressBar) v.findViewById(R.id.pg_image_load);
        viewHolder.pg_image_load_large_shops = (ProgressBar) v.findViewById(R.id.pg_image_load_large_shops);
        viewHolder.tv_shop_name_large_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_large_shop_list);
        viewHolder.tv_distance_item = (TextView) v.findViewById(R.id.tv_distance_item);
        viewHolder.tv_distance_details = (TextView) v.findViewById(R.id.tv_distance_details);
    }

    public class ViewHolder {
        public TextView tv_shop_name_shop_list;
        public TextView tv_service_type_shop_list;
        public TextView tv_desc_shop_list;
        public TextView tv_desc_short_shop_list;

        public TextView tv_shop_name_large_shop_list;
        public TextView tv_distance_item;
        public TextView tv_distance_details;
        public RatingBar rb_shop_shop_list;
        public RatingBar rb_shop_large__shop_list;
        LinearLayout lay_shop_item;
        @SuppressWarnings("unused")
        LinearLayout lay_shops_names;
        LinearLayout lay_details;
        LinearLayout lay_expand;
        LinearLayout lay_collpase;
        @SuppressWarnings("unused")
        ImageView iv_fav_shop_list;
        ImageView iv_drop_shop_details;
        SelectableRoundedImageView iv_item_top_shops_list;
        ImageView iv_drop_details_shop_details;
        ImageView iv_shop_detail_large_expand;
        Button btn_details_shops_list;
        public ProgressBar pg_image_load;
        public ProgressBar pg_image_load_large_shops;

    }

    private ViewHolder viewHolder = null;

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        viewHolder=new ViewHolder();
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_shops, null);
            setViews(vi, viewHolder);
            vi.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        String stringTypeArr[] = _ShopslistRecordList.get(position).getServiceType().split(",");
        if (stringTypeArr != null) {
            viewHolder.tv_service_type_shop_list.setText(stringTypeArr[0]);
        }
        if (position > 10) {
            btn_back_top_shops_list.setVisibility(View.VISIBLE);
        } else {
            btn_back_top_shops_list.setVisibility(View.GONE);
        }
        viewHolder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        viewHolder.rb_shop_shop_list.setRating((Float.parseFloat(_ShopslistRecordList.get(position).getShopRating())));
        viewHolder.tv_desc_shop_list.setText(_ShopslistRecordList.get(position).getShopDescription());
        viewHolder.tv_desc_short_shop_list.setText(_ShopslistRecordList.get(position).getShopDescription());
        viewHolder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        viewHolder.btn_details_shops_list.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("RedundantStringToString")
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                mIntent.putExtra("ShopID", _ShopslistRecordList.get(position).getID().toString());
                activity.startActivity(mIntent);
            }
        });

        viewHolder.iv_item_top_shops_list.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.iv_item_top_shops_list.setCornerRadiiDP(5, 5, 5, 5);
        viewHolder.iv_item_top_shops_list.setBorderWidthDP(0);
        viewHolder.iv_item_top_shops_list.setOval(false);
        viewHolder.lay_details.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //noinspection UnusedAssignment
        int height = viewHolder.lay_details.getMeasuredHeight();
        if (currentShopId == Integer.parseInt(_ShopslistRecordList.get(position).getID())) {

            if (viewHolder.lay_details.getVisibility() != View.VISIBLE && !expand) {
                viewHolder.lay_shop_item.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.dr_corner_orange_three_color, null));
                int i = (int) activity.getResources().getDimension(R.dimen._80sdp);
                expand(viewHolder.lay_details, 500, i);
                expand = true;

            } else {
                if (stateMaintain) {
                    viewHolder.lay_shop_item.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.dr_corner_grey, null));
                    collapse(viewHolder.lay_details);
                    expand = false;
                    stateMaintain = false;
                }

            }
        } else {
            viewHolder.lay_shop_item.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.dr_corner_grey, null));
            collapse(viewHolder.lay_details);
//            expand = false;

        }
        viewHolder.lay_shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                if (!stateMaintain) {
                    stateMaintain = true;
                }
                currentShopId = Integer.parseInt(_ShopslistRecordList.get(position).getID());
                notifyDataSetChanged();

//
            }
        });
        if (_ShopslistRecordList.get(position).getShopImage() != null && !_ShopslistRecordList.get(position).getShopImage().equals("null")) {
//            pg_image_load.setVisibility(View.VISIBLE);
            //noinspection deprecation
            try {
                viewHolder.pg_image_load.setVisibility(View.VISIBLE);
                Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _ShopslistRecordList.get(position).getID() + "/thumbnails/" + _ShopslistRecordList.get(position)
                        .getShopImage())
                        .override((int) activity.getResources().getDimension(R.dimen._120sdp), (int) activity.getResources().getDimension(R.dimen._120sdp))
                        .error(activity.getResources().getDrawable(R.drawable.ic_img_place_holder))
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                viewHolder.pg_image_load.setVisibility(View.GONE);
                                notifyDataSetChanged();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                viewHolder.pg_image_load.setVisibility(View.GONE);
                                notifyDataSetChanged();
                                return false;
                            }
                        })
                        .into(viewHolder.iv_item_top_shops_list);
//                    notifyDataSetChanged();
            } catch (Exception e) {
                viewHolder.pg_image_load.setVisibility(View.GONE);
                notifyDataSetChanged();
                e.printStackTrace();
            }


        }

        try {
            if (isLocationAvail != null && isLocationAvail.equals("Yes")) {
                if (mLatlngCurrent != null) {
                    Location curentLocation = new Location("");
                    curentLocation.setLatitude(mLatlngCurrent.latitude);
                    curentLocation.setLongitude(mLatlngCurrent.longitude);

                    Location destination = new Location("");
                    destination.setLatitude(Double.parseDouble(_ShopslistRecordList.get(position).getLatitude()));
                    destination.setLongitude(Double.parseDouble(_ShopslistRecordList.get(position).getLongitude()));

                    double distanceInMeters = curentLocation.distanceTo(destination) / 1000;

//                    DecimalFormat newFormat = new DecimalFormat("#####");
//                    double kmInDec = Float.valueOf(newFormat.format(distanceInMeters));
                    distanceInMeters = Math.round(distanceInMeters * 10) / 10.0d;
                    viewHolder.tv_distance_item.setText(distanceInMeters + " km");
                    viewHolder.tv_distance_details.setText(distanceInMeters + " km");
                }
            } else {
                viewHolder.tv_distance_item.setText("-- km");
            }
        } catch (Exception e) {
            viewHolder.tv_distance_item.setText("0 km");
            viewHolder.tv_distance_details.setText("0 km");
            e.printStackTrace();
        }


        return vi;
    }

    private static void expand(final View v, int duration, int targetHeight
    ) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                expand = true;
//                holder.lay_shop_item.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();

    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    expand = false;

                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        v.startAnimation(a);
    }

    public static void filterShopsName(String Text, final LatLng mLatlngCurrent) {
        Text = Text.toLowerCase(Locale.getDefault());
        if (_ShopslistRecordList != null) {
            _ShopslistRecordList.clear();
            if (Text.length() == 0) {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            } else {
                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                    if (_ShopslistRecordListFilter.get(i).getShopName().toLowerCase(Locale.getDefault()).contains(Text)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }

            }
        }
        if (_ShopslistRecordList != null && _ShopslistRecordList.size() == 0) {

            Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
        } else {
            if (mLatlngCurrent != null) {
                Comparator<ShopsListModel.ShopslistRecord> byFirstElement = new Comparator<ShopsListModel.ShopslistRecord>() {
                    @Override
                    public int compare(ShopsListModel.ShopslistRecord shop1, ShopsListModel.ShopslistRecord shop2) {

                        LatLng shopLatlng1 = new LatLng(Double.parseDouble(shop1.getLatitude()), Double.parseDouble(shop1.getLongitude()));
                        Location userLoc = new Location("");
                        userLoc.setLatitude(mLatlngCurrent.latitude);
                        userLoc.setLongitude(mLatlngCurrent.longitude);
                        Location shopLoc1 = new Location("");
                        shopLoc1.setLatitude(shopLatlng1.latitude);
                        shopLoc1.setLongitude(shopLatlng1.longitude);
                        float loc1Shop = userLoc.distanceTo(shopLoc1);

                        LatLng shopLatlng2 = new LatLng(Double.parseDouble(shop2.getLatitude()), Double.parseDouble(shop2.getLongitude()));

                        Location shopLoc2 = new Location("");
                        shopLoc2.setLatitude(shopLatlng2.latitude);
                        shopLoc2.setLongitude(shopLatlng2.longitude);
                        float loc2Shop = userLoc.distanceTo(shopLoc2);
                        return Float.compare(loc1Shop, loc2Shop);
                    }


                };
                Collections.sort(_ShopslistRecordList, byFirstElement);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.toast_location_not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void SortFilterDistanceDefault() {
        if (mLatlngCurrent != null) {
            Comparator<ShopsListModel.ShopslistRecord> byFirstElement = new Comparator<ShopsListModel.ShopslistRecord>() {
                @Override
                public int compare(ShopsListModel.ShopslistRecord shop1, ShopsListModel.ShopslistRecord shop2) {

                    LatLng shopLatlng1 = new LatLng(Double.parseDouble(shop1.getLatitude()), Double.parseDouble(shop1.getLongitude()));
                    Location userLoc = new Location("");
                    userLoc.setLatitude(mLatlngCurrent.latitude);
                    userLoc.setLongitude(mLatlngCurrent.longitude);
                    Location shopLoc1 = new Location("");
                    shopLoc1.setLatitude(shopLatlng1.latitude);
                    shopLoc1.setLongitude(shopLatlng1.longitude);
                    float loc1Shop = userLoc.distanceTo(shopLoc1);

                    LatLng shopLatlng2 = new LatLng(Double.parseDouble(shop2.getLatitude()), Double.parseDouble(shop2.getLongitude()));

                    Location shopLoc2 = new Location("");
                    shopLoc2.setLatitude(shopLatlng2.latitude);
                    shopLoc2.setLongitude(shopLatlng2.longitude);
                    float loc2Shop = userLoc.distanceTo(shopLoc2);
                    return Float.compare(loc1Shop, loc2Shop);
                }


            };
            Collections.sort(_ShopslistRecordList, byFirstElement);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.toast_location_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    //sorting Function
    public static void SortingShopsWithNameRatingCity(final String SortingType, final String sortOrderType, final LatLng mLatlngCurrent, String cityName) {
        if (_ShopslistRecordList != null) {
            Locale locale = new Locale("ar");
            if (SortingType.equals("Rating")) {
//                _ShopslistRecordList.clear();
                Comparator<ShopsListModel.ShopslistRecord> byFirstElement = new Comparator<ShopsListModel.ShopslistRecord>() {
                    @Override
                    public int compare(ShopsListModel.ShopslistRecord shop1, ShopsListModel.ShopslistRecord shop2) {
                        return Float.compare(Float.parseFloat(shop2.getShopRating()), Float.parseFloat(shop1.getShopRating()));
                    }


                };
                Collections.sort(_ShopslistRecordList, byFirstElement);
//                _ShopslistRecordList.addAll(ShopsRatingSorting.MatchRating(_ShopslistRecordListFilter, "Ascending"));
//                Float ratingArray[] = new Float[_ShopslistRecordListFilter.size()];
////                ArrayList<String> shopIds = new ArrayList<String>();
//                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
//                    ratingArray[i] = Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating() + Float.parseFloat(_ShopslistRecordListFilter.get(i).getID()));
////                    shopIds.add(i, _ShopslistRecordListFilter.get(i).getID());
//                }
//                Arrays.sort(ratingArray, Collections.reverseOrder());
//                for (int i = 0; i < ratingArray.length; i++) {
//                    for (int j = 0; j < _ShopslistRecordListFilter.size(); j++) {
//                        float id = ratingArray[i] - Float.parseFloat(_ShopslistRecordListFilter.get(i).getID());
//                        if (ratingArray[i] > 0 && ratingArray[i] == Float.parseFloat(_ShopslistRecordListFilter.get(j).getShopRating())) {
//                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(j));
//                            break;
//                        }
//
//
////                        else if (ratingArray[i] == 0&&Integer.parseInt(shopIds.get(i)) == Integer.parseInt(_ShopslistRecordListFilter.get(j).getID())) {
////                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(j));
////                            break;
////                        }
//                    }
//                }
            } else if (SortingType.equals("Distance")) {

//                Float distanceArray[] = new Float[_ShopslistRecordListFilter.size()];

//                if (mLatlngCurrent != null) {
//                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
//                        LatLng shopLatlng = new LatLng(Double.parseDouble(_ShopslistRecordListFilter.get(i).getLatitude()), Double.parseDouble(_ShopslistRecordListFilter.get(i).getLongitude()));
//                        Location userLoc = new Location("");
//                        userLoc.setLatitude(mLatlngCurrent.latitude);
//                        userLoc.setLongitude(mLatlngCurrent.longitude);
//                        Location shopLoc = new Location("");
//                        shopLoc.setLatitude(shopLatlng.latitude);
//                        shopLoc.setLongitude(shopLatlng.longitude);
//                        float lo = userLoc.distanceTo(shopLoc);
//                        distanceArray[i] = lo;
////                        if (userLoc.distanceTo(shopLoc) <= 1000) {
////                            _Shops1Km.add(_ShopslistRecordListFilter.get(i));
////                        } else if (userLoc.distanceTo(shopLoc) <= 5000) {
////                            _Shops5Km.add(_ShopslistRecordListFilter.get(i));
////                        } else if (userLoc.distanceTo(shopLoc) <= 20000) {
////                            _Shops20Km.add(_ShopslistRecordListFilter.get(i));
////                        } else if (userLoc.distanceTo(shopLoc) > 20000) {
////                            _ShopsHighestKm.add(_ShopslistRecordListFilter.get(i));
////                        }
////                            final SortByDistance.Location myLocation=new SortByDistance.Location(mLatlngCurrent.latitude,mLatlngCurrent.longitude);
////                            final SortByDistance.Location myShop=new SortByDistance.Location(shopLatlng.latitude,shopLatlng.longitude);
//
////                            sortDistance(myLocation,myShop);
//                    }
//                    _ShopslistRecordList.clear();
//                    Arrays.sort(distanceArray, Collections.reverseOrder());
//                    for (int i = distanceArray.length - 1; i >= 0; i--) {
//                        for (int j = 0; j < _ShopslistRecordListFilter.size(); j++) {
//                            LatLng shopLatlng = new LatLng(Double.parseDouble(_ShopslistRecordListFilter.get(j).getLatitude()), Double.parseDouble(_ShopslistRecordListFilter.get(j).getLongitude()));
//                            Location userLoc = new Location("");
//                            userLoc.setLatitude(mLatlngCurrent.latitude);
//                            userLoc.setLongitude(mLatlngCurrent.longitude);
//                            Location shopLoc = new Location("");
//                            shopLoc.setLatitude(shopLatlng.latitude);
//                            shopLoc.setLongitude(shopLatlng.longitude);
//                            float lo = userLoc.distanceTo(shopLoc);
//                            if (distanceArray[i] == lo) {
//                                _ShopslistRecordList.add(_ShopslistRecordListFilter.get(j));
//                                break;
//                            } else {
////                                _ShopslistAfterFiltration.remove(i);
////                                break;
////                                _ShopslistBeforeFiltration.remove(i);
////                                break;
//                            }
//                        }
//                    }
//                    int i = _ShopslistRecordList.size();
////                    _ShopslistRecordList.addAll(_Shops1Km);
////                    _ShopslistRecordList.addAll(_Shops5Km);
////                    _ShopslistRecordList.addAll(_Shops20Km);
////                    _ShopslistRecordList.addAll(_ShopsHighestKm);
////                } else {
//                    Toast.makeText(activity, activity.getResources().getString(R.string.toast_location_not_found), Toast.LENGTH_SHORT).show();
//                }
                if (mLatlngCurrent != null) {
                    Comparator<ShopsListModel.ShopslistRecord> byFirstElement = new Comparator<ShopsListModel.ShopslistRecord>() {
                        @Override
                        public int compare(ShopsListModel.ShopslistRecord shop1, ShopsListModel.ShopslistRecord shop2) {

                            LatLng shopLatlng1 = new LatLng(Double.parseDouble(shop1.getLatitude()), Double.parseDouble(shop1.getLongitude()));
                            Location userLoc = new Location("");
                            userLoc.setLatitude(mLatlngCurrent.latitude);
                            userLoc.setLongitude(mLatlngCurrent.longitude);
                            Location shopLoc1 = new Location("");
                            shopLoc1.setLatitude(shopLatlng1.latitude);
                            shopLoc1.setLongitude(shopLatlng1.longitude);
                            float loc1Shop = userLoc.distanceTo(shopLoc1);

                            LatLng shopLatlng2 = new LatLng(Double.parseDouble(shop2.getLatitude()), Double.parseDouble(shop2.getLongitude()));

                            Location shopLoc2 = new Location("");
                            shopLoc2.setLatitude(shopLatlng2.latitude);
                            shopLoc2.setLongitude(shopLatlng2.longitude);
                            float loc2Shop = userLoc.distanceTo(shopLoc2);
                            return Float.compare(loc1Shop, loc2Shop);
                        }


                    };
                    Collections.sort(_ShopslistRecordList, byFirstElement);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.toast_location_not_found), Toast.LENGTH_SHORT).show();
                }

            } else if (SortingType.equals("Name")) {
                _ShopslistRecordList.clear();
                ArabicNamesSortingModel mArabicNamesSortingModel = new ArabicNamesSortingModel();
                _ShopslistRecordList.addAll(mArabicNamesSortingModel.MatchWithName(_ShopslistRecordListFilter, sortOrderType));
            } else if (SortingType.equals("City")) {
                _ShopslistRecordList.clear();
                if (!cityName.equals("")) {
                    for (int j = 0; j < _ShopslistRecordListFilter.size(); j++) {
                        if (_ShopslistRecordListFilter.get(j).getCity().toLowerCase(locale)
                                .equals(cityName.toLowerCase(locale))) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(j));
                        }

                    }
                } else {
                    _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
                }
            }

            if (_ShopslistRecordList.size() == 0) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
            }
        }

    }

    // Filter Class
    @SuppressWarnings("unused")
    public static void filterShops(String Type, String Both, String service, String Brand) {
        service = service.toLowerCase(Locale.getDefault());
        Brand = Brand.toLowerCase(Locale.getDefault());
        if (_ShopslistRecordList != null) {
            _ShopslistRecordList.clear();
            if (service.length() == 0 && Brand.length() == 0) {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            } else {
                if (Type.equals("Service") && Both.equals("No")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(Locale.getDefault())
                                .contains(service)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                } else if (Type.equals("Brand") && Both.equals("No")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(Brand)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                } else if (Both.equals("Yes")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(service)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));

                        } else if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(Brand)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));


                        }
                    }
                } else if (Type.equals("Default") && Both.equals("No")) {
                    _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
                }


            }
        }

    }
}
