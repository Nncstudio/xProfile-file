package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils;

import static com.solodroid.ads.sdk.util.Constant.IRONSOURCE;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdk.format.AppOpenAd;
import com.solodroid.ads.sdk.format.BannerAd;
import com.solodroid.ads.sdk.format.InterstitialAd;
import com.solodroid.ads.sdk.format.NativeAd;
import com.solodroid.ads.sdk.gdpr.GDPR;
import com.solodroid.ads.sdk.gdpr.LegacyGDPR;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.BuildConfig;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.OnCompleteListener;

public class AdsManager {

    Activity activity;
    AdNetwork.Initialize adNetwork;
    AppOpenAd.Builder appOpenAd;
    BannerAd.Builder bannerAd;
    InterstitialAd.Builder interstitialAd;
    NativeAd.Builder nativeAd;
    LegacyGDPR legacyGDPR;
    GDPR gdpr;

    public AdsManager(Activity activity) {
        this.activity = activity;
        this.legacyGDPR = new LegacyGDPR(activity);
        this.gdpr = new GDPR(activity);
        adNetwork = new AdNetwork.Initialize(activity);
        appOpenAd = new AppOpenAd.Builder(activity);
        bannerAd = new BannerAd.Builder(activity);
        interstitialAd = new InterstitialAd.Builder(activity);
        nativeAd = new NativeAd.Builder(activity);
    }

    public void initializeAd() {
        if (xProfile.AD_STATUS) {
            adNetwork.setAdStatus("1")
                    .setAdNetwork(xProfile.AD_NETWORK)
                    .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                    .setStartappAppId(xProfile.STARTAPP_ID)
                    .setUnityGameId(xProfile.UNITY_GAME_ID)
                    .setIronSourceAppKey(xProfile.IRONSOURCE_APP_KEY)
                    .setWortiseAppId(xProfile.WORTISE_APP_ID)
                    .setDebug(BuildConfig.DEBUG)
                    .build();
        }
    }

