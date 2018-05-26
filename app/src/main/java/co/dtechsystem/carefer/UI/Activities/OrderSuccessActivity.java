package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.dtechsystem.carefer.R;

/**
 * Created by SA on 16/04/18.
 */

public class OrderSuccessActivity extends Activity {
    private TextView shopName;
    private TextView orderDate;
    private TextView orderNO;

    private String shop_name ;
    private String order_no ;
    private String order_date ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_order);
        Button button = (Button) findViewById( R.id.button4 );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
//        getdataFromViews();
//        SetdataToViews();
    }



    public  void SetdataToViews(){
        shopName = (TextView)findViewById(R.id.tv_shop_name_shop_details);
        orderDate = (TextView)findViewById(R.id.tv_date_order);
        orderNO = (TextView)findViewById(R.id.tv_order_no);

        shopName.setText(shop_name);
        orderDate.setText(order_date);
        orderNO.setText(order_no);
    }

    public  void getdataFromViews(){
        Intent intent = getIntent();
        if (intent != null) {
            shop_name = intent.getStringExtra("shopName");
            order_date = intent.getStringExtra("orderDate");
            order_no = intent.getStringExtra("orderNO");
        }

    }
}
