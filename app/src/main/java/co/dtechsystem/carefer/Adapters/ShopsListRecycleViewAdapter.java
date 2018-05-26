package co.dtechsystem.carefer.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Sorting.ArabicNamesSortingModel;
import co.dtechsystem.carefer.Utils.AppConfig;


@SuppressWarnings("unchecked")
public class ShopsListRecycleViewAdapter extends RecyclerView.Adapter<ShopsListRecycleViewAdapter.ViewHolder> {
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    private int lastPosition;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressWarnings("unused")
    private static Boolean expand;
    private static LatLng mLatlngCurrent;
   // private Button btn_back_top_shops_list;
    private String isLocationAvail;
    private int selectedPosition = -2;
    ArrayList<Integer> totalIds = new ArrayList();
    private int coun;

    public ShopsListRecycleViewAdapter(){

    }

    //    work on its main thread
    @SuppressWarnings({"unused", "Convert2Diamond"})
    public ShopsListRecycleViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList,
                                       LatLng mLatlngCurrent, String isLocationAvail) {
        ShopsListRecycleViewAdapter._ShopslistRecordList = _ShopslistRecordList;
        _ShopslistRecordListFilter = new ArrayList<ShopsListModel.ShopslistRecord>();
        _ShopslistRecordListFilter.addAll(_ShopslistRecordList);

        ShopsListRecycleViewAdapter.activity = activity;
        this.mLatlngCurrent = mLatlngCurrent;
        this.isLocationAvail = isLocationAvail;
        setHasStableIds(true);

//        totalIds.clear();
//        for (int i = 0; i < _ShopslistRecordList.size(); i++) {
//            totalIds.add(Integer.valueOf(_ShopslistRecordList.get(i).getID()));
//        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_work_shops_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
//        ViewHolder vh=new ViewHolder(v);
        v.setTag(vh);
        return vh;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
     holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
      holder.txt_rate.setText(_ShopslistRecordList.get(position).getShopRating());
        if(_ShopslistRecordList.get(position).getIsDiscounted().equalsIgnoreCase("1"))
            holder.txt_discount.setVisibility(View.VISIBLE);

        try {
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _ShopslistRecordList.get(position).getID() + "/thumbnails/" + _ShopslistRecordList.get(position)
                    .getShopImage())
                    .override((int) activity.getResources().getDimension(R.dimen._120sdp), (int) activity.getResources().getDimension(R.dimen._120sdp))
                    .error(activity.getResources().getDrawable(R.drawable.ic_img_place_holder))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.workShope_img);
        } catch (Exception e) {
            e.printStackTrace();

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
                        holder.tv_distance_item.setText(distanceInMeters + " km");
                    }
                } else {
                    holder.tv_distance_item.setText("-- km");
                }
            } catch (Exception ee) {
                holder.tv_distance_item.setText("0 km");
                e.printStackTrace();
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_shop_name_shop_list;
        public final TextView tv_distance_item;
        public final TextView txt_rate;
        public final TextView txt_discount;

        final CheckBox check_fav;
        ImageView workShope_img;
        public final ProgressBar pg_image_load;


        public ViewHolder(View v) {

            super(v);
           coun++;
            tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
            check_fav = (CheckBox) v.findViewById(R.id.check_fav);
            pg_image_load = (ProgressBar) v.findViewById( R.id.pg_image_load);
            tv_distance_item = (TextView) v.findViewById(R.id.tv_distance_item);
            txt_rate = (TextView) v.findViewById(R.id.txt_rate);
            txt_discount = (TextView) v.findViewById(R.id.txt_discount);
            workShope_img=(ImageView) v.findViewById(R.id.workShope_img);


        }


    }

    @Override
    public int getItemCount() {
        return _ShopslistRecordList.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_ShopslistRecordList.get(position).getID());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Here is the key method to apply the animation
     */
    /*
    private void setAnimation(View viewToAnimate, int position) {
        try {

            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void mExpand(final View v) {
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
//        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(1000);
        v.startAnimation(a);
    }

    private static void collapse(final View v, final ShopsListRecycleViewAdapter.ViewHolder holder) {
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

    @SuppressWarnings("SameParameterValue")
    private static void expand(final View v, int duration, int targetHeight,
                               final ShopsListRecycleViewAdapter.ViewHolder holder) {

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

    @SuppressWarnings("unused")
    public static void collapse1(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
*/
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

    // Filter for Shops Names Class
    @SuppressWarnings("unused")


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


    // Filter Class
    @SuppressWarnings("unused")
    public static void filterShopsWithProviders(ArrayList<String> selectedItem, final String ProvideWarranty, final String ProvideReplacementParts,
                                                final String shopType, final String topRated, final String Distance, LatLng mLatlngCurrent) {
        if (_ShopslistRecordList != null) {
            if (selectedItem.size() > 0) {
                _ShopslistRecordList.clear();
                final List<ShopsListModel.ShopslistRecord> _Shops1Km = new ArrayList<>();
                final List<ShopsListModel.ShopslistRecord> _Shops5Km = new ArrayList<>();
                final List<ShopsListModel.ShopslistRecord> _Shops20Km = new ArrayList<>();
                final List<ShopsListModel.ShopslistRecord> _ShopsHighestKm = new ArrayList<>();
                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {

                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(Locale.getDefault())
                            .equals(shopType) || _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(Locale.getDefault())
                            .equals(ProvideReplacementParts) || _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(Locale.getDefault())
                            .equals(ProvideWarranty) || _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault())
                            .equals(topRated)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    } else if (Distance.equals("Highest")) {
                        if (mLatlngCurrent != null) {
                            LatLng shopLatlng = new LatLng(Double.parseDouble(_ShopslistRecordListFilter.get(i).getLatitude()), Double.parseDouble(_ShopslistRecordListFilter.get(i).getLongitude()));
                            Location userLoc = new Location("");
                            userLoc.setLatitude(mLatlngCurrent.latitude);
                            userLoc.setLongitude(mLatlngCurrent.longitude);
                            Location shopLoc = new Location("");
                            shopLoc.setLatitude(shopLatlng.latitude);
                            shopLoc.setLongitude(shopLatlng.longitude);
                            float lo = userLoc.distanceTo(shopLoc);
                            if (userLoc.distanceTo(shopLoc) <= 1000) {
                                _Shops1Km.add(_ShopslistRecordListFilter.get(i));
                            } else if (userLoc.distanceTo(shopLoc) <= 5000) {
                                _Shops5Km.add(_ShopslistRecordListFilter.get(i));
                            } else if (userLoc.distanceTo(shopLoc) <= 20000) {
                                _Shops20Km.add(_ShopslistRecordListFilter.get(i));
                            } else if (userLoc.distanceTo(shopLoc) > 20000) {
                                _ShopsHighestKm.add(_ShopslistRecordListFilter.get(i));
                            }
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.toast_location_not_found), Toast.LENGTH_SHORT).show();
                        }
                    } else if (selectedItem.size() == 4 && _ShopslistRecordListFilter.get(i).getShopType().toLowerCase(Locale.getDefault())
                            .equals(shopType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(Locale.getDefault())
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(Locale.getDefault())
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault())
                            .equals(topRated)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
                if (Distance.equals("Highest")) {
                    _ShopslistRecordList.addAll(_Shops1Km);
                    _ShopslistRecordList.addAll(_Shops5Km);
                    _ShopslistRecordList.addAll(_Shops20Km);
                    _ShopslistRecordList.addAll(_ShopsHighestKm);


                }
                if (_ShopslistRecordList.size() == 0) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                _ShopslistRecordList.clear();
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            }

        }
    }

    //sorting Function
    public static void SortingShopsWithNameRatingCity(final String SortingType, final String sortOrderType, final LatLng mLatlngCurrent, String cityName) {
        if (_ShopslistRecordList != null) {
            Locale locale = new Locale("ar");
            if (SortingType.equals("Rating")) {
                Comparator<ShopsListModel.ShopslistRecord> byFirstElement = new Comparator<ShopsListModel.ShopslistRecord>() {
                    @Override
                    public int compare(ShopsListModel.ShopslistRecord shop1, ShopsListModel.ShopslistRecord shop2) {
                        return Float.compare(Float.parseFloat(shop2.getShopRating()), Float.parseFloat(shop1.getShopRating()));
                    }


                };
                Collections.sort(_ShopslistRecordList, byFirstElement);
            } else if (SortingType.equals("Distance")) {

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

    public void SortFilterDistanceDefault() {
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

