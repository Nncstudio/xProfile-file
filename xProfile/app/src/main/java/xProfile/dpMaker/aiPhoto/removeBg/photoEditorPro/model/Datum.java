package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("list_name")
    @Expose
    private String list_name;
    @SerializedName("details")
    @Expose
    private List<Details> details = null;
    @SerializedName("type")
    @Expose
    private Integer type;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer num) {
        this.type = num;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getList_name() {
        return this.list_name;
    }

    public void setList_name(String str) {
        this.list_name = str;
    }


    public List<Details>getProductDetails() {
        return this.details;
    }

    public void setProductDetails(List<Details>list) {
        this.details = list;
    }
}
