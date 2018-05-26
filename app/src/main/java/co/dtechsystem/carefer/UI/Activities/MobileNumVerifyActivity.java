package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.SessionClass;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MobileNumVerifyActivity extends BaseActivity {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    private EditText et_1_verify;
    private EditText et_2_verify;
    private EditText et_3_verify;
    private EditText et_4_verify;
    private TextView chronometer_sms , mobileNumber;
    private TextView chronometer_smscounter;
    private boolean mAutoReceivedCode = false;
    private CountDownTimer mCountDownTimer;
    private String mCustomerMobile = null;
    String MESSAGE_KEY ;
    Button btn_resend_code ;
    String v;

    @SuppressLint("ResourceAsColor")
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mobile_num_verification);
        Button btn_verify_pin = (Button) findViewById(R.id.btn_verify_pin);
        et_1_verify = (EditText) findViewById(R.id.et_1_verify);
        et_2_verify = (EditText) findViewById(R.id.et_2_verify);
        et_3_verify = (EditText) findViewById(R.id.et_3_verify);
        et_4_verify = (EditText) findViewById(R.id.et_4_verify);
        chronometer_sms = (TextView) findViewById(R.id.chronometer_sms);
        Button btn_resend_code = (Button)findViewById(R.id.btn_resend_code);
        btn_resend_code.setTextColor(getResources().getColor(R.color.btn_chnge) );
        Button btn_re_enter_mobile = (Button)findViewById(R.id.btn_re_enter_mobile);
        chronometer_smscounter = (TextView) findViewById(R.id.chronometer_smscounter);
        mobileNumber = (TextView) findViewById(R.id.mobileNumber);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MESSAGE_KEY);
        mobileNumber.setText(SessionClass.userPhoneNum);
        SetFocusForEdit();
        setdataToViews();
        getData();
        //openHack();

        if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
            btn_re_enter_mobile.setVisibility(View.GONE);
        } else {
            btn_re_enter_mobile.setVisibility(View.VISIBLE);
        }
        btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
                    APiCreateUserPhone(mCustomerMobile);
                } else {
                    APiCreateUserPhone(sUser_Mobile);
                }
                // dialog.dismiss();
                StartTimer(120000);
            }
        });
        btn_re_enter_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MobileNumActivity.class);
                Utils.savePreferences(activity, "User_Mobile", "");
                Utils.savePreferences(activity, "User_ID", "");
                //dialog.dismiss();
                startActivity(i);
                finish();
            }
        });
        //  dialog.show();
    }



    private void openHack() {
        Intent i = new Intent(activity, CareferPolicyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    /**
     * Receive All intents send from previous activity
     */
    private void getData() {
        if (intent.getExtras() != null) {
            mCustomerMobile = intent.getStringExtra("customerMobile");
        }
    }

    /**
     * Set Data to views as received from all intents
     */
    private void setdataToViews() {
//        btn_resend_code.setVisibility(View.GONE);
        StartTimer(120000);
//        btn_resend_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firstSms = false;
//                btn_resend_code.setVisibility(View.GONE);
//                StartTimer();
//            }
//        });
    }

    /**
     * Start a timer of 2 minutes wait for code receive
     *
     * @param Time
     */
    private void StartTimer(long Time) {
        mCountDownTimer = new CountDownTimer(Time, 1000) {
            @SuppressLint("ResourceType")
            public void onTick(long millisUntilFinished) {
                chronometer_sms.setVisibility(View.VISIBLE);
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                chronometer_sms.setText(getResources().getString(R.string.toast_verfication_sent_mobile));
                chronometer_smscounter.setText(getResources().getString(R.string.toast_counter)+" " + v + ":" + String.format("%02d", va ));
                //mobileNumber.setText(getResources().getString(R.id.edit_text));
            }

            @SuppressLint("ResourceAsColor")
            public void onFinish() {
                chronometer_sms.setText(getResources().getString(R.string.toast_verfication_sent_mobile) );
                chronometer_smscounter.setText(getResources().getString(R.string.toast_counter)+" " + "00:00" );


                if (!mAutoReceivedCode) {
                    CustomResendCodeDialog();
                }
//                    chronometer_sms.setVisibility(View.GONE);
//                    btn_resend_code.setVisibility(View.VISIBLE);
            }
        };


        mCountDownTimer.start();

    }


    @SuppressWarnings("UnusedParameters")
/**
 * Handle user click when the user received his verification code
 */
    public void ben_Next_to_carafePolicy_Click(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
            if (et_1_verify.length() > 0 && et_2_verify.length() > 0 && et_3_verify.length() > 0 && et_4_verify.length() > 0) {
                String VerificationCode = et_1_verify.getText().toString() + et_2_verify.getText().toString() +
                 et_3_verify.getText().toString() + et_4_verify.getText().toString();
                loading.show();
                APiVarifyCustomer(sUser_ID, VerificationCode, sRegId, mCustomerMobile);

            } else {
                showToast(getResources().getString(R.string.toast_fill_all_fields));
            }

        }
    }

    /**
     * Handle focus for small edit texts uses for input of varify numbers
     */
    private void SetFocusForEdit() {
//        Utils.gradientTextView(btn_verify_pin, activity);
        et_1_verify.setFocusableInTouchMode(true);
        et_1_verify.requestFocus();
        et_1_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_2_verify.setFocusableInTouchMode(true);
                    et_2_verify.requestFocus();
                } else {
                    et_1_verify.setFocusableInTouchMode(true);
                    et_1_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_2_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_3_verify.setFocusableInTouchMode(true);
                    et_3_verify.requestFocus();
                } else {
                    et_2_verify.setFocusableInTouchMode(true);
                    et_2_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_3_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_4_verify.setFocusableInTouchMode(true);
                    et_4_verify.requestFocus();
                } else {
                    et_3_verify.setFocusableInTouchMode(true);
                    et_3_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_4_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_4_verify.clearFocus();
                    Utils.hideKeyboard(activity);
                } else {
                    et_4_verify.setFocusableInTouchMode(true);
                    et_4_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
    }

    /**
     * Show a custom dialog to user to resend code and change number after 2 minutes
     */
    private void CustomResendCodeDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_resend_code);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_resend_code = (Button) dialog.findViewById(R.id.btn_resend_code);
        Button btn_re_enter_mobile = (Button) dialog.findViewById(R.id.btn_re_enter_mobile);
        Button btn_cancel_mobile = (Button) dialog.findViewById(R.id.btn_cancel_mobile);
        // if button is clicked, close the custom dialog
        if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
            btn_re_enter_mobile.setVisibility(View.GONE);
        } else {
            btn_re_enter_mobile.setVisibility(View.VISIBLE);
        }
        btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
                    APiCreateUserPhone(mCustomerMobile);
                } else {
                    APiCreateUserPhone(sUser_Mobile);
                }
                // dialog.dismiss();
                StartTimer(120000);
            }
        });
        btn_re_enter_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MobileNumActivity.class);
                Utils.savePreferences(activity, "User_Mobile", "");
                Utils.savePreferences(activity, "User_ID", "");
                //dialog.dismiss();
                startActivity(i);
                finish();
            }
        });
        btn_cancel_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  dialog.show();
    }

    /**
     * Handle Receiving of massages as received Web API calls to varify the code
     */
    private final BroadcastReceiver SMSBroadcastReceiver = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Intent recieved: " + intent.getAction());

            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    assert pdus != null;
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > -1) {
                        String message = messages[0].getMessageBody();
                        String pin;
                        if (message.contains("Carefer")) {
                            pin = message.replaceAll("[^0-9]", "");
                            if (pin != null && pin.length() == 4) {
                                et_1_verify.setText(String.valueOf(pin.charAt(0)));
                                et_2_verify.setText(String.valueOf(pin.charAt(1)));
                                et_3_verify.setText(String.valueOf(pin.charAt(2)));
                                et_4_verify.setText(String.valueOf(pin.charAt(3)));
                                chronometer_sms.setVisibility(View.GONE);
                                mAutoReceivedCode = true;
                                loading.show();
                                APiVarifyCustomer(sUser_ID, pin, sRegId, mCustomerMobile);


                            }
//                            Toast.makeText(context, "Pin received: " + pin, Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }
        }
    };

    /**
     * Web API To varify customer pin code as entered by user
     * @param UserID Takes String as pram of customer ID
     * @param verificationCode  Takes Pin code received by user
     * @param regID Takes String as param registration id received from Fire base
     * @param mCustomerMobile Takes String as param of customer mobile number
     */
    private void APiVarifyCustomer(final String UserID, final String verificationCode, final String regID, final String mCustomerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        String Url = "";
        if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
            Url = AppConfig.APiVarifyCustomerNumberChange;
        } else {
            Url = AppConfig.APiVarifyCustomer;
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject customerDetails = jsonObject.getJSONObject("customer");
                            String Status = customerDetails.getString("statusCode");
                            if (Status.equals("1")) {
                                Utils.savePreferences(activity, "User_Mobile_varify", Status);
                                if (mCountDownTimer != null) {
                                    mCountDownTimer.cancel();
                                    mCountDownTimer = null;
                                }
                                if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
                                    Utils.savePreferences(activity, "User_Mobile", mCustomerMobile);
                                } else {
                                    Intent i = new Intent(activity, CareferPolicyActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                                loading.close();
                                showToast(getResources().getString(R.string.toast_mobile_Verified));
                                finish();
                            } else {
                                showToast(getResources().getString(R.string.invalid_phone_number));
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
                params.put("customerID", UserID);
                params.put("verificationCode", verificationCode);
                params.put("regID", regID);
                params.put("mobileType", "Android");
                if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
                    params.put("customerMobile", mCustomerMobile);
                }

                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /**
     * Web APi to create user profile as he click on resend code to given mobile number
     * @param customerMobile
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
                            if (smsAPIResponse != null && !smsAPIResponse.equals("SMS sent successfully.")) {
                                showToast(smsAPIResponse);
                                loading.close();
                            } else {
                                showToast(getResources().getString(R.string.toast_verfication_sent_mobile));
                                loading.close();
                            }
                        } catch (JSONException e) {
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
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        SendFireBaseError(String.valueOf(error));
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
        queue.add(postRequest);
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVED);
        filter.setPriority(999);
        registerReceiver(SMSBroadcastReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(SMSBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
//        if (mCustomerMobile != null && !mCustomerMobile.equals("")) {
//            finish();
//            super.onBackPressed();
//        }
    }
}
