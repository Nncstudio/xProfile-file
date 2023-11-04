package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;

public class CropperActivity extends BaseActivity{
    static Bitmap btm;
    public CropImageView iCrop;
    String iFrom;
    AdsManager adsManager;
    public static void setMainBtm(Bitmap bitmap) {
        btm = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_cropper);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }
        iFrom = getIntent().getStringExtra(Constants.OPEN);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.loadBannerAd(xProfile.IS_BANNER);
        adsManager.loadInterstitialAd(xProfile.IS_INTERSTITIAL, xProfile.SHOW_INTER_COUNT);
        this.iCrop = findViewById(R.id.crop_image_view);
        iCrop.setImageBitmap(btm);
        iCrop.setCropMode(CropImageView.CropMode.SQUARE);
        findViewById(R.id.iLeft).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivDone).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new saveCrop().execute();
            }
        });
        findViewById(R.id.iRight).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        findViewById(R.id.iVertical).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip(iCrop, FlipDirection.VERTICAL);
            }
        });

        findViewById(R.id.iHorizontal).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip(iCrop, FlipDirection.HORIZONTAL);
            }
        });

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

    class saveCrop extends AsyncTask<Void, Bitmap, Bitmap> {

        public Bitmap doInBackground(Void... voidArr) {
            return iCrop.getCroppedBitmap();
        }

        public void onPostExecute(Bitmap bitmap) {
            Intent intent = null;
            if (iFrom.equalsIgnoreCase(Constants.OPEN_PROFILE)){
                ProfileActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, ProfileActivity.class);
            } else if (iFrom.equalsIgnoreCase(Constants.OPEN_DRIP)){
                DripActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, DripActivity.class);
            } else if (iFrom.equalsIgnoreCase(Constants.OPEN_PATTERN)){
                PatternActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, PatternActivity.class);
            }
            startActivityForResult(intent, 900);
            finish();
            adsManager.showInterstitialAd();
        }
    }
}
