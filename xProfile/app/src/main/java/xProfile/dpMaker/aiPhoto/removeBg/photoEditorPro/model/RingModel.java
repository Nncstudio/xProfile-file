package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RingModel {
    @SerializedName("data")
    @Expose
    private List<Datum>data = null;

    public List<Datum>getData() {
        return this.data;
    }

    public void setData(List<Datum>list) {
        this.data = list;
    }
}
