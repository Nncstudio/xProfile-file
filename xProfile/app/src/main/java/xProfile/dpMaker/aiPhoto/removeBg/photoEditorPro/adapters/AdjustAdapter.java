package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.AdjustModel;


public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    private final ArrayList<AdjustModel> adjust;
    int iRes;

    public AdjustAdapter(ArrayList<AdjustModel> list, int resource) {
        adjust = list; iRes = resource;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater li = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) li.inflate(iRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdjustModel item = adjust.get(position);
        ImageView iconView = holder.iconView;
        TextView textView = holder.textView;

        iconView.setImageDrawable(item.getIcon());
        textView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return adjust.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconView;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.ivIcon);
            textView = itemView.findViewById(R.id.tvName);
        }
    }
}
