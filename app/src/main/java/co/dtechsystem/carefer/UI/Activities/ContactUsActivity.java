package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import co.dtechsystem.carefer.R;

public class ContactUsActivity extends Fragment {


    private MainActivity activity;
    private View view;

    public ContactUsActivity() {
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
//        view= inflater.inflate(R.layout.activity_contact_us, container, false);
        view = inflater.inflate( R.layout.activity_contact_us , container , false );
        setUpToolbar();


        return view;
    }



    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.title_Contact_us);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);

    }

}
