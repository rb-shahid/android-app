package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.FragmentUtil;

public class SettingsActivity extends Fragment{

    Button change_mobile_number , about_us ,contact_us , share_App;
    View view;
   public MainActivity activity;

    private boolean fromstack;

    public SettingsActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_settings, container, false );


        change_mobile_number = (Button) view.findViewById( R.id.change_mobile_number );
        about_us = (Button) view.findViewById( R.id.about_us );
        contact_us = (Button) view.findViewById( R.id.contact_us );
        share_App = (Button) view.findViewById( R.id.share_App );
        setUpToolbar();


        return view ;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.setting);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });


        change_mobile_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentUtil.replaceFragment( getActivity() , new ChangeNumActivity() , R.id.main_frame );

            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentUtil.replaceFragment( getActivity() , new AboutCareferActivity() , R.id.main_frame );

            }
        });

        contact_us.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.replaceFragment( getActivity() , new ContactUsActivity() , R.id.main_frame );
            }
        } );


        share_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           ShareActivity bottomSheet =new ShareActivity() ;
//                bottomSheet.show( getFragmentManager() , "shareactivity" );
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this dope website! - http://www.mikemilla.com/"); // Simple text and URL to share
                sendIntent.setType("text/plain");
                getActivity().startActivity(sendIntent);

                }
        });




        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);


    }

}
