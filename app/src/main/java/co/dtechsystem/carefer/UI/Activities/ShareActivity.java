package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

public class ShareActivity extends BottomSheetDialogFragment {
//    private DrawerLayout mDrawerLayout;

//    private  ShowbottomSheet mListener ;
    View view;
    public MainActivity activity;

    public ShareActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.activity_share, container, false );

//        Button btn_share_fb =      (Button) view.findViewById(R.id.btn_share_fb);
//        Button btn_share_twitter = (Button) view.findViewById(R.id.btn_share_twitter);
//        Button btn_share_gtalk =   (Button) view.findViewById(R.id.btn_share_gtalk);
//        Button btn_share_insta =   (Button) view.findViewById(R.id.btn_share_insta);
//        Button btn_share_pintrest =(Button) view.findViewById(R.id.btn_share_pintrest);
//        Button btn_share_whatsap = (Button) view.findViewById(R.id.btn_share_whatsap);
//        Button btn_share_telegram =(Button) view.findViewById(R.id.btn_share_telegram);
//        Button btn_share_snapchat =(Button) view.findViewById(R.id.btn_share_snapchat);


//        btn_share_fb.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               mListener.onButtonCliked(Utils.SharePublic(activity, "https://m.facebook.com/sharer.php", "com.facebook.katana"));
//               dismiss();
//            }
//        } );
//
//        btn_share_twitter.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://mobile.twitter.com/compose/tweet", "com.twitter.android"));
//                dismiss();
//
//            }
//        } );
//
//        btn_share_gtalk.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://plus.google.com/share?url=https://play.google.com/store/apps/details?id=" , "com.google.android.apps.plus"));
//                dismiss();
//            }
//        } );
//
//        btn_share_insta.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://www.instagram.com/", "com.instagram.android"));
//                dismiss();
//            }
//        } );
//
//        btn_share_pintrest.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "http://pinterest.com/pin/create", "com.pinterest"));
//                dismiss();
//            }
//        } );
//
//        btn_share_whatsap.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://web.whatsapp.com/", "com.whatsapp"));
//                dismiss();
//
//
//            }
//        } );
//
//        btn_share_telegram.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://web.telegram.org", "org.telegram.messenger"));
//                dismiss();
//            }
//        } );
//
//        btn_share_snapchat.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onButtonCliked(Utils.SharePublic(activity, "https://www.snap.com", "com.snapchat.android"));
//                dismiss();
//            }
//        } );



//        SetShaderToViews();
//        SetUpLeftbar();
//        setlistenrstosharebtns();

        return view ;

    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    /*private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_share_app, activity);
    }*/

    private void setlistenrstosharebtns() {
        activity.aQuery.find(R.id.btn_share_fb).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_twitter).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_gtalk).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_insta).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_pintrest).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_whatsap).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_telegram).clicked( (View.OnClickListener) this );
        activity.aQuery.find(R.id.btn_share_snapchat).clicked( (View.OnClickListener) this );

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.btn_share_fb:
//                Utils.SharePublic(activity, "https://m.facebook.com/sharer.php", "com.facebook.katana");
//                break;
//            case R.id.btn_share_twitter:
//                Utils.SharePublic(activity, "https://mobile.twitter.com/compose/tweet", "com.twitter.android");
//                break;
//            case R.id.btn_share_gtalk:
//                Utils.SharePublic(activity, "https://plus.google.com/share?url=https://play.google.com/store/apps/details?id=" + getPackageName(), "com.google.android.apps.plus");
//                break;
//
//            case R.id.btn_share_insta:
//                Utils.SharePublic(activity, "https://www.instagram.com/", "com.instagram.android");
//
//                break;
//            case R.id.btn_share_pintrest:
//                Utils.SharePublic(activity, "http://pinterest.com/pin/create", "com.pinterest");
//                break;
//
//            case R.id.btn_share_whatsap:
//                Utils.SharePublic(activity, "https://web.whatsapp.com/", "com.whatsapp");
//
//                break;
//            case R.id.btn_share_telegram:
//                Utils.SharePublic(activity, "https://web.telegram.org", "org.telegram.messenger");
//                break;
//
//            case R.id.btn_share_snapchat:
//                Utils.SharePublic(activity, "https://www.snap.com", "com.snapchat.android");
//                break;
//
//        }
//
//    }

//    private void SetUpLeftbar() {
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) this );
//    }

//    @SuppressWarnings("UnusedParameters")
//    @SuppressLint("RtlHardcoded")
//    public void btn_drawyerMenuOpen(View v) {
//        mDrawerLayout.openDrawer(Gravity.RIGHT);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            getActivity().onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        activity.getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_my_details) {
//            Intent i = new Intent(this, MyDetailsActivity.class);
//            startActivity(i);
//            // Handle the camera action
//        } else if (id == R.id.nav_my_orders) {
//            Intent i = new Intent(this, MyOrdersActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_fav_shops) {
//            Intent i = new Intent(this, FavouriteShopsActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_share) {
////            Intent i = new Intent(this, ShareActivity.class);
////            startActivity(i);
//
//        } else if (id == R.id.nav_about_us) {
//            Intent i = new Intent(this, ContactUsActivity.class);
//            startActivity(i);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
//        return true;
//    }


//    public interface ShowbottomSheet{
//
//        void onButtonCliked(String text);
////
////        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( ShareActivity.this );
////        View parentView = getLayoutInflater().inflate( R.layout.menu_bottom_sheet , null );
////        bottomSheetDialog.setContentView( parentView );
////        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from( (View) parentView.getParent() );
////        bottomSheetBehavior.setPeekHeight( (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , 100 , getResources().getDisplayMetrics() ) );
////        bottomSheetDialog.show();
//
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach( context );
//        mListener = (ShowbottomSheet) context;
////        try {
//
////        } catch (ClassCastException e ){
////            throw  new ClassCastException( context.toString()+
////                    "must implement ShowbottomSheet " );
////        }
////    }
//    }
}
