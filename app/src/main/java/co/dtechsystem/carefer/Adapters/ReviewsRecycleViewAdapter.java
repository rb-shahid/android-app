package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import co.dtechsystem.carefer.Models.ReviewsModel;
import co.dtechsystem.carefer.R;


public class ReviewsRecycleViewAdapter extends RecyclerView.Adapter<ReviewsRecycleViewAdapter.ViewHolder> {
    private final List<ReviewsModel.ShopReview> _shopReviews;
    private int lastPosition;
    private final Activity activity;

    @SuppressWarnings("unused")
    public ReviewsRecycleViewAdapter(Activity activity, List<ReviewsModel.ShopReview> _shopReviews) {
        this._shopReviews = _shopReviews;
        this.activity = activity;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ReviewsRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ReviewsRecycleViewAdapter.ViewHolder vh = new ReviewsRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ReviewsRecycleViewAdapter.ViewHolder holder, final int position) {
//        setAnimation(holder.itemView, position);
        float priceRate=0,timeRate=0,qualityRate=0;
        String customerName = _shopReviews.get(position).getCustomerName();
        if (customerName != null && !customerName.equals("")) {
            holder.tv_customer_name_rate.setText(customerName);

        } else {
            customerName = activity.getResources().getString(R.string.tv_name_anonymous);
            holder.tv_customer_name_rate.setText(customerName);
        }
//        String DateFormed = Utils.formattedDateFromString("yyyy-MM-dd", "dd-MMM-yyyy", _shopReviews.get(position).getDateAdded());
        holder.tv_date_rate.setText(_shopReviews.get(position).getDateAdded());
        if (_shopReviews.get(position).getHidAction().equals("0")) {
            holder.tv_coments_rate.setText(_shopReviews.get(position).getComment());
        }
        if (_shopReviews.get(position).getPriceRating() != null && !_shopReviews.get(position).getPriceRating().equals("")) {
            priceRate=Float.parseFloat(_shopReviews.get(position).getPriceRating());
            holder.rb_price_rate.setRating(priceRate);

        } else {
            holder.rb_price_rate.setRating(0);
        }
        if (_shopReviews.get(position).getQualityRating() != null && !_shopReviews.get(position).getQualityRating().equals("")) {
            qualityRate=Float.parseFloat(_shopReviews.get(position).getQualityRating());
            holder.rb_quality_rate.setRating(qualityRate);

        } else {
            holder.rb_quality_rate.setRating(0);
        }
        if (_shopReviews.get(position).getTimeRating() != null && !_shopReviews.get(position).getTimeRating().equals("")) {
            timeRate=Float.parseFloat(_shopReviews.get(position).getTimeRating());
            holder.rb_time_rate.setRating(timeRate);
        } else {
            holder.rb_time_rate.setRating(0);
        }
        holder.tv_coments_rate.setMovementMethod(new ScrollingMovementMethod());

        Float AvgRate = priceRate+qualityRate+timeRate;
        AvgRate = AvgRate / 3;
        String avgRate = String.format("%.01f", AvgRate);
        holder.tv_avg_rate_review.setText(avgRate);
//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRoundRect(String.valueOf(customerName.charAt(0)), color, 90);
//        holder.iv_first_letter_of_name.setImageDrawable(drawable);
//        holder.tv_coments_rate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                holder.tv_coments_rate.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final RatingBar rb_price_rate, rb_quality_rate, rb_time_rate;
        public final TextView tv_coments_rate, tv_customer_name_rate, tv_date_rate, tv_avg_rate_review;
//        public final ImageView iv_first_letter_of_name;

        public ViewHolder(View v) {

            super(v);
            tv_customer_name_rate = (TextView) v.findViewById(R.id.tv_customer_name_rate);
            tv_date_rate = (TextView) v.findViewById(R.id.tv_date_rate);
            rb_price_rate = (RatingBar) v.findViewById(R.id.rb_price_rate);
            rb_quality_rate = (RatingBar) v.findViewById(R.id.rb_quality_rate);
            rb_time_rate = (RatingBar) v.findViewById(R.id.rb_time_rate);
            tv_coments_rate = (TextView) v.findViewById(R.id.tv_coments_rate);
//            iv_first_letter_of_name = (ImageView) v.findViewById(R.id.iv_first_letter_of_name);
            tv_avg_rate_review = (TextView) v.findViewById(R.id.tv_avg_rate_review);

        }

    }


    @Override
    public int getItemCount() {
        return _shopReviews.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_shopReviews.get(position).getID());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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