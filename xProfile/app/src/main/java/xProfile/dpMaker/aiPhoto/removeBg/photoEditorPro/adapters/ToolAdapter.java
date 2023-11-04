package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import android.content.Context;
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
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Tools;

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ViewHolder>{
    Context ctx;
    public OnItemSelected onItemSelected;
    public List<Tool> toolList = new ArrayList<>();
    boolean b;
    public int iSelected = 0;
    public interface OnItemSelected {
        void onToolSelected(Tools toolType);
    }

    public ToolAdapter(Context ctx,  OnItemSelected onItemSelected) {
        this.ctx = ctx;
        this.onItemSelected = onItemSelected;
        this.toolList.add(new Tool(R.string.erase, R.drawable.ic_eraser, Tools.ERASE));
        this.toolList.add(new Tool(R.string.auto, R.drawable.ic_auto, Tools.MAGIC));
        this.toolList.add(new Tool(R.string.lasso, R.drawable.ic_lasso, Tools.LASSO));
        this.toolList.add(new Tool(R.string.reset, R.drawable.ic_left, Tools.RESTORE));
        this.toolList.add(new Tool(R.string.zoom, R.drawable.ic_round_zoom_in, Tools.ZOOM));
    }

    public ToolAdapter(Context ctx, boolean z, OnItemSelected onItemSelected) {
        this.ctx = ctx;
        b = z;
        this.onItemSelected = onItemSelected;
        this.toolList.add(new Tool(R.string.pattern, R.drawable.ic_deg, Tools.PATTERN));
        this.toolList.add(new Tool(R.string.color, R.drawable.ic_palette, Tools.COLOR));
        this.toolList.add(new Tool(R.string.gradient, R.drawable.ic_bg, Tools.DEGRADE));
        this.toolList.add(new Tool(R.string.adjust, R.drawable.ic_adjust, Tools.ADJUST));
        this.toolList.add(new Tool(R.string.stickers, R.drawable.ic_sticker, Tools.STICKER));
    }

    public ToolAdapter(boolean z, Context ctx, OnItemSelected onItemSelected) {
        this.ctx = ctx;
        b = z;
        this.onItemSelected = onItemSelected;
        this.toolList.add(new Tool(R.string.drip, R.drawable.ic_art, Tools.DRIP));
        this.toolList.add(new Tool(R.string.color, R.drawable.ic_palette, Tools.COLOR));
        this.toolList.add(new Tool(R.string.stickers, R.drawable.ic_sticker, Tools.STICKER));
    }

    public ToolAdapter(Context ctx, OnItemSelected onItemSelected, boolean z) {
        this.ctx = ctx;
        b = z;
        this.onItemSelected = onItemSelected;
        this.toolList.add(new Tool(R.string.frame, R.drawable.ic_profile, Tools.PROFILE));
        this.toolList.add(new Tool(R.string.adjust, R.drawable.ic_adjust, Tools.ADJUST));
        this.toolList.add(new Tool(R.string.stickers, R.drawable.ic_sticker, Tools.STICKER));
    }

    class Tool {
        public int toolIcon;
        public int toolName;
        public Tools toolType;

        Tool(int str, int i, Tools toolType) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tool, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        viewHolder.tvIcon.setText(toolList.get(pos).toolName);
        viewHolder.ivIcon.setImageResource(toolList.get(pos).toolIcon);
        if (iSelected == pos){
            viewHolder.tvIcon.setTextColor(ctx.getResources().getColor(R.color.mainColor));
            viewHolder.ivIcon.setColorFilter(ctx.getResources().getColor(R.color.mainColor));
        } else {
            viewHolder.tvIcon.setTextColor(ctx.getResources().getColor(R.color.black));
            viewHolder.ivIcon.setColorFilter(ctx.getResources().getColor(R.color.black));

        }

    }

    public int getItemCount() {
        return this.toolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvIcon;


        ViewHolder(View view) {
            super(view);
            this.ivIcon = view.findViewById(R.id.ivIcon);
            this.tvIcon = view.findViewById(R.id.tvIcon);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    iSelected = getLayoutPosition();
                    onItemSelected.onToolSelected((toolList.get(iSelected)).toolType);
                    notifyDataSetChanged();
                }
            });

        }
    }
}
