package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities.SplashActivity;

public class xProfile extends Application {
    private static xProfile mInstance;
    private static xProfile myApplication;
    DatabaseReference iDatabase;

    public static boolean AD_STATUS = true;
    public static boolean APP_OPEN_AD_START = true;
    public static boolean APP_OPEN_AD_RESUME = true;
    public static  int SHOW_INTER_COUNT = 1;
    public static boolean IS_INTERSTITIAL = true;
    public static boolean IS_NATIVE = true;
    public static boolean IS_BANNER = true;
    public static String AD_NETWORK = "";
    public static String BACKUP_AD_NETWORK = "";
    public static  String ADMOB_PUBLISHER_ID = "";
    public static  String ADMOB_BANNER_ID = "";
    public static  String ADMOB_INTERSTITIAL_ID = "";
    public static  String ADMOB_NATIVE_ID = "";
    public static  String ADMOB_APP_OPEN_AD_ID = "";

    public static String GOOGLE_AD_MANAGER_BANNER_ID = "";
    public static String GOOGLE_AD_MANAGER_INTERSTITIAL_ID = "";
    public static String GOOGLE_AD_MANAGER_NATIVE_ID = "";
    public static String GOOGLE_AD_MANAGER_APP_OPEN_AD_ID = "";

    public static String FAN_BANNER_ID = "";
    public static String FAN_INTERSTITIAL_ID = "";
    public static String FAN_NATIVE_ID = "";

    public static String APPLOVIN_APP_OPEN_ID = "";
    public static String APPLOVIN_BANNER_ID = "";
    public static String APPLOVIN_INTERSTITIAL_ID = "";
    public static String APPLOVIN_NATIVE_ID = "";

    public static String APPLOVIN_BANNER_ZONE_ID = "";
    public static String APPLOVIN_INTERSTITIAL_ZONE_ID = "";
    public static String APPLOVIN_NATIVE_ZONE_ID = "";

    public static String IRONSOURCE_APP_KEY = "";
    public static String IRONSOURCE_BANNER_ID = "";
    public static String IRONSOURCE_INTERSTITIAL_ID = "";

    public static String UNITY_GAME_ID = "";
    public static String UNITY_BANNER_ID = "";
    public static String UNITY_INTERSTITIAL_ID = "";

    public static String STARTAPP_ID = "";

    public static String WORTISE_APP_ID = "";
    public static String WORTISE_APP_OPEN_ID = "";
    public static String WORTISE_BANNER_ID = "";
    public static String WORTISE_INTERSTITIAL_ID = "";
    public static String WORTISE_NATIVE_ID = "";

    public static final boolean LEGACY_GDPR = false;
    public static boolean isAppOpen = false;

    public static  String FEEDBACK_EMAIL = "";
    public static  String NATIVE_STYLE = "radio";
    public static  String PRIVACY_POLICY_URL = "";
    public static  String DEVELOPER_ACCOUNT_URL = "";
    public static  String BACKEND_RESOURCES_URL = "https://i.postimg.cc";
    public static  String FORMAT = ".png";

    public static void setApplication(xProfile application) {
        myApplication = application;
    }

