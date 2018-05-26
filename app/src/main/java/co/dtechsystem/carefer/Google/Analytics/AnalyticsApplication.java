package co.dtechsystem.carefer.Google.Analytics;

import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import co.dtechsystem.carefer.R;

/**
 * Created by DELL on 7/7/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class AnalyticsApplication extends MultiDexApplication {
    private Tracker mTracker;
    private static AnalyticsApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }

    synchronized public Tracker getDefaultTracker()
    {
        if (mTracker == null)
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(AnalyticsApplication.this);

            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    private synchronized Tracker getGoogleAnalyticsTracker()
    {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    public void trackEvent(String category, String action, String label)
    {
        Tracker t = getDefaultTracker();
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    public static synchronized AnalyticsApplication getInstance()
    {
        return mInstance;
    }

    public void trackScreenView(String screenName)
    {
        Tracker t = getGoogleAnalyticsTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e)
    {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription( new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }
}
