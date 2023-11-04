package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.Log;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;


public class ImageUtils {

    public static int dpToPx(Context context, float i) {
        float f = (float) i;
        context.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        float f;
        float f2;
        if (bitmap == null) {
            return null;
        }
        float f3 = (float) i;
        float f4 = (float) i2;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append(f3);
        String str = "  ";
        sb.append(str);
        sb.append(f4);
        sb.append("  and  ");
        sb.append(width);
        sb.append(str);
        sb.append(height);
        String str2 = "testings";
        Log.i(str2, sb.toString());
        float f5 = width / height;
        float f6 = height / width;
        String str3 = "  if (he >hr) ";
        String str4 = " in else ";
        if (width >f3) {
            f = f3 * f6;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("if (wd >wr) ");
            sb2.append(f3);
            sb2.append(str);
            sb2.append(f);
            Log.i(str2, sb2.toString());
            if (f >f4) {
                f3 = f4 * f5;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(f3);
                sb3.append(str);
                sb3.append(f4);
                Log.i(str2, sb3.toString());
                return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str4);
            sb4.append(f3);
            sb4.append(str);
            sb4.append(f);
            Log.i(str2, sb4.toString());
        } else {
            if (height >f4) {
                f2 = f4 * f5;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str3);
                sb5.append(f2);
                sb5.append(str);
                sb5.append(f4);
                Log.i(str2, sb5.toString());
                if (f2 <= f3) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str4);
                    sb6.append(f2);
                    sb6.append(str);
                    sb6.append(f4);
                    Log.i(str2, sb6.toString());
                    f3 = f2;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else if (f5 >0.75f) {
                f = f3 * f6;
                Log.i(str2, " if (rat1 >.75f) ");
                if (f >f4) {
                    f3 = f4 * f5;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str3);
                    sb7.append(f3);
                    sb7.append(str);
                    sb7.append(f4);
                    Log.i(str2, sb7.toString());
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str4);
                sb8.append(f3);
                sb8.append(str);
                sb8.append(f);
                Log.i(str2, sb8.toString());
            } else if (f6 >1.5f) {
                f2 = f4 * f5;
                Log.i(str2, " if (rat2 >1.5f) ");
                if (f2 <= f3) {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str4);
                    sb9.append(f2);
                    sb9.append(str);
                    sb9.append(f4);
                    Log.i(str2, sb9.toString());
                    f3 = f2;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else {
                f = f3 * f6;
                Log.i(str2, str4);
                if (f >f4) {
                    f3 = f4 * f5;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str3);
                    sb10.append(f3);
                    sb10.append(str);
                    sb10.append(f4);
                    Log.i(str2, sb10.toString());
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
                StringBuilder sb11 = new StringBuilder();
                sb11.append(str4);
                sb11.append(f3);
                sb11.append(str);
                sb11.append(f);
                Log.i(str2, sb11.toString());
            }
            f4 = f3 * f6;
            return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
        }
        f4 = f;
        return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
    }


    public static Bitmap getTiledBitmap(Context context, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), i, new Options()), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public static Bitmap bitmapmasking(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }
    public static Options getResampling(int i, int i2, int i3) {
        float f;
        float f2;
        Options options = new Options();
        if (i <= i2 && i2 > i) {
            f = (float) i3;
            f2 = (float) i2;
        } else {
            f = (float) i3;
            f2 = (float) i;
        }
        float f3 = f / f2;
        options.outWidth = (int) ((((float) i) * f3) + 0.5f);
        options.outHeight = (int) ((((float) i2) * f3) + 0.5f);
        return options;
    }

    public static int getClosestResampleSize(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        while (true) {
            if (i4 >= Integer.MAX_VALUE) {
                break;
            } else if (i4 * i3 > max) {
                i4--;
                break;
            } else {
                i4++;
            }
        }
        if (i4 > 0) {
            return i4;
        }
        return 1;
    }

    public static boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }
}
