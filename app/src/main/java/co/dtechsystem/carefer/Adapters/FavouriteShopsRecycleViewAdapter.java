package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import co.dtechsystem.carefer.Models.FavouriteShopsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;
import co.dtechsystem.carefer.UI.Activities.ShopsListActivity;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;


public class FavouriteShopsRecycleViewAdapter extends RecyclerView.Adapter<FavouriteShopsRecycleViewAdapter.ViewHolder> {
    private final List<FavouriteShopsModel.FavouriteShopsRecord> _FavouriteShopsDetails;
    private int lastPosition;
    private final Activity activity;

    @SuppressWarnings("unused")
    public FavouriteShopsRecycleViewAdapter(Activity activity, List<FavouriteShopsModel.FavouriteShopsRecord> _FavouriteShopsDetail) {
        this._FavouriteShopsDetails = _FavouriteShopsDetail;
        this.activity = activity;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fav_shops, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        setAnimation(holder.itemView, position);
        holder.tv_fav_shop_name.setText(_FavouriteShopsDetails.get(position).getShopName());
        holder.tv_brands_shops.setText(_FavouriteShopsDetails.get(position).getShopServiceType());
        if (_FavouriteShopsDetails.get(position).getShopImage() != null) {
            holder.pg_image_load.setVisibility(View.VISIBLE);
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _FavouriteShopsDetails.get(position).getID() + "/thumbnails/" + _FavouriteShopsDetails.get(position)
                    .getShopImage())
                    .override((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp))
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(R.drawable.ic_img_place_holder)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.pg_image_load.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.pg_image_load.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_shop_fav);

        }
        holder.lay_main_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                mIntent.putExtra("ShopID", _FavouriteShopsDetails.get(position).getID().toString());
                mIntent.putExtra("CityId", Utils.readPreferences(activity, "CityId", ""));
                ShopsListActivity shopsListActivity = new ShopsListActivity();
                shopsListActivity.ShopsListDataResponse = Utils.readPreferences(activity, "ShopsListDataResponse", "");
//                mIntent.putExtra("ShopsListDataResponse", Utils.readPreferences(activity, "ShopsListDataResponse", ""));
                mIntent.putExtra("citiesNamesIDsResponse", Utils.readPreferences(activity, "citiesNamesIDsResponse", ""));
                mIntent.putExtra("isLocationAvail", Utils.readPreferences(activity, "isLocationAvail", ""));
                String ltlng = Utils.readPreferences(activity, "mLatLngCurrent", "");
                String latLngString[] = ltlng.split(",");
                Bundle args = new Bundle();
                if (latLngString.length > 0) {
                    LatLng mLatLngCurrent = new LatLng(Double.parseDouble(latLngString[0]), Double.parseDouble(latLngString[1]));
                    args.putParcelable("LatLngCurrent", mLatLngCurrent);
                }
                mIntent.putExtra("bundle", args);
                mIntent.putExtra("placeName", Utils.readPreferences(activity, "mPlaceName", ""));
                activity.startActivity(mIntent);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_fav_shop_name;
        public final TextView tv_brands_shops;
        public final ImageView iv_shop_fav;
        public final ProgressBar pg_image_load;
        RelativeLayout lay_main_fav;

        public ViewHolder(View v) {

            super(v);
            pg_image_load = (ProgressBar) v.findViewById(R.id.pg_image_load);
            tv_fav_shop_name = (TextView) v.findViewById(R.id.tv_fav_shop_name);
            tv_brands_shops = (TextView) v.findViewById(R.id.tv_brands_shops);
            iv_shop_fav = (ImageView) v.findViewById(R.id.iv_shop_fav);
            lay_main_fav = (RelativeLayout) v.findViewById(R.id.lay_main_fav);
        }

    }

    @Override
    public int getItemCount() {
        return _FavouriteShopsDetails.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}