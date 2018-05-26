package co.dtechsystem.carefer.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.dtechsystem.carefer.Adapters.SimpleSearchListAdapter;
import co.dtechsystem.carefer.R;


/**
 * Created by huzaifahhussain on 23/11/2017.
 */

public class ListSpinnerDialog extends Dialog {

    private EditText et_search;
    private ListView listView;
    private ArrayList<String> mList;
    private ArrayList<String> mSearchedResultList;

    private SimpleSearchListAdapter adapter;
    private TextView mCancel;
    private TextView mOK;
    int selected = -1;


    public ListSpinnerDialog(@NonNull Context context) {
        super(context);
    }


    public ListSpinnerDialog(@NonNull Context context, ArrayList<String> list) {
        super(context);
        mList = list;
        mSearchedResultList = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_spinner_dialog);
        et_search = (EditText) findViewById(R.id.et_search_list);
        listView = (ListView) findViewById(R.id.simple_list);
        mCancel = (TextView) findViewById(R.id.negative);
        mOK = (TextView) findViewById(R.id.tv_positive);


        //ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,mList);
        adapter = new SimpleSearchListAdapter(getContext(), R.layout.item_text, mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // select the selected and return it back some how. :P
                selected = position;
                adapter.setSelected(position);
            }
        });

        setClickListeneres();
        setOnTextChangeListenr();
    }

    private void setOnTextChangeListenr() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchedResultList.clear();
                for (String string : mList) {
                    if (string.contains(s)) {
                        mSearchedResultList.add(string);
                    }
                }
                adapter.setList(mSearchedResultList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClickListeneres() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = -1;
                ListSpinnerDialog.this.dismiss();

            }
        });        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSpinnerDialog.this.dismiss();

            }
        });
    }

    /**
     * restuns the position index
     *
     * @return
     */
    public int getSelected() {
        if (selected < 0)
            return -1;
        if (!mSearchedResultList.isEmpty() && mSearchedResultList.size() < mList.size()) {
            String selectedText = mSearchedResultList.get(selected);
            return mList.indexOf(selectedText);

        }

        return selected;
    }


}
