package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import co.dtechsystem.carefer.Models.Price;
import co.dtechsystem.carefer.R;

/**
 * Created by Zoraiz Ejaz on 12/14/2017.
 */

public class MovedShopAdapter extends RecyclerView.Adapter<MovedShopAdapter.channelHolder> {

    private static Activity activity;
    private List<Price> mDataset;


    public static class channelHolder extends RecyclerView.ViewHolder {


        TextView price;
        TextView description;
        RadioButton rb;


        public channelHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            description = (TextView) itemView.findViewById(R.id.description);
            rb = (RadioButton) itemView.findViewById(R.id.radioBtn);


        }


    }


    public MovedShopAdapter(Context mContext, List<Price> myDataset) {
        mDataset = myDataset;
        activity = (Activity) mContext;
    }

    @Override
    public channelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_moved_shop_price, parent, false);

        channelHolder dataObjectHolder = new channelHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(channelHolder holder, final int position) {
        holder.price.setText(mDataset.get(position).getPrice());
        holder.description.setText(mDataset.get(position).getPriceDesc());

        holder.rb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                if(cb.isChecked()){
                    mDataset.get(position).setIs_selected("true");
                }else{
                    mDataset.get(position).setIs_selected("false");
                }


            }
        });


    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
