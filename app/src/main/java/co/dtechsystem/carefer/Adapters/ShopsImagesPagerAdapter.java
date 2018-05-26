package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class ShopsImagesPagerAdapter extends PagerAdapter {

    private final Activity mActivity;
    private final LayoutInflater mLayoutInflater;
    private final List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails;
    private final String mShopID;
//    private boolean clicked;
//    private final int getPosition;

    @SuppressWarnings("unused")
    public ShopsImagesPagerAdapter(Activity mActivity, List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails, String ShopID) {
        this.mActivity = mActivity;
        this._ShopsImagesDetails = _ShopsImagesDetails;
        this.mShopID = ShopID;
//        this.clicked = true;
//        this.getPosition = getPosition;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _ShopsImagesDetails.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item_sliding_images, container, false);
        final ImageView iv_full_image = (ImageView) itemView.findViewById(R.id.image);
        final ProgressBar pg_image_load_large_shops = (ProgressBar) itemView.findViewById(R.id.pg_image_load_large_shops);
        final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/";
        final String UrlThumbnail = AppConfig.BaseUrlImages + "shop-" + mShopID + "/thumbnails/";

        pg_image_load_large_shops.setVisibility(View.VISIBLE);
        Glide.with(mActivity)
                .load(UrlThumbnail + _ShopsImagesDetails.get(position)

                        .getImageName())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        iv_full_image.setOnTouchListener(new ImageMatrixTouchHandler(mActivity));
                        iv_full_image.setImageBitmap(bitmap);
                        Glide.with(mActivity)
                                .load(Url + _ShopsImagesDetails.get(position)
                                        .getImageName())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        // Do something with bitmap here.
                                        pg_image_load_large_shops.setVisibility(View.GONE);
                                        iv_full_image.setOnTouchListener(new ImageMatrixTouchHandler(mActivity));
                                        iv_full_image.setImageBitmap(bitmap);
                                    }
                                });
                    }
                });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}