package co.dtechsystem.carefer;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import java.util.Locale;

import co.dtechsystem.carefer.UI.Activities.BaseActivity;
import co.dtechsystem.carefer.UI.Activities.CareferPolicyActivity;
import co.dtechsystem.carefer.UI.Activities.MainActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumVerifyActivity;
import co.dtechsystem.carefer.UI.Activities.enableLocation;

public class SplashActivity extends BaseActivity {
    private Locale locale;
    String Language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SplashScreenThread();
    }


    /* Uncomment this code to enable multiple language selection
     public String[] CustomLanguageDialog() {
 //        Language = Utils.readPreferences(activity, "language", "");
 //        if (Language != null && !Language.equals("")) {
 //            SplashScreenThread();
 //        } else {
 //            CustomLanguageDialog();
 //        }
         final String[] languageSelection = new String[1];
         final Dialog dialog = new Dialog(activity);
         dialog.setContentView(R.layout.lay_dialog_choose_language);
         dialog.setTitle(getResources().getString(R.string.dialog_language_choose));
         dialog.setCancelable(false);
         // set the custom dialog components - text, image and button
         Button btn_language_eng = (Button) dialog.findViewById(R.id.btn_language_eng);
         Button btn_language_ar = (Button) dialog.findViewById(R.id.btn_language_ar);
         Button btn_cancel_lang = (Button) dialog.findViewById(R.id.btn_cancel_lang);
         // if button is clicked, close the custom dialog

         btn_language_eng.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Utils.savePreferences(activity, "language", "en");
                 locale = new Locale("en");
                 setLanguage(locale);
                 dialog.dismiss();
                 SplashScreenThread();
             }
         });
         btn_language_ar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Utils.savePreferences(activity, "language", "ar");
                 locale = new Locale("ar");
                 setLanguage(locale);
                 dialog.dismiss();
                 SplashScreenThread();
             }
         });
         btn_cancel_lang.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Utils.savePreferences(activity, "language", "");
                 dialog.dismiss();
             }
         });

         dialog.show();
         return languageSelection;
     }
 */
    private void SplashScreenThread() {
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 /* Create an Intent that will start the Menu-Activity. */
                @SuppressWarnings("UnusedAssignment") Intent mainIntent = null;
                if (sUser_Mobile != null && !sUser_Mobile.equals("") && sUser_Mobile_Varify != null &&
                        sUser_Mobile_Varify.equals("1") && sPrivacy_check != null && sPrivacy_check.equals("1")) {
                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (sUser_Mobile != null && sUser_Mobile.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, MobileNumActivity.class);

                } else if (sUser_Mobile_Varify != null && sUser_Mobile_Varify.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, MobileNumVerifyActivity.class);
                } else if (sPrivacy_check != null && sPrivacy_check.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, CareferPolicyActivity.class);
                } else {
                    mainIntent = new Intent(SplashActivity.this, MobileNumActivity.class);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }



}
