package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

public class SaveFileUtils {
    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static File saveBitmap(Context ctx, Bitmap btm, String name, String dir) throws IOException {

        if (Build.VERSION.SDK_INT >= 29) {
            boolean isWritable=isExternalStorageWritable();
            String imagesRelPath=null;
            if(isWritable) {
                FileOutputStream fos = null;
                try {
                    String relativePath = dir != null ? Environment.DIRECTORY_PICTURES + File.separator + dir : Environment.DIRECTORY_PICTURES + File.separator + ctx.getResources().getString(R.string
                            .app_name);
                    ContentResolver resolver = ctx.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

                    Uri contUri= resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imagesRelPath= FilePathUtil.getPath(ctx,contUri);
                    fos = (FileOutputStream) resolver.openOutputStream(contUri);
                    btm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                } catch (Exception e) {
                    String err = e.getMessage();
                    Log.e("File save exception", err);
                } finally {
                    if (fos != null) fos.close();
                }
                return imagesRelPath!=null?new File(imagesRelPath):null;
            }
            return null;

        }
        String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + ctx.getResources().getString(R.string
                .app_name);
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }

        File image = new File(imagesDir2, name + ".png");
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(ctx, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        btm.compress(Bitmap.CompressFormat.PNG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

}
