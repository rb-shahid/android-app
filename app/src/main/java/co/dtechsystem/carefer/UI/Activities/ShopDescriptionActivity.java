package co.dtechsystem.carefer.UI.Activities;

import android.os.Bundle;
import android.widget.TextView;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;


public class ShopDescriptionActivity extends BaseActivity {
    private String shopName;
    private String shopDescription;
    private TextView tv_shop_name_des;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_description);
        tv_shop_name_des = (TextView) findViewById(R.id.tv_shop_name_des);
        if (getIntent() != null) {
            shopName = intent.getStringExtra("shopName");
            shopDescription = intent.getStringExtra("shopDescription");
            aQuery.id(R.id.tv_shop_name_des).text(shopName);
            aQuery.id(R.id.tv_shop_description_des).text(shopDescription);
            SetShaderToViews();

        }
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextView(tv_shop_name_des, activity);
    }

}
