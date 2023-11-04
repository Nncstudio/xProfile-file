package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters.WorksAdapter;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.FileClickInterface;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants;


public class WorkActivity extends BaseActivity {
    RecyclerView mRecyclerMyPhotos;
    ArrayList<File> currFileList;
    WorksAdapter myPhotosAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
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

        findViewById(R.id.noResult).setVisibility(View.GONE);
        mRecyclerMyPhotos = (RecyclerView) findViewById(R.id.rvWorks);
        mRecyclerMyPhotos.setLayoutManager(new GridLayoutManager(this, 4));
        currFileList = listFiles();
        setAdapter();
    }


    private void refreshView() {
        if (currFileList.size() > 0) {
            mRecyclerMyPhotos.setVisibility(View.VISIBLE);
            findViewById(R.id.noResult).setVisibility(View.GONE);
        } else {
            mRecyclerMyPhotos.setVisibility(View.GONE);
            findViewById(R.id.noResult).setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        myPhotosAdapter = new WorksAdapter(WorkActivity.this, currFileList, new FileClickInterface() {
            @Override
            public void onPhotoClick(final String filePath) {

                Intent intent = new Intent(WorkActivity.this, DisplayActivity.class);
                intent.putExtra(Constants.KEY_URI_IMAGE, filePath.toString());
                startActivityForResult(intent, 1);
            }
        });
        mRecyclerMyPhotos.setAdapter(myPhotosAdapter);
        refreshView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                currFileList = listFiles();
                setAdapter();
            }
        }
    }


    @Override
    protected void onResume() {
        ArrayList<File> fileList = listFiles();
        if (currFileList.size() != fileList.size()) {
            currFileList = fileList;
            resetAdapter();
        }
        super.onResume();
    }

    private void resetAdapter() {
        myPhotosAdapter.notifyDataSetChanged();
        refreshView();
    }

    public ArrayList<File> listFiles() {
        ArrayList<File> fileList = new ArrayList<>();
        String img = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string
                .app_name);
        File myDir = new File(img);
        File[] files = myDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg") || Uri.fromFile(file).toString().endsWith(".jpeg")) {
                    fileList.add(file);
                }
            }
        }
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                long k = file2.lastModified() - file1.lastModified();
                if (k > 0) {
                    return 1;
                } else if (k == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));
        return fileList;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
