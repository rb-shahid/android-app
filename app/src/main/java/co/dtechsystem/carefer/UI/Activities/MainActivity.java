package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.BottomNavigationViewHelper;
import co.dtechsystem.carefer.Utils.FragmentUtil;

public class MainActivity extends BaseActivity {

    BottomNavigationView navigation;
    private boolean fromstack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Getting Google Play availability status
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.tv_title_top_a_map);

        if(savedInstanceState == null){
            FragmentUtil.addFragment(MainActivity.this,new MainFragment(),R.id.main_frame);

        }
        SetUpLeftbar();
        isLocationEnable();



    }



    /**
     * Handle left bar menu
     */
    private void SetUpLeftbar() {
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }



    }


    @Override
    public void onBackPressed() {
Log.d("onBackPressed",getSupportFragmentManager().getBackStackEntryCount()+"");
        int count=getSupportFragmentManager().getBackStackEntryCount();


        if(count == 1){
            getSupportFragmentManager().popBackStack();
            fromstack=true;
            currentFragment=R.id.nav_home;
            navigation.findViewById(R.id.nav_home).performClick();
            fromstack=false;
        }
        else if (count > 1 ){
            getSupportFragmentManager().popBackStack();
            String sname =getSupportFragmentManager().getBackStackEntryAt(count - 2).getName();
             fromstack=true;

            int name=Integer.parseInt(sname);
            currentFragment=name;
            switch(name){
                case  R.id.nav_home:
                    navigation.findViewById(R.id.nav_home).performClick();
                    break;
                case  R.id.nav_my_orders:
                    navigation.findViewById(R.id.nav_my_orders).performClick();
                    break;
                case  R.id.nav_fav_shops:
                    navigation.findViewById(R.id.nav_fav_shops).performClick();
                    break;
                case R.id.nav_profile:
                    navigation.findViewById(R.id.nav_profile).performClick();
                    break;
                case R.id.nav_settings:
                    navigation.findViewById(R.id.nav_settings).performClick();
                    break;
                default:
                    break;

            }
             fromstack=false;

        } else {
            super.onBackPressed();
        }

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    private int currentFragment=R.id.nav_home;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d("count","OnNavigationItemSelectedListener");

            if (fromstack)return true;
           if(currentFragment == item.getItemId()){
               return false;
           }

            currentFragment=item.getItemId();

            switch (item.getItemId()) {
                case R.id.nav_home:
                    FragmentUtil.replaceFragmentWithBackStack(MainActivity.this,new MainFragment(),R.id.main_frame,R.id.nav_home);
                    return true;
                case R.id.nav_my_orders:
                    FragmentUtil.replaceFragmentWithBackStack(MainActivity.this,new MyOrdersFragment(),R.id.main_frame,R.id.nav_my_orders);

                    return true;
                case R.id.nav_fav_shops:
                    FragmentUtil.replaceFragmentWithBackStack(MainActivity.this,new FavouriteShopsActivity(),R.id.main_frame,R.id.nav_fav_shops);

                    return true;
                case R.id.nav_profile:
                    FragmentUtil.replaceFragmentWithBackStack(MainActivity.this,new MyDetailsFragment(),R.id.main_frame,R.id.nav_profile);

                    return true;
                case R.id.nav_settings:
                    FragmentUtil.replaceFragmentWithBackStack(MainActivity.this,new SettingsActivity(),R.id.main_frame,R.id.nav_settings);



                    return true;
            }
            return false;
        }

    };
    /*
    @SuppressWarnings({"StatementWithEmptyBody", "NullableProblems"})
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_details) {
            Intent i = new Intent(this, MyDetailsActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_my_orders) {
            Intent i = new Intent(this, MyOrdersActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_fav_shops) {
            Intent i = new Intent(this, FavouriteShopsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this, ShareActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(this, ContactUsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
*/

    public void isLocationEnable(){
        LocationManager lm = (LocationManager)getSystemService( Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled= false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){}
        if(!gps_enabled && !network_enabled){
            Intent intent = new Intent(this , enableLocation.class);
            startActivity( intent );
        }else {

        }
    }
}
