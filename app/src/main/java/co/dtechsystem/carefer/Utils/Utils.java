package co.dtechsystem.carefer.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import co.dtechsystem.carefer.R;

public abstract class Utils {
    public static void savePreferences(Context activity, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean isLocationServiceEnabled(Activity activity) {
        LocationManager lm = (LocationManager)
                activity.getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        return (provider!=null&&!provider.equals("") &&
                !LocationManager.PASSIVE_PROVIDER.equals(provider));
    }

    public static String readPreferences(Activity activity, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getString(key, defaultValue);
    }

    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {
        if (inputFormat.equals("")) { // if inputFormat = "", set a default input format.
            inputFormat = "yyyy-MM-dd hh:mm:ss";
        }
        if (outputFormat.equals("")) {
            outputFormat = "EEEE d 'de' MMMM 'del' yyyy"; // if inputFormat = "", set a default output format.
        }
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("en"));

        // You can set a different Locale, This example set a locale of Country Mexico.
//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;

    }

    //Share Data To public on clicks
    public static void SharePublic(Activity activity, String url, String packagename) {
        String message = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + activity.getApplication().getPackageName();
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setPackage(packagename);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("In Exception", e.printStackTrace());
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            activity.startActivity(i);
        }
    }

    public void makeToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @SuppressLint("LongLogTag")
    public static boolean ValidateNumberFromLibPhone(Context context, String number) {
        if (!number.startsWith("+")) {
            number = "+" + number;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil
                .getInstance();
        Phonenumber.PhoneNumber phNumberProto = null;

        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Log.e("********   Value ********" + tm.getNetworkCountryIso(), "");
            String CountryCode = tm.getNetworkCountryIso();
            Log.e("********   Upper Value ********" + CountryCode.toUpperCase(), "");
            // I set the default region to SA (Saudi Arabia)
            // You can find your country code here http://www.iso.org/iso/country_names_and_code_elements
            phNumberProto = phoneUtil.parse(number, "SA");

        } catch (NumberParseException e) {
            // if there's any error
            System.err
                    .println("NumberParseException was thrown: "
                            + e.toString());
        }

        // check if the number is valid
        boolean isValid = phoneUtil.isValidNumber(phNumberProto);

        if (isValid) {

            // get the valid number's international format
            String internationalFormat = phoneUtil.format(
                    phNumberProto,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);


            System.out.println("Validation" + internationalFormat);
            Log.e("Validate Number" + internationalFormat, "");
            return true;

        } else {

            // prompt the user when the number is invalid
            if (number.startsWith("+")) {
                number = number.replace("+", "");
            }
            Toast.makeText(context, context.getResources().getString(R.string.invalid_phone_number) + ": \n" + number,
                    Toast.LENGTH_LONG).show();
            return false;

        }

    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void gradientTextView(TextView textView, Activity activity) {
        int[] rainbow = getRainbowColors(activity);
        Shader shader = new LinearGradient(100, 110, 0, 0, rainbow,
                null, Shader.TileMode.MIRROR);

        Matrix matrix = new Matrix();
        matrix.setRotate(360);
        shader.setLocalMatrix(matrix);
        textView.getPaint().setShader(shader);
    }

    public static void gradientTextViewLong(TextView textView, Activity activity) {
        int[] rainbow = getRainbowColors(activity);
        Shader shader = new LinearGradient(100, 150, 0, 0, rainbow,
                null, Shader.TileMode.MIRROR);

        Matrix matrix = new Matrix();
        matrix.setRotate(360);
        shader.setLocalMatrix(matrix);
        textView.getPaint().setShader(shader);
    }

    public static void gradientTextViewShort(TextView textView, Activity activity) {
        int[] rainbow = getRainbowColors(activity);
        Shader shader = new LinearGradient(0, 0, 100, 100, rainbow,
                null, Shader.TileMode.MIRROR);

        Matrix matrix = new Matrix();
        matrix.setRotate(360);
        shader.setLocalMatrix(matrix);
        textView.getPaint().setShader(shader);
    }

    private static int[] getRainbowColors(Activity activity) {
        return new int[]{
                activity.getResources().getColor(R.color.colorMandy),
                activity.getResources().getColor(R.color.colorPrimary),
//                activity.getResources().getColor(R.color.colorGreylight),
//                activity.getResources().getColor(R.color.colorPrimary),
        };
    }
    private void getCallDetails(Activity activity) {
        StringBuffer sb = new StringBuffer();
        @SuppressWarnings("UnusedAssignment") Uri contacts = CallLog.Calls.CONTENT_URI;
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") HashMap rowDataCall;
//        Cursor managedCursor = getContentResolver().query(contacts, null, null, null, null);
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Cursor managedCursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

        int number = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.NUMBER) : 0;
        int type = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.TYPE) : 0;
        int date = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.DATE) : 0;
        int duration = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.DURATION) : 0;
        sb.append("Call Details :");
        while (managedCursor != null && managedCursor.moveToNext()) {

            //noinspection UnusedAssignment
            rowDataCall = new HashMap<>();

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callDayTime = new Date(Long.valueOf(callDate)).toString();
            // long timestamp = convertDateToTimestamp(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");


        }
        assert managedCursor != null;
        managedCursor.close();
        System.out.println(sb);
    }
}