    public static synchronized xProfile getInstance() {

        synchronized (xProfile.class) {
            synchronized (xProfile.class) {
                myApplication = mInstance;
            }
        }
        return myApplication;
    }


    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        setApplication(this);
        mInstance = this;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FirebaseApp.initializeApp(mInstance);
        fireBaseAD();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getResources().getString(R.string.oneSignal_id));
        OneSignal.setNotificationOpenedHandler(
                new OneSignal.OSNotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenedResult result) {
                        String launchUrl = result.getNotification().getLaunchURL();
                        Log.i("OneSignalExample", "launchUrl set with value: " + launchUrl);
                        if (launchUrl != null) {
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("openURL", launchUrl);
                            Log.i("OneSignalExample", "openURL = " + launchUrl);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void fireBaseAD() {
        iDatabase = FirebaseDatabase.getInstance().getReference().child("adConfig");
        iDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Config
                AD_STATUS = (boolean) dataSnapshot.child("ad_status").getValue();
                AD_NETWORK = dataSnapshot.child("ad_network").getValue().toString();
                BACKUP_AD_NETWORK = dataSnapshot.child("backup_ad_network").getValue().toString();
                BACKEND_RESOURCES_URL = dataSnapshot.child("resources_url").getValue().toString();
                SHOW_INTER_COUNT = dataSnapshot.child("show_inter_count").getValue().hashCode();
                APP_OPEN_AD_START = (boolean) dataSnapshot.child("app_open_ad_start").getValue();
                APP_OPEN_AD_RESUME = (boolean) dataSnapshot.child("app_open_ad_resume").getValue();

                //Admob
                ADMOB_PUBLISHER_ID = dataSnapshot.child("admob_publisher_id").getValue().toString();
                ADMOB_APP_OPEN_AD_ID = dataSnapshot.child("admob_app_open_id").getValue().toString();
                ADMOB_BANNER_ID = dataSnapshot.child("admob_banner_id").getValue().toString();
                ADMOB_INTERSTITIAL_ID = dataSnapshot.child("admob_interstitial_id").getValue().toString();
                ADMOB_NATIVE_ID = dataSnapshot.child("admob_native_id").getValue().toString();

                //AdManager
                GOOGLE_AD_MANAGER_APP_OPEN_AD_ID = dataSnapshot.child("adManager_app_open").getValue().toString();
                GOOGLE_AD_MANAGER_BANNER_ID = dataSnapshot.child("adManager_banner_id").getValue().toString();
                GOOGLE_AD_MANAGER_INTERSTITIAL_ID = dataSnapshot.child("adManager_interstitial_id").getValue().toString();
                GOOGLE_AD_MANAGER_NATIVE_ID = dataSnapshot.child("adManager_native_id").getValue().toString();

                //Fan
                FAN_BANNER_ID = dataSnapshot.child("fan_banner_id").getValue().toString();
                FAN_INTERSTITIAL_ID = dataSnapshot.child("fan_interstitial_id").getValue().toString();
                FAN_NATIVE_ID = dataSnapshot.child("fan_native_id").getValue().toString();

                //Applovin Max
                APPLOVIN_APP_OPEN_ID = dataSnapshot.child("applovin_max_app_open").getValue().toString();
                APPLOVIN_BANNER_ID = dataSnapshot.child("applovin_max_banner_id").getValue().toString();
                APPLOVIN_INTERSTITIAL_ID = dataSnapshot.child("applovin_max_interstitial_id").getValue().toString();
                APPLOVIN_NATIVE_ID = dataSnapshot.child("applovin_max_native_id").getValue().toString();

                //Applovin DISCOVERY
                APPLOVIN_BANNER_ZONE_ID = dataSnapshot.child("applovin_banner_zone_id").getValue().toString();
                APPLOVIN_INTERSTITIAL_ZONE_ID = dataSnapshot.child("applovin_interstitial_zone_id").getValue().toString();
                APPLOVIN_NATIVE_ZONE_ID = dataSnapshot.child("applovin_native_zone_id").getValue().toString();

                //IronSource
                IRONSOURCE_APP_KEY = dataSnapshot.child("ironSource_app_key").getValue().toString();
                IRONSOURCE_BANNER_ID = dataSnapshot.child("ironSource_banner_id").getValue().toString();
                IRONSOURCE_INTERSTITIAL_ID = dataSnapshot.child("ironSource_interstitial_id").getValue().toString();

                //Unity
                UNITY_GAME_ID = dataSnapshot.child("unity_game_id").getValue().toString();
                UNITY_BANNER_ID = dataSnapshot.child("unity_banner_id").getValue().toString();
                UNITY_INTERSTITIAL_ID = dataSnapshot.child("unity_interstitial_id").getValue().toString();

                //StartApp
                STARTAPP_ID = dataSnapshot.child("startApp_id").getValue().toString();

                //Wortise
                WORTISE_APP_ID = dataSnapshot.child("wortise_app_id").getValue().toString();
                WORTISE_APP_OPEN_ID = dataSnapshot.child("wortise_app_open_id").getValue().toString();
                WORTISE_BANNER_ID = dataSnapshot.child("wortise_banner_id").getValue().toString();
                WORTISE_INTERSTITIAL_ID = dataSnapshot.child("wortise_interstitial_id").getValue().toString();
                WORTISE_NATIVE_ID = dataSnapshot.child("wortise_native_id").getValue().toString();

                //Info
                FEEDBACK_EMAIL = dataSnapshot.child("feedback_email").getValue().toString();
                PRIVACY_POLICY_URL = dataSnapshot.child("privacy_policy_url").getValue().toString();
                DEVELOPER_ACCOUNT_URL = dataSnapshot.child("developer_account_url").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static Context getContext() {
        return mInstance.getContext();
    }


    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

}