package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;


import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;

public class SplashActivity extends BaseActivity{
    AdsManager adsManager;
    int DELAY_SPLASH = 1000;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        adsManager = new AdsManager(this);
        adsManager.initializeAd();

        if (xProfile.AD_NETWORK.equals(ADMOB) || xProfile.AD_NETWORK.equals(GOOGLE_AD_MANAGER)) {
            showAppOpenAdIfAvailable(true);
        } else {
            showAppOpenAdIfAvailable(false);
        }
    }


    private void showAppOpenAdIfAvailable(boolean showAd) {
        if (showAd) {
            if (xProfile.APP_OPEN_AD_START) {
                adsManager.loadAppOpenAd(xProfile.APP_OPEN_AD_START, this::startMainActivity);
            } else {
                startMainActivity();
            }
        } else {
            startMainActivity();
        }
    }

    public void onDestroy() {
       // startMainActivity();
        super.onDestroy();
    }

    private void startMainActivity() {
        AdsManager.postDelayed(()-> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }, DELAY_SPLASH);
    }

}

