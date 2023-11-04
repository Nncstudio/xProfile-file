package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.ColorCode;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CircleView;

public class DoubleAdapter extends RecyclerView.Adapter<DoubleAdapter.ViewHolder>{
    public ColorListener colorListener;
    Context context;
    boolean isColored;
    public int iSelected = 1;
    public List<iModel> iModels = new ArrayList();
    public List<iModel2> iModel2s = new ArrayList();

    public interface ColorListener {
        void onColorSelected(iModel iModel, iModel2 iModel2, int i);
    }

    public DoubleAdapter(Context ctx, ColorListener frameListener, boolean z) {
        context = ctx;
        this.colorListener = frameListener;
        isColored = z;
        List<String>listColor1 = ColorCode.colorList2();
        List<String>listColor2 = ColorCode.colorList1();
        for (int i = 0; i < listColor1.size() - 2; i++) {
            this.iModels.add(new iModel(Color.parseColor(listColor1.get(i)),  true));
        }
        for (int i = 0; i < listColor2.size() - 2; i++) {
            this.iModel2s.add(new iModel2(Color.parseColor(listColor2.get(i)),  true));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_double, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i == 0){
            viewHolder.ivRefresh.setVisibility(View.VISIBLE);
            viewHolder.ilColor.setVisibility(View.GONE);
        } else {
            iModel iModel = this.iModels.get(i);
            iModel2 iModel2 = this.iModel2s.get(i);
            viewHolder.ivRefresh.setVisibility(View.GONE);
            viewHolder.ilColor.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText("PT"+i++);
            if (isColored){
                viewHolder.iColor.setStrokeColor(iModel.drawableId);
                viewHolder.iColor.setFillColor(iModel2.drawableId);
                viewHolder.iColor.setBackgroundColor(iModel2.drawableId);
            } else{
                viewHolder.iColor.setStrokeColor(iModel2.drawableId);
                viewHolder.iColor.setFillColor(iModel.drawableId);
                viewHolder.iColor.setBackgroundColor(iModel.drawableId);
            }
        }

    }

    public int getItemCount() {
        return this.iModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivRefresh;
        CircleView iColor;
        LinearLayout ilColor;
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            iColor = view.findViewById(R.id.iColor);
            ilColor = view.findViewById(R.id.ilColor);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivRefresh = view.findViewById(R.id.ivRefresh);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = iSelected;
            iSelected = getAdapterPosition();
            notifyItemChanged(p);
            notifyItemChanged(iSelected);
            colorListener.onColorSelected((iModel) iModels.get(iSelected), (iModel2) iModel2s.get(iSelected), getAdapterPosition());
        }
    }

    public void setSelectedColorIndex(int i) {
        this.iSelected = i;
    }

    public class iModel {
        public int drawableId;
        public boolean isColor;

        iModel(int i) {
            this.drawableId = i;
        }

        iModel(int i, boolean z) {
            this.drawableId = i;
            this.isColor = z;
        }
    }

    public class iModel2 {
        public int drawableId;
        public boolean isColor;

        iModel2(int i) {
            this.drawableId = i;
        }

        iModel2(int i, boolean z) {
            this.drawableId = i;
            this.isColor = z;
        }
    }
}
