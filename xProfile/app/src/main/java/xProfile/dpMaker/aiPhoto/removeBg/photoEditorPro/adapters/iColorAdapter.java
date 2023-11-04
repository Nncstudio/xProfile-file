package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.ColorCode;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CircleView;

public class iColorAdapter extends RecyclerView.Adapter<iColorAdapter.ViewHolder>{
    public iColorListener iColorListener;
    private Context ctx;
    public int iSelected;
    public List<iColor> iColorList = new ArrayList();

    public interface iColorListener {
        void onColorSelected(int i, iColor squareView);
    }

    public iColorAdapter(Context context, iColorListener frameListener) {
        ctx = context;
        iColorListener = frameListener;
        List<String>listColorBrush = ColorCode.colorList();
        for (int i = 0; i < listColorBrush.size() - 2; i++) {
            iColorList.add(new iColor(Color.parseColor(listColorBrush.get(i)), true));
        }

    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        iColor squareView = this.iColorList.get(i);
        if (this.iSelected == i) {
            viewHolder.tvTitle.setTextColor(ctx.getResources().getColor(R.color.black));
            viewHolder.iColor.setStrokeColor(ctx.getResources().getColor(R.color.black));
        } else {
            viewHolder.tvTitle.setTextColor(ctx.getResources().getColor(R.color.iconColor));
            viewHolder.iColor.setStrokeColor(ctx.getResources().getColor(R.color.itemColor));
        }
        viewHolder.tvTitle.setText("CL"+i++);
        viewHolder.iColor.setFillColor(squareView.drawableId);
        viewHolder.iColor.setBackgroundColor(squareView.drawableId);
    }

    public int getItemCount() {
        return this.iColorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleView iColor;
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            iColor = view.findViewById(R.id.iColor);
            tvTitle = view.findViewById(R.id.tvTitle);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            iSelected = getAdapterPosition();
            iColorListener.onColorSelected(iSelected,iColorList.get(iSelected));
            notifyDataSetChanged();
        }
    }

    public class iColor {
        public int drawableId;
        public boolean isColor;

        iColor(int i) {
            this.drawableId = i;
        }

        iColor(int i, boolean z) {
            this.drawableId = i;
            this.isColor = z;
        }
    }
}
