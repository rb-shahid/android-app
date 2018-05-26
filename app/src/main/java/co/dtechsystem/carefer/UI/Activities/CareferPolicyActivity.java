package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Google.Analytics.AnalyticsApplication;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class CareferPolicyActivity extends BaseActivity {
    private TextView tv_title_policy;
    private RadioButton cb_carefer_policy;
    private Button next ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carefer_policy);
        tv_title_policy = (TextView) findViewById(R.id.tv_title_policy);
        cb_carefer_policy = (RadioButton) findViewById(R.id.cb_carefer_policy);
        next = (Button)findViewById( R.id.buttonNext );
//        SetShaderToViews();
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            APiCareferPolicyData(AppConfig.APiCareferPolicy, "Policy", "");
        }
    }

    /**
     * Handle shades of text view title of activity in multicolor
     */
//    private void SetShaderToViews() {
//        Utils.gradientTextView(tv_title_policy, activity);
//    }

    /**
     * Handle api call for get Carefer Policy data and submit it to server as user accept it
     * @param URL Takes String as param for url of web api
     * @param Type Takes String as param for type get policy data or post that user accept the policy
     * @param customerID Takes String param for customer id to post carefer policy accept.
     */
    private void APiCareferPolicyData(String URL, final String Type, final String customerID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (Type.equals("Policy")) {
                                JSONArray policyData = jsonObject.getJSONArray("policyData");
                                JSONObject jsonObject1 = policyData.getJSONObject(0);
                                aQuery.id(R.id.tv_carefer_policy_details).text(jsonObject1.getString("policyContent"));
                                loading.close();
                            } else {
                                String policyVerified = jsonObject.getString("policyVerified");
                                if (policyVerified.equals("true")) {
                                    Utils.savePreferences(activity, "User_privacy_check", "1");
                                    Intent i = new Intent(activity, MainActivity.class);
                                    startActivity(i);
                                    loading.close();
                                    showToast(getResources().getString(R.string.toast_logged_in));
                                    finish();
                                }

                            }

                        } catch (JSONException e) {
                            AnalyticsApplication.getInstance().trackException(e);
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                            SendFireBaseError(String.valueOf(e));
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AnalyticsApplication.getInstance().trackException(error);
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                        SendFireBaseError(String.valueOf(error));
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!Type.equals("Policy")) {
                    params.put("customerID", customerID);
                }


                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /**
     * Handle user click for next to Main Activity as user accept the policy
     * @param v
     */
    @SuppressWarnings("UnusedParameters")
    public void btn_Next_to_mainmenu_Click(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
            if (cb_carefer_policy.isChecked()) {
                loading.show();
                mTracker.send(new HitBuilders.EventBuilder()

                        .setCategory("CareferPolicyActivity ")

                        .setAction("View Policy")

                        .build());
//                next.setBackgroundColor(getResources().getColor( R.color.btn_green ));
                next.setClickable( true );
                APiCareferPolicyData(AppConfig.APiVarifyPolicy, "VarifyPolicy", sUser_ID);
            } else {
                showToast(getResources().getString(R.string.toast_carefer_policy));
            }
        }
    }

}
