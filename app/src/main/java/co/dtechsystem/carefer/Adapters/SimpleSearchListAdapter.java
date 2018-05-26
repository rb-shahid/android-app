package co.dtechsystem.carefer.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.R;
//import android.util.Log;

/**
 * my custom adapter to facilitate the list View of the Main Menu
 *
 * @author hamza
 */

public class SimpleSearchListAdapter extends ArrayAdapter<String> {
    private List<String> mDataListArray;
    private Context context;
    private LayoutInflater inflator;
    int mHighlighted = -1;


    public SimpleSearchListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SimpleSearchListAdapter(Context context, int resource, List<String> mDataList) {
        super(context, resource, mDataList);
        this.mDataListArray = mDataList;
        this.context = context;
        inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder;
        String selectedString = mDataListArray.get(position);

        if (row == null) {
            // Log.v("","");

            row = inflator.inflate(R.layout.item_text, null);
            // Log.v("MainMenuCustomAdapter","row = inflator.inflate(R.layout.main_menu_item, null);");

            holder = new Holder();
            // holder.mMenuItemIconImageView = (ImageView)
            // row.findViewById(R.id.mainMenuItemImageView);
            holder.mTextView = (TextView) row
                    .findViewById(R.id.tv_simple_tv);

            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }
        if (mHighlighted >= 0 && mHighlighted == position) {
            holder.mTextView.setBackgroundColor(Color.LTGRAY);
        } else
            holder.mTextView.setBackgroundColor(Color.TRANSPARENT);


        if (selectedString != null) {

            holder.mTextView.setText(selectedString);

        }
        return row;
    }

    public void setSelected(int position) {
        mHighlighted = position;
        SimpleSearchListAdapter.this.notifyDataSetChanged();

    }

    public int getSelected() {
        return mHighlighted;
    }

    public void setList(ArrayList<String> list) {
        this.mDataListArray = list;
    }

    @Override
    public int getCount() {
        return mDataListArray.size();
    }

    private class Holder {
        TextView mTextView;
    }

    @Override
    public String getItem (int pos){
        return mDataListArray.get(pos);
    }
}
