package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iBitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CornerImage;

public class PreviewActivity extends BaseActivity {
    CornerImage ivFb, ivInst, ivWhat;
    public static Bitmap btm;
    AdsManager adsManager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView(R.layout.activity_preview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (iBitmap != null) {
            btm = iBitmap;
        }
        iViews();
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.loadBannerAd(xProfile.IS_BANNER);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void iViews() {
        ivFb = findViewById(R.id.ivFacebook);
        ivInst = findViewById(R.id.ivInstagram);
        ivWhat = findViewById(R.id.ivWhatsapp);
        ivFb.setImageBitmap(btm);
        ivInst.setImageBitmap(btm);
        ivWhat.setImageBitmap(btm);
    }

    @Override
    public void onBackPressed() {
        finish();
        destroyBannerAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        adsManager.resumeBannerAd(xProfile.IS_BANNER);
    }



    private void destroyBannerAd() {
        adsManager.destroyBannerAd();
    }
}
