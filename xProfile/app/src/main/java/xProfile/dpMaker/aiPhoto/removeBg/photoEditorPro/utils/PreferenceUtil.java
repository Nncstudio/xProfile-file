package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public SharedPreferences appSharedPrefs;
    public SharedPreferences.Editor prefsEditor;
    Context context;

    public PreferenceUtil(Context context) {
        this.context = context;
        this.appSharedPrefs = context.getSharedPreferences("xProfile_pref", Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }
    public Integer getInAppReviewToken() {
        return appSharedPrefs.getInt("in_app_review_token", 0);
    }

    public void updateInAppReviewToken(int value) {
        prefsEditor.putInt("in_app_review_token", value);
        prefsEditor.apply();
    }

}
