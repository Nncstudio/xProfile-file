package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model;

import android.graphics.drawable.Drawable;

public class AdjustModel {
    private Drawable icon;
    private String text;

    public AdjustModel(Drawable i, String t) {
        icon = i; text = t;
    }

    public Drawable getIcon() {return icon;}

    public void setIcon(Drawable icon) {this.icon = icon;}

    public String getText() {return text;}

    public void setText(String text) {this.text = text;}
}
