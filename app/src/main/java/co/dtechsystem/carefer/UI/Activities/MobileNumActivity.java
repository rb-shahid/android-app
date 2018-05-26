package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lamudi.phonefield.PhoneEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.SessionClass;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MobileNumActivity extends BaseActivity {
    private PhoneEditText phoneEditText;
    private Button submit_button;
    private String CountryID;
    private TelephonyManager tm;
    private String countryCode;
    private String number="0000000000";
    InputStream inStream = null;
    HurlStack hurlStack = null;
    String MESSAGE_KEY ;
    String Phone ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num);
        submit_button = (Button) findViewById(R.id.submit_button);
        phoneEditText = (PhoneEditText) findViewById(R.id.edit_text);
        Phone = phoneEditText.toString();
        intent.putExtra(MESSAGE_KEY,Phone);
        phoneDropAndValid();
        AutoDetectMobileSim1();

        /*
        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

            } else {
                AutoDetectMobileSim1();
            }
        } else {
            AutoDetectMobileSim1();

        }
        */


    }

    /**
     * Handle user Mobile number to auto fetch from the mobile
     */
    private void AutoDetectMobileSim1() {

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);

                return;
            }
            tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                CountryID = tm.getNetworkCountryIso();
            }
            number = tm.getLine1Number();

            if(number!=null&&!number.isEmpty()) {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, CountryID.toUpperCase());
                countryCode = String.valueOf(numberProto.getCountryCode());
            }

            if (number.startsWith("00")) {
                number = number.replaceFirst("00", "");
            }
            if (number.startsWith("0")) {
                number = number.replaceFirst("0", "");
            }
            if (countryCode!=null&&number.startsWith(countryCode)) {
                number = number.replaceFirst(countryCode, "");
            }
//            if (!number.startsWith("+")) {
//                number = "+" + number;
//            }
            if (CountryID != null && !CountryID.equals("")) {
                phoneEditText.setDefaultCountry(CountryID);
            } else {
                phoneEditText.setDefaultCountry("SA");
            }

            phoneEditText.getEditText().setText(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle user mobile number if it is valid or not
     */
    private void phoneDropAndValid() {

        assert phoneEditText != null;
        assert submit_button != null;

//        phoneEditText.setHint(R.string.phone_hint);
        if (CountryID != null && !CountryID.equals("")) {
            phoneEditText.setDefaultCountry(CountryID);
        } else {
            phoneEditText.setDefaultCountry("SA");
        }
//        Utils.gradientTextView(submit_button, activity);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations.isInternetAvailable(activity, true)) {
                    if (phoneEditText.getPhoneNumber() != null && !phoneEditText.getPhoneNumber().equals("")) {
                        String Phone = phoneEditText.getPhoneNumber();
                        if (Phone != null && !Phone.equals("")) {
                            if (phoneEditText.isValid()) {
                                loading.show();
                                if (Phone.startsWith("+")) {
                                    Phone = Phone.replaceFirst("\\u002B", "");
                                }
                                SessionClass.userPhoneNum = Phone;
                                APiCreateUserPhone(Phone);
                            } else {
                                showToast(getResources().getString(R.string.invalid_phone_number));
                            }
                        }
                    } else {
                        showToast(getResources().getString(R.string.enter_mobile));
                    }
                }
            }
        });

    }

    /**
     * Web API for create user profile for first time
     *
     * @param customerMobile Takes String as pram for customer mobile
     */
    private void APiCreateUserPhone(final String customerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiCreateUserPhone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject customerDetails = jsonObject.getJSONObject("customer");
                            String smsAPIResponse = customerDetails.getString("smsAPIResponse");
                            String customerMobile = customerDetails.getString("customerMobile");
                            if (smsAPIResponse != null && !smsAPIResponse.equals("SMS sent successfully.")) {
                                showToast(smsAPIResponse);
                            } else {
                                String ID = customerDetails.getString("ID");
                                Utils.savePreferences(activity, "User_ID", ID);
                                Utils.savePreferences(activity, "User_Mobile", customerMobile);
                                Intent i = new Intent(activity, MobileNumVerifyActivity.class);
                                startActivity(i);
                                finish();
                                showToast(getResources().getString(R.string.toast_verfication_sent_mobile));
                                loading.close();
                            }
                        } catch (JSONException e) {
                            SendFireBaseError(String.valueOf(e));
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileNumber", customerMobile);

                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AutoDetectMobileSim1();
                }
            }

        }
    }
}