    public void loadAppOpenAd(boolean placement, OnShowAdCompleteListener onShowAdCompleteListener) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                appOpenAd = new AppOpenAd.Builder(activity)
                        .setAdStatus("1")
                        .setAdNetwork(xProfile.AD_NETWORK)
                        .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                        .setAdMobAppOpenId(xProfile.ADMOB_APP_OPEN_AD_ID)
                        .setAdManagerAppOpenId(xProfile.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID)
                        .setApplovinAppOpenId(xProfile.APPLOVIN_APP_OPEN_ID)
                        .setWortiseAppOpenId(xProfile.WORTISE_APP_OPEN_ID)
                        .build(onShowAdCompleteListener);
            } else {
                onShowAdCompleteListener.onShowAdComplete();
            }
        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }
    }

    public void loadAppOpenAd(boolean placement) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                appOpenAd = new AppOpenAd.Builder(activity)
                        .setAdStatus("1")
                        .setAdNetwork(xProfile.AD_NETWORK)
                        .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                        .setAdMobAppOpenId(xProfile.ADMOB_APP_OPEN_AD_ID)
                        .setAdManagerAppOpenId(xProfile.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID)
                        .setApplovinAppOpenId(xProfile.APPLOVIN_APP_OPEN_ID)
                        .setWortiseAppOpenId(xProfile.WORTISE_APP_OPEN_ID)
                        .build();
            }
        }
    }

    public void showAppOpenAd(boolean placement) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                appOpenAd.show();
            }
        }
    }

    public void destroyAppOpenAd(boolean placement) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                appOpenAd.destroyOpenAd();
            }
        }
    }

    public void loadBannerAd(boolean placement) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                bannerAd.setAdStatus("1")
                        .setAdNetwork(xProfile.AD_NETWORK)
                        .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                        .setAdMobBannerId(xProfile.ADMOB_BANNER_ID)
                        .setGoogleAdManagerBannerId(xProfile.GOOGLE_AD_MANAGER_BANNER_ID)
                        .setFanBannerId(xProfile.FAN_BANNER_ID)
                        .setUnityBannerId(xProfile.UNITY_BANNER_ID)
                        .setAppLovinBannerId(xProfile.APPLOVIN_BANNER_ID)
                        .setAppLovinBannerZoneId(xProfile.APPLOVIN_BANNER_ZONE_ID)
                        .setIronSourceBannerId(xProfile.IRONSOURCE_BANNER_ID)
                        .setWortiseBannerId(xProfile.WORTISE_BANNER_ID)
                        .setPlacementStatus(1)
                        .setLegacyGDPR(xProfile.LEGACY_GDPR)
                        .build();
            }
        }
    }

    public void loadInterstitialAd(boolean placement, int interval) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                interstitialAd.setAdStatus("1")
                        .setAdNetwork(xProfile.AD_NETWORK)
                        .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                        .setAdMobInterstitialId(xProfile.ADMOB_INTERSTITIAL_ID)
                        .setGoogleAdManagerInterstitialId(xProfile.GOOGLE_AD_MANAGER_INTERSTITIAL_ID)
                        .setFanInterstitialId(xProfile.FAN_INTERSTITIAL_ID)
                        .setUnityInterstitialId(xProfile.UNITY_INTERSTITIAL_ID)
                        .setAppLovinInterstitialId(xProfile.APPLOVIN_INTERSTITIAL_ID)
                        .setAppLovinInterstitialZoneId(xProfile.APPLOVIN_INTERSTITIAL_ZONE_ID)
                        .setIronSourceInterstitialId(xProfile.IRONSOURCE_INTERSTITIAL_ID)
                        .setWortiseInterstitialId(xProfile.WORTISE_INTERSTITIAL_ID)
                        .setInterval(interval)
                        .setPlacementStatus(1)
                        .setLegacyGDPR(xProfile.LEGACY_GDPR)
                        .build();
            }
        }
    }

    public void loadNativeAd(boolean placement) {
        if (placement) {
            if (xProfile.AD_STATUS) {
                nativeAd.setAdStatus("1")
                        .setAdNetwork(xProfile.AD_NETWORK)
                        .setBackupAdNetwork(xProfile.BACKUP_AD_NETWORK)
                        .setAdMobNativeId(xProfile.ADMOB_NATIVE_ID)
                        .setAdManagerNativeId(xProfile.GOOGLE_AD_MANAGER_NATIVE_ID)
                        .setFanNativeId(xProfile.FAN_NATIVE_ID)
                        .setAppLovinNativeId(xProfile.APPLOVIN_NATIVE_ID)
                        .setAppLovinDiscoveryMrecZoneId(xProfile.APPLOVIN_NATIVE_ZONE_ID)
                        .setWortiseNativeId(xProfile.WORTISE_NATIVE_ID)
                        .setPlacementStatus(1)
                        .setDarkTheme(true)
                        .setLegacyGDPR(false)
//                        .setNativeAdStyle(adsPref.getNativeAdStylePostDetails())
                        .setNativeAdBackgroundColor(R.color.BackgroundColor, R.color.BackgroundColor)
                        .build();
                nativeAd.setNativeAdPadding(
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp)
                );
                nativeAd.setNativeAdMargin(
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp),
                        activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
                );
            }
        }
    }

    public void showInterstitialAd() {
        if (xProfile.AD_STATUS) {
            interstitialAd.show();
        }
    }

    public void destroyBannerAd() {
        if (xProfile.AD_STATUS) {
            bannerAd.destroyAndDetachBanner();
        }
    }

    public void resumeBannerAd(boolean placement) {
        if (xProfile.AD_STATUS && !xProfile.IRONSOURCE_BANNER_ID.equals("0")) {
            if (xProfile.AD_NETWORK.equals(IRONSOURCE) || xProfile.BACKUP_AD_NETWORK.equals(IRONSOURCE)) {
                loadBannerAd(placement);
            }
        }
    }

    public void updateConsentStatus() {
        if (xProfile.LEGACY_GDPR) {
            legacyGDPR.updateLegacyGDPRConsentStatus(xProfile.ADMOB_PUBLISHER_ID, xProfile.PRIVACY_POLICY_URL);
        } else {
            gdpr.updateGDPRConsentStatus();
        }
    }

    public static void postDelayed(OnCompleteListener onCompleteListener, int millisecond) {
        new Handler(Looper.getMainLooper()).postDelayed(onCompleteListener::onComplete, millisecond);
    }

}
