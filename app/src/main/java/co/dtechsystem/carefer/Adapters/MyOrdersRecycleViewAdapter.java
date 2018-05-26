package co.dtechsystem.carefer.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.OrderDetailActivity;
import co.dtechsystem.carefer.UI.Activities.RatingFragment;
import co.dtechsystem.carefer.Utils.FragmentUtil;
import co.dtechsystem.carefer.Utils.Utils;


public class MyOrdersRecycleViewAdapter extends RecyclerView.Adapter<MyOrdersRecycleViewAdapter.ViewHolder> {
    private final List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords;
    private int lastPosition;
    private final Activity activity;


    @SuppressWarnings({"unused", "UnusedAssignment"})
    public MyOrdersRecycleViewAdapter(Activity activity, List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords) {
        this._MyOrdersRecords = _MyOrdersRecords;
        this.activity = activity;
        Boolean mExpand = false;
        setHasStableIds(true);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_record_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String DateFormed = Utils.formattedDateFromString("yyyy-MM-dd", "dd-MM-yyyy",
                _MyOrdersRecords.get(position).getOrderDate());
        holder.number_req.setText(_MyOrdersRecords.get(position).getOrderNo());
        holder.date_req.setText(DateFormed);
        holder.type_req.setText(_MyOrdersRecords.get(position).getOrderType());
        holder.workshop_car.setText(_MyOrdersRecords.get(position).getShopName());

        holder.rate_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();

                Log.d("id",_MyOrdersRecords.get(position).getOrderServiceTypeID());
                if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("1")) {

                    if (!_MyOrdersRecords.get(position).getIsRated().equals("1")) {
                        b=new Bundle();
                        b.putString("orderID", _MyOrdersRecords.get(position).getID());
                        b.putString("shopID", _MyOrdersRecords.get(position).getShopID());
                        b.putString("ShopName", _MyOrdersRecords.get(position).getShopName());

                        FragmentUtil.replaceFragmentWithBackStack(activity,new RatingFragment(),R.id.main_frame,R.string.btn_rate_this_shop, b);
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.toast_review_already_added), Toast.LENGTH_SHORT).show();
                    }

                } else if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("2")) {
                    FragmentUtil.replaceFragmentWithBackStack(activity,new RatingFragment(),R.id.main_frame,R.string.btn_rate_this_shop, b);
                    b.putString("orderID", _MyOrdersRecords.get(position).getOrderNo());

//                    Intent intent = new Intent(activity, OrderDetailActivity.class);
//                    intent.putExtra("orderID", _MyOrdersRecords.get(position).getOrderNo());
//                    activity.startActivity(intent);

                } else if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("3")) {
                    FragmentUtil.replaceFragmentWithBackStack(activity,new RatingFragment(),R.id.main_frame,R.string.btn_rate_this_shop, b);
                    b.putString("orderID", _MyOrdersRecords.get(position).getOrderNo());

//                   Intent intent = new Intent(activity, OrderDetailActivity.class);
//                    intent.putExtra("orderID", _MyOrdersRecords.get(position).getOrderNo());
//                    activity.startActivity(intent);

                }


            }
        });

    }



 /*

    @SuppressWarnings("SameParameterValue")
    private static void expand(final View v, int duration, int targetHeight,
                               final MyOrdersRecycleViewAdapter.ViewHolder holder) {

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
                holder.lay_top_my_order.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();

    }

    private static void collapse(final View v, final MyOrdersRecycleViewAdapter.ViewHolder holder) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
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
                holder.lay_top_my_order.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration(1000);
        v.startAnimation(a);
    }
*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView number_req,date_req, workshop_car, type_req;
        public  RelativeLayout myRequestrecord ;
        Button rate_shop;


        public ViewHolder(View v) {

            super(v);
            number_req = (TextView) v.findViewById(R.id.number_req);
            date_req = (TextView) v.findViewById(R.id.date_req);
            workshop_car = (TextView) v.findViewById(R.id.workshop_car);
            type_req = (TextView) v.findViewById(R.id.type_req);
            rate_shop = (Button) v.findViewById(R.id.rate_shope);
            myRequestrecord =(RelativeLayout)v.findViewById( R.id.my_request_record_item);

            myRequestrecord.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentUtil.replaceFragment( activity , new OrderDetailActivity() , R.id.main_frame );
                }
            } );




        }
        }

/*
    @SuppressWarnings("unused")
    public static void mExpand(final View v, int duration, int targetHeight) {

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
        valueAnimator.start();
    }

    @SuppressWarnings("unused")
    public static void collapse(final View v, int duration, int targetHeight) {
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
    @Override
    public int getItemCount() {
        return _MyOrdersRecords.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_MyOrdersRecords.get(position).getID());
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
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    */


}