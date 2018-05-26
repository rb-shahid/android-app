package co.dtechsystem.carefer.UI.Activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lamudi.phonefield.PhoneEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class ChangeNumActivity extends Fragment {
    
    private PhoneEditText phoneEditText;

    View view;
    public MainActivity activity;

    private boolean fromstack;

    public ChangeNumActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.activity_change_mobail_number, container, false );

        Button change_button = (Button) view.findViewById( R.id.change_button );
        phoneEditText = (PhoneEditText) view.findViewById( R.id.edit_text );
        setUpToolbar();


        change_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations.isInternetAvailable( activity, false )) {
                    if (phoneEditText.getPhoneNumber() != null && !phoneEditText.getPhoneNumber().equals( "" )) {
                        String Phone = phoneEditText.getPhoneNumber();
                        if (Phone != null && !Phone.equals( "" )) {
                            if (phoneEditText.isValid()) {
                                activity.loading.show();
                                if (Phone.startsWith( "+" )) {
                                    Phone = Phone.replaceFirst( "\\u002B", "" );
                                }
                                APiCreateUserPhone( Phone );
                            } else {
                                activity.showToast( getResources().getString( R.string.invalid_phone_number ) );
                            }
                        }
                    } else {
                        activity.showToast( getResources().getString( R.string.enter_mobile ) );
                    }
                }
            }
        } );
        return  view ;


}

    private void APiCreateUserPhone(final String customerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest postRequest = new StringRequest( Request.Method.POST, AppConfig.APiCreateUserPhone,
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
                                activity.showToast(smsAPIResponse);
                            } else {
                                String ID = customerDetails.getString("ID");
                                Utils.savePreferences(activity, "User_ID", ID);
                                Utils.savePreferences(activity, "User_Mobile", customerMobile);
                                Intent i = new Intent(activity, MobileNumVerifyActivity.class);
                                startActivity(i);
                                activity.finish();
                                activity.showToast(getResources().getString(R.string.toast_verfication_sent_mobile));
                                activity.loading.close();
                            }
                        } catch (JSONException e) {
                            activity.SendFireBaseError(String.valueOf(e));
                            activity.showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            activity.loading.close();
                            e.printStackTrace();
                        }
                        activity.loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.loading.close();
                        activity.SendFireBaseError(String.valueOf(error));
                        activity.showToast(getResources().getString(R.string.some_went_wrong));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(MainActivity)context;
    }

    private void setUpToolbar(){
        TextView title= (TextView) activity.findViewById(R.id.toolbar_title);
        title.setText(R.string.change_num_mobile);

        final ImageButton back=(ImageButton) activity.findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        activity.findViewById(R.id.toolbar_edit).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_fav).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_filter).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_sort).setVisibility(View.GONE);
        activity.findViewById(R.id.searchfiled).setVisibility(View.GONE);


    }

}


