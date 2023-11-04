package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import static android.content.ContentValues.TAG;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.ITEM;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iBitmap;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.iProfileJson;
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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.StickerAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.ToolAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.temAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.StickerViews;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.ScaleListener;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.RingModel;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.CheckNet;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.ColorFilterGenerator;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Tools;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.SquareLayout;

public class ProfileActivity extends BaseActivity implements ToolAdapter.OnItemSelected{
    private static Bitmap originalBtm;
    ImageView iBg, ivGone,ivImage;
    SquareLayout iSquare;
    RecyclerView rvProfile, rvTool, rvStickers;
    temAdapter pAdapter;
    StickerAdapter stickerAdapter;
    Tools currentMode = Tools.NONE;
    SeekBar sbRotate, sbColor;
    RingModel iProfile = null, iSte = null;

    AdsManager adsManager;
    TabLayout tabStickers, tabProfile;
    private StickerViews mCurrentStickerView;

    public static void setMainBtm(Bitmap bitmap) {
        originalBtm = bitmap;
    }

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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }

        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.loadBannerAd(xProfile.IS_BANNER);
        adsManager.loadInterstitialAd(xProfile.IS_INTERSTITIAL, xProfile.SHOW_INTER_COUNT);

        try {
            iProfile = (RingModel) new Gson().fromJson(iProfileJson(this), RingModel.class);
            iSte = (RingModel) new Gson().fromJson(iStickerJson(this), RingModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onCreate: setFrame " + iSte.getData());
        Log.e(TAG, "onCreate: setFrame " + iProfile.getData());
        iStart();

        findViewById(R.id.ivRound).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCurrentStickerView != null) {
                    mCurrentStickerView.setInEdit(false);
                }
                return false;
            }
        });

        findViewById(R.id.ivHorizontal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFlipper.flip(iBg, FlipDirection.HORIZONTAL);

            }
        });

        findViewById(R.id.ivVertical).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFlipper.flip(iBg, FlipDirection.VERTICAL);
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                Intent intent = new Intent(ProfileActivity.this, PreviewActivity.class);
                startActivityForResult(intent, 900);
            }
        });
        checkConnection();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void iStart() {
        iSquare = findViewById(R.id.iSquare);
        rvTool = findViewById(R.id.rvTool);
        rvProfile = findViewById(R.id.rvBg);
        tabStickers = findViewById(R.id.tabStickers);
        tabProfile = findViewById(R.id.tabProfile);
        rvStickers = findViewById(R.id.rvStickers);
        sbRotate = findViewById(R.id.sbRotate);
        sbColor = findViewById(R.id.sbColor);
        ivGone = findViewById(R.id.ivGone);
        iBg = findViewById(R.id.iBg);
        ivImage = findViewById(R.id.ivImage);
        ivImage.setImageBitmap(originalBtm);
        Glide.with(this).load(R.drawable.iv_loading).into(ivGone);
        ivImage.setOnTouchListener(new ScaleListener(this, true));
        rvProfile.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        iAdjust();
        rvTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvTool.setAdapter(new ToolAdapter(this,this, true));

        for (int i = 0; i < iProfile.getData().size(); i++) {
            tabProfile.addTab(tabProfile.newTab().setText(iProfile.getData().get(i).getList_name()));
        }
        Log.e(TAG, "onCreate: " + tabProfile.getSelectedTabPosition());

        if (tabProfile.getSelectedTabPosition() == 0) {
            pAdapter = new temAdapter(ProfileActivity.this, true, iProfile.getData().get(0), new temAdapter.CallBack() {
                @Override
                public void selFrame(int i) {
                    String image = iProfile.getData().get(0).getProductDetails().get(i).getImage();
                    setBg(xProfile.BACKEND_RESOURCES_URL + ITEM + image);
                }
            });
            rvProfile.setAdapter(pAdapter);
        }
        tabProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()  {
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
                for ( selected = 0; selected < iProfile.getData().size(); selected++) {
                    if (selected == tabProfile.getSelectedTabPosition()) {
                        int finalI = selected;
                        pAdapter = new temAdapter(ProfileActivity.this, true,iProfile.getData().get(tabProfile.getSelectedTabPosition()), new temAdapter.CallBack() {
                            @Override
                            public void selFrame(int i1) {
                                String image = iProfile.getData().get(finalI).getProductDetails().get(i1).getImage();
                                Log.e(TAG, "iClicked: " + iProfile.getData().get(finalI).getProductDetails().get(i1).getImage());
                                setBg(xProfile.BACKEND_RESOURCES_URL+ITEM+image);
                            }
                        });
                        rvProfile.setAdapter(pAdapter);
                    }
                }
            }
        });

        for (int i = 0; i < iSte.getData().size(); i++) {
            tabStickers.addTab(tabStickers.newTab().setText(iSte.getData().get(i).getList_name()));
        }
        Log.e(TAG, "onCreate: " + tabStickers.getSelectedTabPosition());

        if (tabStickers.getSelectedTabPosition() == 0) {
            stickerAdapter = new StickerAdapter(ProfileActivity.this, iSte.getData().get(0), new StickerAdapter.CallBack() {
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
                        stickerAdapter = new StickerAdapter(ProfileActivity.this, iSte.getData().get(tabStickers.getSelectedTabPosition()), new StickerAdapter.CallBack() {
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
        CheckNet connectivityReceiver = new CheckNet(ProfileActivity.this);
        connectivityReceiver.isConnected();
    }


    private void iAdjust() {
        sbRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("WrongConstant")
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i <= 50) {
                    iBg.setRotation((float) (((50 - i) * -360) / 50));
                } else {
                    iBg.setRotation((float) (((i - 50) * 360) / 50));
                }
            }
        });

        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("WrongConstant")
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = 0;
                if (i != 100) {
                    i2 = i - 100;
                }
                iBg.setColorFilter(ColorFilterGenerator.adjustHue((float) i2));
            }
        });

    }


    @Override
    public void onToolSelected(Tools toolType) {
        if (mCurrentStickerView != null) {
            mCurrentStickerView.setInEdit(false);
        }
        this.currentMode = toolType;
        switch (toolType) {
            case PROFILE:
                findViewById(R.id.iAdjust).setVisibility(View.GONE);
                findViewById(R.id.iStickers).setVisibility(View.GONE);
                iVisible(findViewById(R.id.iProfile));
                break;
            case ADJUST:
                findViewById(R.id.iProfile).setVisibility(View.GONE);
                iVisible(findViewById(R.id.iAdjust));
                findViewById(R.id.iStickers).setVisibility(View.GONE);
                break;
            case STICKER:
                findViewById(R.id.iProfile).setVisibility(View.GONE);
                iVisible(findViewById(R.id.iStickers));
                findViewById(R.id.iAdjust).setVisibility(View.GONE);

                break;
        }
    }


    private void setBg(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap btm, @Nullable Transition<? super Bitmap> transition) {
                        findViewById(R.id.ivGone).setVisibility(View.GONE);
                        iBg.setImageBitmap(btm);
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
            Intent intent = new Intent(ProfileActivity.this, AdjustActivity.class);
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
