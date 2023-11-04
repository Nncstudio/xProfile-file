package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CornerImage;

public class DisplayActivity extends BaseActivity {
    CornerImage mMainImage;
    Uri imgUri;
    AdsManager adsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }

        mMainImage = (CornerImage) findViewById(R.id.iView);
        imgUri = Uri.parse(getIntent().getStringExtra(Constants.KEY_URI_IMAGE));
        mMainImage.setImageURI(imgUri);

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.ivShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
            }
        });

        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        LinearLayout nativeAdView = findViewById(R.id.iNative);
        Constants.setNativeAdStyle(this, nativeAdView, xProfile.NATIVE_STYLE);
        adsManager.loadNativeAd(xProfile.IS_NATIVE);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
