package co.dtechsystem.carefer.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.dtechsystem.carefer.R;

public class enableLocation extends AppCompatActivity {
    private boolean locationSettings = false;
    boolean gps_enabled = false;
    boolean network_enabled= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);


    }

    public void locationenable(View view) {
        LocationManager lm = (LocationManager)getSystemService( Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){}

        if(!gps_enabled && !network_enabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity( intent );
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager lm = (LocationManager)getSystemService( Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){}
        if(gps_enabled && network_enabled){
            finish();
        }
    }
}


