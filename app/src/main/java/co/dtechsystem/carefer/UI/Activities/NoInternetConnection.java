package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Validations;

public class NoInternetConnection extends AppCompatActivity {
    Button reConnection ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        reConnection = (Button) findViewById(R.id.reConnection);
    }


    public void reConnection(View view){
        if (Validations.isInternetAvailable(this, true)){
            finish();
        }


    }
}
