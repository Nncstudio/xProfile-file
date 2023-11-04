package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile.FORMAT;
import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.ITEM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.Datum;
import io.github.florent37.shapeofview.shapes.CircleView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    Context ctx;
    Datum iDetails;
    CallBack selFrames;
    boolean iBg;
    public int iSelected = 0;
    public interface CallBack {
        void selFrame(int i);
    }

    public ListAdapter(Datum datum, Context context2, boolean z, CallBack cb) {
        iDetails = datum;
        ctx = context2;
        iBg = z;
        selFrames = cb;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String image = this.iDetails.getProductDetails().get(position).getImage();
        String title = this.iDetails.getProductDetails().get(position).getTitle();
        if (this.iSelected == position) {
            holder.tvTitle.setTextColor(this.ctx.getResources().getColor(R.color.black));
            holder.rfItem.setBorderColor(this.ctx.getResources().getColor(R.color.black));
        } else {
            holder.tvTitle.setTextColor(this.ctx.getResources().getColor(R.color.iconColor));
            holder.rfItem.setBorderColor(this.ctx.getResources().getColor(R.color.itemColor));
        }
        holder.tvTitle.setText(title);
        if (iBg){
            Glide.with(this.ctx).load(xProfile.BACKEND_RESOURCES_URL+ITEM+image+FORMAT).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.mipmap.ic_launcher)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into(holder.ivItem);
            holder.ivItem.setColorFilter(0);
        } else {
            Glide.with(this.ctx).load(xProfile.BACKEND_RESOURCES_URL+ITEM+image+FORMAT).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.mipmap.ic_launcher)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into(holder.ivItem);
            holder.ivItem.setColorFilter(ctx.getResources().getColor(R.color.black));
        }
    }


    public int getItemCount() {
        return this.iDetails.getProductDetails().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivItem;
        CircleView rfItem;
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            tvTitle =  view.findViewById(R.id.tvTitle);
            ivItem =  view.findViewById(R.id.ivPic);
            rfItem =  view.findViewById(R.id.rfItem);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            iSelected = getAdapterPosition();
            selFrames.selFrame(iSelected);
            notifyDataSetChanged();
        }
    }
}
