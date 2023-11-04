package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;


public class NoNetDialog {
    Dialog dialog;
    TextView notNow;

    public NoNetDialog(final Activity activity) {
        try {
            dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_internet);
            notNow =  dialog.findViewById(R.id.tvRetry);
            if (dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.setCancelable(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    activity.finish();
                }
            });
            notNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } catch (Exception unused) {
        }
    }

    public void showDialog() {
        try {
            dialog.show();
        } catch (Exception unused) {
        }
    }

    public void hideDialog() {
        try {
            dialog.dismiss();
        } catch (Exception unused) {
        }
    }
}
