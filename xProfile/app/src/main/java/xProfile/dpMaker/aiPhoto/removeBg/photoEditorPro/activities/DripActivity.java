package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import static android.content.ContentValues.TAG;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile.FORMAT;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.ITEM;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iBitmap;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iPortJson;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iStickerJson;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iVisible;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentation;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.DoubleAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.StickerAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.ToolAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.temAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.StickerViews;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.ScaleListener;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.RingModel;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.CheckNet;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Tools;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.SquareLayout;

public class DripActivity extends BaseActivity implements DoubleAdapter.ColorListener, ToolAdapter.OnItemSelected{
    public static Bitmap originalBtm, noBitmap;
    ImageView ivBack, ivGone,ivFront, ivImage;
    SquareLayout iSquare;
    RecyclerView rvBg, rvColor, rvTool, rvStickers;
    temAdapter dAdapter;
    StickerAdapter stickerAdapter;
    Tools currentMode = Tools.NONE;
    COLOR iColor =  COLOR.NONE;
    RingModel iPort = null, iSte = null;
    boolean iChecked =false;
    AdsManager adsManager;
    TabLayout tabStickers;
    private StickerViews mCurrentStickerView;

    public void addSticker(String url) {
        final StickerViews stickerView = new StickerViews(this);

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap btm, @Nullable Transition<? super Bitmap> transition) {
                        findViewById(R.id.ivGone).setVisibility(View.GONE);
                        stickerView.setImageBitmap(btm);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        findViewById(R.id.ivGone).setVisibility(View.VISIBLE);
                    }
                });



        stickerView.setOperationListener(new StickerViews.OperationListener() {
            @Override
            public void onDeleteClick() {
//                stickerViews.remove(stickerView);
                iSquare.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerViews stickerView) {
                mCurrentStickerView.setInEdit(false);
                mCurrentStickerView = stickerView;
                mCurrentStickerView.setInEdit(true);
            }

            @Override
            public void onTop(StickerViews stickerView) {

            }
        });

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        iSquare.addView(stickerView, lp);
        //stickerViews.add(stickerView);
        setCurrentEdit(stickerView);


        stickerView.setOnTouchListener((v, event) -> {
            return false;
        });


    }

    private void setCurrentEdit(StickerViews stickerView) {
        if (mCurrentStickerView != null) {
            mCurrentStickerView.setInEdit(false);
        }
        mCurrentStickerView = stickerView;
        stickerView.setInEdit(true);
    }

    public static void setMainBtm(Bitmap bitmap) {
        originalBtm = bitmap;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView(R.layout.activity_drip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.loadBannerAd(xProfile.IS_BANNER);
        adsManager.loadInterstitialAd(xProfile.IS_INTERSTITIAL, xProfile.SHOW_INTER_COUNT);

        try {
            iPort = (RingModel) new Gson().fromJson(iPortJson(this), RingModel.class);
            iSte = (RingModel) new Gson().fromJson(iStickerJson(this), RingModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onCreate: setFrame " + iPort.getData());
        Log.e(TAG, "onCreate: setFrame " + iSte.getData());
        iStart();

        findViewById(R.id.ivFront).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCurrentStickerView != null) {
                    mCurrentStickerView.setInEdit(false);
                }
                return false;
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivErase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EraseActivity.b = noBitmap;
                Intent i = new Intent(DripActivity.this, EraseActivity.class);
                i.putExtra(Constants.OPEN, Constants.OPEN_DRIP);
                startActivityForResult(i, 1024);
            }
        });

        findViewById(R.id.ivDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentStickerView != null) {
                    mCurrentStickerView.setInEdit(false);
                }
                new saveImage().execute();
            }
        });

        findViewById(R.id.ivPreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentStickerView != null) {
                    mCurrentStickerView.setInEdit(false);
                }
                Bitmap exported = getBitmapFromView(iSquare);
                if (exported != null){
                    iBitmap = exported;
                }
                Intent intent = new Intent(DripActivity.this, PreviewActivity.class);
                startActivityForResult(intent, 900);
            }
        });
        checkConnection();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (noBitmap != null) {
                ivImage.setImageBitmap(noBitmap);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void iStart() {
        iSquare = findViewById(R.id.iSquare);
        rvTool = findViewById(R.id.rvTool);
        rvColor = findViewById(R.id.rvColor);
        rvBg = findViewById(R.id.rvBg);
        tabStickers = findViewById(R.id.tabStickers);
        rvStickers = findViewById(R.id.rvStickers);
        ivBack = findViewById(R.id.ivBack);
        ivGone = findViewById(R.id.ivGone);
        ivFront = findViewById(R.id.ivFront);
        ivImage = findViewById(R.id.ivImage);
        createImageTransactor(originalBtm);
        Glide.with(this).load(R.drawable.iv_loading).into(ivGone);
        ivImage.setOnTouchListener(new ScaleListener(this, true));
        rvColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvBg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvTool.setAdapter(new ToolAdapter(true,this, this));
        rvColor.setAdapter(new DoubleAdapter(this, this, true));

        dAdapter = new temAdapter(DripActivity.this, false, iPort.getData().get(0),  new temAdapter.CallBack() {
            @Override
            public void selFrame(int i) {
                String image = iPort.getData().get(0).getProductDetails().get(i).getImage();
                String mask = iPort.getData().get(0).getProductDetails().get(i).getMask();
                setBg(xProfile.BACKEND_RESOURCES_URL+ITEM+image+FORMAT);
                setPort(xProfile.BACKEND_RESOURCES_URL+ITEM+mask+FORMAT);
            }
        });
        rvBg.setAdapter(dAdapter);

        for (int i = 0; i < iSte.getData().size(); i++) {
            tabStickers.addTab(tabStickers.newTab().setText(iSte.getData().get(i).getList_name()));
        }
        Log.e(TAG, "onCreate: " + tabStickers.getSelectedTabPosition());

        if (tabStickers.getSelectedTabPosition() == 0) {
            stickerAdapter = new StickerAdapter(DripActivity.this, iSte.getData().get(0), new StickerAdapter.CallBack() {
                @Override
                public void selFrame(int i) {
                    String image = iSte.getData().get(0).getProductDetails().get(i).getImage();
                    addSticker(xProfile.BACKEND_RESOURCES_URL + ITEM + image+".png");
                }
            });
            rvStickers.setAdapter(stickerAdapter);
        }
        tabStickers.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()  {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " + tab.getPosition());
                int selected;
                for ( selected = 0; selected < iSte.getData().size(); selected++) {
                    if (selected == tabStickers.getSelectedTabPosition()) {
                        int finalI = selected;
                        stickerAdapter = new StickerAdapter(DripActivity.this, iSte.getData().get(tabStickers.getSelectedTabPosition()), new StickerAdapter.CallBack() {
                            @Override
                            public void selFrame(int i1) {
                                String image = iSte.getData().get(finalI).getProductDetails().get(i1).getImage();
                                Log.e(TAG, "iClicked: " + iSte.getData().get(finalI).getProductDetails().get(i1).getImage());
                                addSticker(xProfile.BACKEND_RESOURCES_URL+ITEM+image+".png");

                            }
                        });
                        rvStickers.setAdapter(stickerAdapter);
                    }
                }
            }
        });
    }

    private void checkConnection() {
        CheckNet connectivityReceiver = new CheckNet(DripActivity.this);
        connectivityReceiver.isConnected();
    }

    @Override
    public void onColorSelected(DoubleAdapter.iModel iModel, DoubleAdapter.iModel2 iModel2, int i) {
        if (i == 0){
            if(iChecked){
                iChecked=false;
                iColor = COLOR.COL_1;
                rvColor.setAdapter(new DoubleAdapter(DripActivity.this, DripActivity.this, true));
            }else{
                iChecked=true;
                iColor = COLOR.COL_2;
                rvColor.setAdapter(new DoubleAdapter(DripActivity.this, DripActivity.this, false));
            }
        } else {
            if (iColor == COLOR.COL_1){
                ivBack.setColorFilter(iModel.drawableId);
                ivFront.setColorFilter(iModel.drawableId);
                iSquare.setBackgroundColor(iModel2.drawableId);
            } else if (iColor == COLOR.COL_2){
                ivBack.setColorFilter(iModel2.drawableId);
                ivFront.setColorFilter(iModel2.drawableId);
                iSquare.setBackgroundColor(iModel.drawableId);
            }
        }
    }



    @Override
    public void onToolSelected(Tools toolType) {
        if (mCurrentStickerView != null) {
            mCurrentStickerView.setInEdit(false);
        }
        this.currentMode = toolType;
        switch (toolType) {
            case DRIP:
                rvColor.setVisibility(View.GONE);
                findViewById(R.id.iStickers).setVisibility(View.GONE);
                iVisible(rvBg);
                break;
            case COLOR:
                iColor = COLOR.COL_1;
                rvBg.setVisibility(View.GONE);
                findViewById(R.id.iStickers).setVisibility(View.GONE);
                iVisible(rvColor);
                break;
            case STICKER:
                rvColor.setVisibility(View.GONE);
                rvBg.setVisibility(View.GONE);
                iVisible(findViewById(R.id.iStickers));
                break;
        }
    }

    enum COLOR {
        NONE,
        COL_2,
        COL_1
    }

    private void setPort(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap btm, @Nullable Transition<? super Bitmap> transition) {
                        findViewById(R.id.ivGone).setVisibility(View.GONE);
                        ivBack.setImageBitmap(btm);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        findViewById(R.id.ivGone).setVisibility(View.VISIBLE);
                    }
                });
    }


    private void setBg(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap btm, @Nullable Transition<? super Bitmap> transition) {
                        findViewById(R.id.ivGone).setVisibility(View.GONE);
                        ivFront.setImageBitmap(btm);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        findViewById(R.id.ivGone).setVisibility(View.VISIBLE);
                    }
                });

    }

    private MLImageSegmentationAnalyzer analyzer;

    private void createImageTransactor(Bitmap image) {
        MLImageSegmentationSetting setting = new MLImageSegmentationSetting.Factory().setAnalyzerType(MLImageSegmentationSetting.BODY_SEG).create();
        this.analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting);
        if (this.isChosen(image)) {
            MLFrame mlFrame = new MLFrame.Creator().setBitmap(image).create();
            Task<MLImageSegmentation>task = this.analyzer.asyncAnalyseFrame(mlFrame);
            task.addOnSuccessListener(new OnSuccessListener<MLImageSegmentation>() {
                @Override
                public void onSuccess(MLImageSegmentation mlImageSegmentationResults) {

                    if (mlImageSegmentationResults != null) {
                        noBitmap = mlImageSegmentationResults.getForeground();
                        ivImage.setImageBitmap(noBitmap);
                    } else {
                        displayFailure();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    displayFailure();
                }
            });
        } else {
             Toast.makeText(this.getApplicationContext(), R.string.txt_not_detect_human, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayFailure() {
        Toast.makeText(this.getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
    }

    private boolean isChosen(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        } else {
            return true;
        }
    }


    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    class saveImage extends android.os.AsyncTask<String, Bitmap, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap exported = getBitmapFromView(iSquare);
            return exported;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            AdjustActivity.setMainBtm(bitmap);
            Intent intent = new Intent(DripActivity.this, AdjustActivity.class);
            intent.putExtra("done","done");
            startActivityForResult(intent, 900);
            adsManager.showInterstitialAd();
        }
    }

    private void exit() {
        Dialog dialogOnBackPressed= new Dialog(this, R.style.Theme_Dialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setCancelable(false);
        dialogOnBackPressed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogOnBackPressed.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        TextView textViewCancel, textViewDiscard;
        textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        textViewCancel.setOnClickListener(view ->{
            dialogOnBackPressed.dismiss();
        });

        textViewDiscard.setOnClickListener(view ->{
            dialogOnBackPressed.dismiss();
            currentMode = null;
            finish();
        });

        dialogOnBackPressed.show();
    }

    @Override
    public void onBackPressed() {
        exit();
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
