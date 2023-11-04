package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.solodroid.ads.sdk.format.AppOpenAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.BuildConfig;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.dialog.DetailsDialog;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.picker.activities.AlbumSelectActivity;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.picker.helpers.ConstantsCustomGallery;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.picker.models.Image;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.PreferenceUtil;
public class MainActivity extends BaseActivity implements DefaultLifecycleObserver{
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 123;
    private AppUpdateManager appUpdateManager;
    PreferenceUtil sharedPref;
    AdsManager adsManager;

    TOOL iTool = TOOL.NONE;

    public enum TOOL{
        NONE,
        PROFILE,
        DRIP,
        PATTERN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new PreferenceUtil(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }
        initAds();

        if (!BuildConfig.DEBUG) {
            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            inAppUpdate();
            inAppReview();
        }

       findViewById(R.id.iProfile).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               iTool = TOOL.PROFILE;
               ePic();
           }
       });
        findViewById(R.id.iPattern).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iTool = TOOL.PATTERN;
                ePic();
            }
        });
        findViewById(R.id.iDrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iTool = TOOL.DRIP;
                ePic();
            }
        });
        findViewById(R.id.iWorks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privacy = new Intent(MainActivity.this, WorkActivity.class);
                startActivity(privacy);
            }
        });

        findViewById(R.id.iShare).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myapp = new Intent(Intent.ACTION_SEND);
                myapp.setType("text/plain");
                myapp.putExtra(Intent.EXTRA_TEXT, getString(R.string.download_this) + "\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
                startActivity(myapp);
            }
        });

        findViewById(R.id.iRate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!BuildConfig.DEBUG) {
                    inAppReview();
                }
            }
        });

        findViewById(R.id.iPrivacy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("qq","moreApp");
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(xProfile.PRIVACY_POLICY_URL)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(xProfile.PRIVACY_POLICY_URL)));
                }
            }
        });

        findViewById(R.id.iFeedback).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{xProfile.FEEDBACK_EMAIL});
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                startActivity(emailIntent);
            }
        });

        findViewById(R.id.iMore).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("qq","moreApp");
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(xProfile.DEVELOPER_ACCOUNT_URL)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(xProfile.DEVELOPER_ACCOUNT_URL)));
                }
            }
        });
    }
    public void initAds() {
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadAppOpenAd(xProfile.APP_OPEN_AD_RESUME);
        adsManager.loadInterstitialAd(xProfile.IS_INTERSTITIAL, xProfile.SHOW_INTER_COUNT);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (AppOpenAd.isAppOpenAdLoaded) {
                adsManager.showAppOpenAd(xProfile.APP_OPEN_AD_RESUME);
            }
        }, 100);
    }

    @Override
    public void onDestroy() {
        xProfile.isAppOpen = false;
        destroyBannerAd();
        adsManager.destroyAppOpenAd(xProfile.APP_OPEN_AD_RESUME);
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        super.onDestroy();
    }

    public void destroyBannerAd() {
        adsManager.destroyBannerAd();
    }


    @Override
    public void onBackPressed() {
        exitApp();
    }


    public void exitApp() {
        finish();
        destroyBannerAd();
        adsManager.destroyAppOpenAd(xProfile.APP_OPEN_AD_RESUME);
        xProfile.isAppOpen = false;
    }

    private void ePic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] arrPermissionsGrid = {"android.permission.READ_MEDIA_IMAGES"};
            Dexter.withContext(MainActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
                        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(MainActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError ->Toast.makeText(MainActivity.this, "Error occurred! ", LENGTH_SHORT).show()).onSameThread().check();

        } else {
            String[] arrPermissionsGrid = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29) arrPermissionsGrid=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            Dexter.withContext(MainActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
                        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(MainActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest>list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError ->Toast.makeText(MainActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        }
    }

    private void inAppReview() {
        if (sharedPref.getInAppReviewToken() <= 3) {
            sharedPref.updateInAppReviewToken(sharedPref.getInAppReviewToken() + 1);
            Log.d(TAG, "in app update token");
        } else {
            ReviewManager manager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    manager.launchReviewFlow(MainActivity.this, reviewInfo).addOnFailureListener(e -> {
                    }).addOnCompleteListener(complete -> {
                                Log.d(TAG, "Success");
                            }
                    ).addOnFailureListener(failure -> {
                        Log.d(TAG, "Rating Failed");
                    });
                }
            }).addOnFailureListener(failure -> Log.d(TAG, "In-App Request Failed " + failure));
            Log.d(TAG, "in app token complete, show in app review if available");
        }
        Log.d(TAG, "in app review token : " + sharedPref.getInAppReviewToken());
    }

    private void inAppUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    public void showSnackBar(String msg) {
        Toast.makeText(this, msg, LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                showSnackBar("Update canceled");
            } else if (resultCode == RESULT_OK) {
                showSnackBar("Update success!");
            } else {
                showSnackBar("Update Failed!");
                inAppUpdate();
            }
        }
        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    final Bitmap btm = BitmapFactory.decodeStream(imageStream);
                    send(btm);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void send(Bitmap btm) {
        CropperActivity.setMainBtm(btm);
        Intent intent = new Intent(this, CropperActivity.class);
        if (iTool == TOOL.PROFILE){
            intent.putExtra(Constants.OPEN, Constants.OPEN_PROFILE);
        } else if (iTool == TOOL.DRIP){
            intent.putExtra(Constants.OPEN, Constants.OPEN_DRIP);
        } else if (iTool == TOOL.PATTERN){
            intent.putExtra(Constants.OPEN, Constants.OPEN_PATTERN);
        }
        startActivityForResult(intent, 900);
        adsManager.showInterstitialAd();
    }


}