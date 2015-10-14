package com.zohaltech.app.essentialwords.classes;


import com.zohaltech.app.essentialwords.R;

public final class ConstantParams {

    private static String apiSecurityKey  = App.context.getString(R.string.jan);

    public static String getApiSecurityKey() {
        return apiSecurityKey;
    }
}
