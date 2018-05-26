package co.dtechsystem.carefer.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class Loading {

    private ProgressDialog pd;

    public Loading(Context context, String msg) {

        pd = new ProgressDialog(context);
        pd.setMessage(msg);
        pd.setIndeterminate(true);
        pd.setCancelable(false);

    }

    //show loading on login and Notify
    public void show() {

        pd.show();
    }

    public void close() {

        pd.cancel();
    }


}
