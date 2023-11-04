package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.ToolAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.MultiTouchListener;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.AdsManager;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.ImageUtils;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Tools;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.EraseView;

public class EraseActivity extends BaseActivity implements OnClickListener, ToolAdapter.OnItemSelected{
    public static Bitmap bitmap = null;
    public static int curBgType = 1, orgBitHeight, orgBitWidth;
    public static BitmapShader patternBMPshader;
    public static Bitmap b = null;
    public RelativeLayout rlMain, rlSeekbar;
    ImageView ivOutSide, ivInSide, ivRedo, ivUndo, ivBg, ivPV, ivChange;
    public Bitmap orgBitmap;
    public EraseView eraseView;
    public int height, width;
    public boolean isTutOpen = true, showDialog = false;
    private LinearLayout llLasso, llEraser, llAuto;
    private SeekBar sbBrush, sbOffset, sbExtract, sbRadius, sbThreshold;
    private String openFrom;
    private RecyclerView rvTool;

    AdsManager adsManager;

    @SuppressLint({"WrongConstant"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        makeFullScreen();
        setContentView(R.layout.activity_erase);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            findViewById(R.id.vStatus).setVisibility(View.VISIBLE);
        }
        openFrom = getIntent().getStringExtra(Constants.OPEN);
        initUI();
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveView();
            }
        });
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.loadBannerAd(xProfile.IS_BANNER);
        adsManager.loadInterstitialAd(xProfile.IS_INTERSTITIAL, xProfile.SHOW_INTER_COUNT);
        this.isTutOpen = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx(this, 120.0f);
        curBgType = 1;
        this.rlMain.postDelayed(new Runnable() {
            public void run() {
                if (isTutOpen) {
                    ivBg.setImageBitmap(ImageUtils.getTiledBitmap(EraseActivity.this, R.drawable.tbg1, width, height));
                } else {
                    ivBg.setImageBitmap(ImageUtils.getTiledBitmap(EraseActivity.this, R.drawable.tbg2, width, height));
                }
                importImageFromUri();
            }
        }, 10);

        findViewById(R.id.ivChange).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeBG();
            }
        });

        rvTool = findViewById(R.id.rvOptions);
        rvTool.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvTool.setAdapter(new ToolAdapter(this, this));
        
    }


    Tools currentMode = Tools.NONE;

    @Override
    public void onToolSelected(Tools toolType) {
        this.currentMode = toolType;
        switch (toolType) {
            case ERASE:
                eraseView.enableTouchClear(true);
                rlMain.setOnTouchListener(null);
                eraseView.setMODE(1);
                eraseView.invalidate();
                sbBrush.setProgress(eraseView.getOffset() + 150);
                llEraser.setVisibility(View.VISIBLE);
                llAuto.setVisibility(View.GONE);
                llLasso.setVisibility(View.GONE);
                break;
            case MAGIC:
                eraseView.enableTouchClear(true);
                rlMain.setOnTouchListener(null);
                eraseView.setMODE(2);
                eraseView.invalidate();
                sbOffset.setProgress(eraseView.getOffset() + 150);
                llEraser.setVisibility(View.GONE);
                llAuto.setVisibility(View.VISIBLE);
                llLasso.setVisibility(View.GONE);
                break;
            case LASSO:
                eraseView.enableTouchClear(true);
                rlMain.setOnTouchListener(null);
                eraseView.setMODE(3);
                eraseView.invalidate();
                sbExtract.setProgress(eraseView.getOffset() + 150);
                llEraser.setVisibility(View.GONE);
                llAuto.setVisibility(View.GONE);
                llLasso.setVisibility(View.VISIBLE);
                break;
            case RESTORE:
                eraseView.enableTouchClear(true);
                rlMain.setOnTouchListener(null);
                eraseView.setMODE(4);
                eraseView.invalidate();
                sbBrush.setProgress(eraseView.getOffset() + 150);
                llEraser.setVisibility(View.VISIBLE);
                llAuto.setVisibility(View.GONE);
                llLasso.setVisibility(View.GONE);
                break;
            case ZOOM:
                eraseView.enableTouchClear(false);
                rlMain.setOnTouchListener(new MultiTouchListener());
                eraseView.setMODE(0);
                eraseView.invalidate();
                llEraser.setVisibility(View.GONE);
                llAuto.setVisibility(View.GONE);
                llLasso.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
       
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initUI() {
        this.rlSeekbar = findViewById(R.id.relativeLayoutSeekBar);
        this.ivChange = findViewById(R.id.ivChange);
        this.rlMain = findViewById(R.id.main_rel);
        this.llAuto = findViewById(R.id.linearLayoutAuto);
        this.llEraser = findViewById(R.id.linearLayoutEraser);
        this.llLasso = findViewById(R.id.lay_lasso_cut);
        this.ivInSide = findViewById(R.id.imageViewCutInSide);
        this.ivOutSide = findViewById(R.id.imageViewCutOutSide);
        this.ivUndo = findViewById(R.id.imageViewUndo);
        this.ivRedo = findViewById(R.id.imageViewRedo);
        this.ivBg = findViewById(R.id.imageViewBackgroundCover);
        this.ivUndo.setOnClickListener(this);
        this.ivRedo.setOnClickListener(this);
        this.ivUndo.setEnabled(false);
        this.ivRedo.setEnabled(false);
        this.ivInSide.setOnClickListener(this);
        this.ivOutSide.setOnClickListener(this);
        this.sbBrush =  findViewById(R.id.seekBarBrushOffset);
        this.sbOffset = findViewById(R.id.sbOffset);
        this.sbExtract = findViewById(R.id.seekBarExtractOffset);
        this.sbBrush.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (eraseView != null) {
                    eraseView.setOffset(i - 150);
                    eraseView.invalidate();
                }
            }
        });
        this.sbOffset.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (eraseView != null) {
                    eraseView.setOffset(i - 150);
                    eraseView.invalidate();
                }
            }
        });
        this.sbExtract.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (eraseView != null) {
                    eraseView.setOffset(i - 150);
                    eraseView.invalidate();
                }
            }
        });
        this.sbRadius = (SeekBar) findViewById(R.id.sbSize);
        this.sbRadius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (eraseView != null) {
                    eraseView.setRadius(i + 2);
                    eraseView.invalidate();
                }
            }
        });
        this.sbThreshold = findViewById(R.id.seekBarThreshold);
        this.sbThreshold.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (eraseView != null) {
                    eraseView.setThreshold(seekBar.getProgress() + 10);
                    eraseView.updateThreshHold();
                }
            }
        });

    }

    @SuppressLint({"WrongConstant"})
    public void onClick(View view) {
        if (this.eraseView != null ) {
            switch (view.getId()) {
                case R.id.imageViewRedo:
                    //laView.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        eraseView.redoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           // laView.setVisibility(View.GONE);
                        }
                    }).start();
                    return;
                case R.id.imageViewUndo:
                    //laView.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        eraseView.undoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //laView.setVisibility(View.GONE);
                        }
                    }).start();
                    return;
                case R.id.imageViewCutInSide:
                    this.eraseView.enableInsideCut(true);
                    this.ivInSide.clearAnimation();
                    this.ivOutSide.clearAnimation();
                    return;
                case R.id.imageViewCutOutSide:
                    this.eraseView.enableInsideCut(false);
                    this.ivInSide.clearAnimation();
                    this.ivOutSide.clearAnimation();
                    return;

            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.import_img_warning), Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveView() {
        bitmap = this.eraseView.getFinalBitmap();
        if (bitmap != null) {
            try {
                int dpToPx = ImageUtils.dpToPx(EraseActivity.this, 42.0f);
                bitmap = ImageUtils.resizeBitmap(bitmap, orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx);
                int i = dpToPx + dpToPx;
                bitmap = Bitmap.createBitmap(bitmap, dpToPx, dpToPx, bitmap.getWidth() - i, bitmap.getHeight() - i);
                bitmap = Bitmap.createScaledBitmap(bitmap, orgBitWidth, orgBitHeight, true);
                bitmap = ImageUtils.bitmapmasking(orgBitmap, bitmap);
                if (openFrom.equalsIgnoreCase(Constants.OPEN_DRIP)) {
                    DripActivity.noBitmap = bitmap;
                } else if (openFrom.equalsIgnoreCase(Constants.OPEN_PATTERN)) {
                    PatternActivity.noBitmap = bitmap;
                }
                setResult(RESULT_OK);
                finish();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
        adsManager.showInterstitialAd();
    }

    private void changeBG() {
        int i = curBgType;
        if (i == 1) {
            curBgType = 2;
            this.ivBg.setImageBitmap(null);
            ivBg.setImageBitmap(ImageUtils.getTiledBitmap(EraseActivity.this, R.drawable.tbg1, width, height));
            ivChange.setImageResource(R.drawable.tbg1);
        } else if (i == 2) {
            curBgType = 3;
            this.ivBg.setImageBitmap(null);
            ivBg.setImageBitmap(ImageUtils.getTiledBitmap(EraseActivity.this, R.drawable.tbg2, width, height));
            ivChange.setImageResource(R.drawable.tbg2);
        }
    }

    public void importImageFromUri() {
        this.showDialog = false;
        final ProgressDialog show = ProgressDialog.show(this, "", getResources().getString(R.string.importing_image), true);
        show.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (b == null) {
                        showDialog = true;
                    } else {
                        orgBitmap = b.copy(b.getConfig(), true);
                        int dpToPx = ImageUtils.dpToPx(EraseActivity.this, 42.0f);
                        EraseActivity.orgBitWidth = b.getWidth();
                        EraseActivity.orgBitHeight = b.getHeight();
                        Bitmap createBitmap = Bitmap.createBitmap(b.getWidth() + dpToPx + dpToPx, b.getHeight() + dpToPx + dpToPx, b.getConfig());
                        Canvas canvas = new Canvas(createBitmap);
                        canvas.drawColor(0);
                        float f = (float) dpToPx;
                        canvas.drawBitmap(b, f, f, null);
                        b = createBitmap;
                        if (b.getWidth() >width || b.getHeight() >height || (b.getWidth() < width && b.getHeight() < height)) {
                            b = ImageUtils.resizeBitmap(b, width, height);
                        }
                    }
                    Thread.sleep(1000);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    showDialog = true;
                    show.dismiss();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    showDialog = true;
                    show.dismiss();
                }
                show.dismiss();
            }
        }).start();
        show.setOnDismissListener(new OnDismissListener() {
            @SuppressLint({"WrongConstant"})
            public void onDismiss(DialogInterface dialogInterface) {
                if (showDialog) {
                    EraseActivity stickerRemoveActivity = EraseActivity.this;
                    Toast.makeText(stickerRemoveActivity, stickerRemoveActivity.getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                String str = "";
                Constants.rewid = str;
                Constants.uri = str;
                Constants.bitmapSticker = null;
                setImageBitmap();
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    public void setImageBitmap() {
        this.eraseView = new EraseView(this);
        this.ivPV = new ImageView(this);
        this.eraseView.setImageBitmap(this.b);
        this.ivPV.setImageBitmap(getGreenLayerBitmap(this.b));
        //this.eraseView.enableTouchClear(false);
        //this.eraseView.setMODE(0);
        this.eraseView.invalidate();
        this.eraseView.enableTouchClear(true);
        this.rlMain.setOnTouchListener(null);
        this.eraseView.setMODE(1);
        this.eraseView.invalidate();
        this.sbBrush.setProgress(this.eraseView.getOffset() + 150);

       // this.seekBarBrushOffset.setProgress(225);
        this.sbRadius.setProgress(18);
        this.sbThreshold.setProgress(20);
        this.rlMain.removeAllViews();
        this.rlMain.setScaleX(1.0f);
        this.rlMain.setScaleY(1.0f);
        this.rlMain.addView(this.ivPV);
        this.rlMain.addView(this.eraseView);
        this.eraseView.invalidate();
        this.ivPV.setVisibility(View.GONE);
        this.eraseView.setUndoRedoListener(new EraseView.UndoRedoListener() {
            public void enableUndo(boolean z, int i) {
                setBGDrawable( i, ivUndo, R.drawable.ic_undo, z);
            }

            public void enableRedo(boolean z, int i) {
                setBGDrawable( i, ivRedo, R.drawable.ic_redo, z);
            }
        });
        this.b.recycle();
        this.eraseView.setActionListener(new EraseView.ActionListener() {
            public void onActionCompleted(final int i) {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant"})
                    public void run() {
                        if (i == 5) {
                            //offset_seekbar_lay.setVisibility(View.GONE);
                        }
                    }
                });
            }

            public void onAction(final int i) {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant"})
                    public void run() {
                    }
                });
            }
        });
    }

    public void setBGDrawable( int i, ImageView imageView, int i2, boolean z) {
        final ImageView imageView2 = imageView;
        final int i3 = i2;
        final boolean z2 = z;
        final int i4 = i;
        runOnUiThread(new Runnable() {
            public void run() {
                imageView2.setImageResource(i3);
                imageView2.setEnabled(z2);
            }
        });
    }

    public Bitmap getGreenLayerBitmap(Bitmap bitmap2) {
        Paint paint = new Paint();
        paint.setColor(-16711936);
        paint.setAlpha(80);
        int dpToPx = ImageUtils.dpToPx(this, 42.0f);
        Bitmap createBitmap = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        float f = (float) dpToPx;
        canvas.drawBitmap(this.orgBitmap, f, f, null);
        canvas.drawRect(f, f, (float) (orgBitWidth + dpToPx), (float) (orgBitHeight + dpToPx), paint);
        Bitmap createBitmap2 = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawColor(0);
        canvas2.drawBitmap(this.orgBitmap, f, f, null);
        patternBMPshader = new BitmapShader(ImageUtils.resizeBitmap(createBitmap2, this.width, this.height), TileMode.REPEAT, TileMode.REPEAT);
        return ImageUtils.resizeBitmap(createBitmap, this.width, this.height);
    }

    public void onDestroy() {
        Bitmap bitmap2 = this.b;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.b = null;
        }
        try {
            if (!isFinishing() && this.eraseView.pd != null && this.eraseView.pd.isShowing()) {
                this.eraseView.pd.dismiss();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }



}
