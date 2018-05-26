package com.lamudi.phonefield;

import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;


class Country {

    private final String mCode;

    private final String mName;

    private final int mDialCode;
    private Locale mlocale = new Locale("ar");
    public Country(String code, String name, int dialCode) {
        mCode = code;
        mName = name;
        mDialCode = dialCode;
    }

    public String getCode() {
        return mCode;
    }

    public String getName() {
        return mName;
    }

    public int getDialCode() {
        return mDialCode;
    }



    public String getDisplayName() {
        return new Locale("", mCode).getDisplayCountry(mlocale);
    }

    public int getResId(Context context) {
        String name = String.format("country_flag_%s", mCode.toLowerCase());
        final Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }
}
