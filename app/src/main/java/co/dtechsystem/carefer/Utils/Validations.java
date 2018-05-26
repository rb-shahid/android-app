package co.dtechsystem.carefer.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.dtechsystem.carefer.UI.Activities.NoInternetConnection;


public abstract class Validations {

	/**
	 * Check Internet Connectivity
	 * Input:
	 * 1. Context
	 * Output: True / False
	 */
	public static boolean isInternetAvailable(Context context, boolean isDisplayToast){

		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

		final boolean connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

		if (!connected) {
			if(isDisplayToast){
//				Toast.makeText(context,context.getResources().getString(R.string.toast_no_internet_avail), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent( context.getApplicationContext(), NoInternetConnection.class );
				context.startActivity( intent );
			}

			return false;
		}
		return true;
	}

	/**
	 * Display Toast Message
	 * Inputs:
	 * 1. Context
	 * 2. Message
	 * Output: It will display Toast of given message
	 */
	private static void showMessage(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Check String is Empty or not
	 * Input:
	 * 1. String
	 * Output: True / False
	 */
	public static boolean isEditTextEmpty(String text)
	{
		return TextUtils.isEmpty(text.trim());
	}

	/**
	 * Check EditText is Empty or not
	 * Input:
	 * 1. EditText
	 * Output: True / False
	 */
	private static boolean isEditTextEmpty(EditText et)
	{
		return TextUtils.isEmpty(et.getText().toString().trim());
	}


	/**
	 * Regex pattern for Email Validation
	 */
	private static String regExpnEmail =
			"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
					+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
					+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
					+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
					+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
					+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
	private final static Pattern EMAIL_ADDRESS_PATTERN= Pattern.compile(regExpnEmail, Pattern.CASE_INSENSITIVE);

	/**
	 * Check Email String is Valid or Not
	 * Input:
	 * 1. Email String
	 * Output: True / False
	 */
	public static boolean isValidEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	/**
	 * Check Email EditText is valid or not and display Right Drawable icon
	 * Inputs:
	 * 1. Context
	 * 2. EditText
	 * Output: True / False and also display right drawable of provided resource if it is false
	 */
	public static boolean isValidEmail(Context context, EditText etEmail) {
		if(isEditTextEmpty(etEmail)){
			showMessage(context, "Please enter email address");
			return true;
		}else return EMAIL_ADDRESS_PATTERN.matcher(etEmail.getText().toString().trim()).matches();
	}

	/**
	 * Check Password EditText is Valid or Not
	 * Inputs:
	 * 1. Context
	 * 2. EditText of Password
	 * Output: True / False and also display toast if password is wrong
	 */
	public static boolean isValidPassword(Context context, EditText etPassword) {
		String temp = etPassword.getText().toString();

		if(temp.length()>0 && isEditTextEmpty(etPassword)){
//			showMessage(context, context.getResources().getString(R.string.v_password_valid));
			return false;
		}
        return !isEditTextEmpty(etPassword);
	}

	public static boolean isValidPhoneNumber(CharSequence target) {
		if (target.length() < 10) {
			return false;
		} else {
			return Patterns.PHONE.matcher(target).matches();
		}
	}



	/**
	 * It will return Date string according to given timestamp and Date format
	 * Inputs:
	 * 1. Timestamp
	 * 2. Require Date Format
	 * Output: Date String
	 */
	public static String getDateFromTimestamp(long timestamp, String DateFormate){

		SimpleDateFormat sdf = new SimpleDateFormat(DateFormate);
		sdf.setTimeZone(TimeZone.getDefault());
		if(timestamp>0){
			Date milidate = new Date(timestamp);
			return sdf.format(milidate);
		}else{
			return "";
		}
	}
	public static boolean isValidYouTubeUrl(String url) {

		if (url == null) {
			return false;
		}

		if (Patterns.WEB_URL.matcher(url).matches()) {
			// Check host of url if youtube exists
			Uri uri = Uri.parse(url);
			if ("www.youtube.com".equals(uri.getHost())||"m.youtube.com".equals(uri.getHost())) {
				return true;
			}
			// Other way You can check into url also like
			//if (url.startsWith("https://www.youtube.com/")) {
			//return true;
			//}
		}
		// In other any case
		return false;
	}

	public static String extractYTId(String ytUrl) {
		String vId = null;
		Pattern pattern = Pattern.compile(
				"^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(ytUrl);
		if (matcher.matches()){
			vId = matcher.group(1);
		}
		return vId;
	}
	public static String expandUrl(String shortenedUrl)  {
		URL url;
		String expandedURL = "";
		try {
			url = new URL(shortenedUrl);
			// open connection
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
			// stop following browser redirect
			httpURLConnection.setInstanceFollowRedirects(false);
			// extract location header containing the actual destination URL
			expandedURL = httpURLConnection.getHeaderField("Location");
			httpURLConnection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expandedURL;
	}
}
