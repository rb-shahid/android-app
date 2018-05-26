package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import co.dtechsystem.carefer.Google.Analytics.AnalyticsApplication;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Loading;
import co.dtechsystem.carefer.Utils.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.google.firebase.analytics.FirebaseAnalytics;


public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
     Loading loading;
     Loading loading2;

    protected Activity activity;
     AQuery aQuery;
    Gson gson;
    Intent intent;
    protected String sUser_Mobile = "";
    protected String sUser_Mobile_Varify = "";
    protected String sPrivacy_check = "";
    String sUser_ID;
    String sRegId = "";
    Locale locale, localeEn;
    private AnalyticsApplication sAnalyticsApplication;
    private FirebaseAnalytics firebaseAnalytics;

    Tracker mTracker;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/jf_flat regular_0.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gson = new Gson();
        aQuery = new AQuery(this);
        activity = this;
        loading = new Loading(this, getResources().getString(R.string.loading));
        loading2 = new Loading(this, getResources().getString(R.string.loading));

        intent = getIntent();
        sAnalyticsApplication = (AnalyticsApplication) getApplication();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, activity.getLocalClassName());
        //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        mTracker = sAnalyticsApplication.getDefaultTracker();
        sRegId = Utils.readPreferences(activity, "regId", "");



        if (sRegId != null && !sRegId.equals("")) {
        } else {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Utils.savePreferences(activity, "regId", refreshedToken);
        }
        sUser_Mobile = Utils.readPreferences(activity, "User_Mobile", "");
        sUser_Mobile_Varify = Utils.readPreferences(activity, "User_Mobile_varify", "");
        sPrivacy_check = Utils.readPreferences(activity, "User_privacy_check", "");
        sUser_ID = Utils.readPreferences(activity, "User_ID", "");
        locale = new Locale("ar");
        localeEn = new Locale("en");


      /* Uncomment this code to enable multiple language selection
        String Language = Utils.readPreferences(activity, "language", "");
        if (Language != null && !Language.equals("")) {
            locale = new Locale(Language);
            }*/

        setLanguage(locale);

        disableSSLCertificateChecking();
    }

    /**
     * Handle multiple Language for application
     *
     * @param locale Takes Locality as a param to change locale of app
     */
    protected void setLanguage(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(localeEn);
        Locale.setDefault(localeEn);
        //noinspection deprecation
        resources.updateConfiguration(configuration, displayMetrics);
    }

    /**
     * Handle button click for back and map click to go to home back
     *
     * @param v
     */
    public void GotoHome(@SuppressWarnings("UnusedParameters") View v) {
        Intent i = new Intent(activity, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    /**
     * Handle close activity generic function can use anywhere in app activities
     *
     * @param v
     */
    @SuppressWarnings({"unused", "UnusedParameters"})
    public void CloseActivity(View v) {
        finish();
    }

    /**
     * Show a small toast message to user for progress
     *
     * @param msg takes String as param
     */
    @SuppressWarnings("unused")
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * show a small dialog alert with some message to user
     * @param msg takes String as param
     */
    @SuppressWarnings("unused")
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create().show();
    }

    /**
     * Show progress for tasks running in background
     * @param msg takes String as param
     */
    @SuppressWarnings("unused")
    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    /**
     * Close progress dialog as if it is running in background
     */
    private void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Handle user touch on other then edit text area and hide the keyboard
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Utils.hideKeyboard(activity);
        return true;
    }

    /**
     * Handle user touch event
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Handle ssl certification for api calls which uses https:// method .It will disable ssl for response
     */
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void SendFireBaseError(String exception){
        FirebaseCrash.report(new Exception(exception));
    }
}