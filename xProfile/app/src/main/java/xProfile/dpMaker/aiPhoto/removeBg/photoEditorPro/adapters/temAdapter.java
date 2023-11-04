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
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CornerLayout;

public class temAdapter extends RecyclerView.Adapter<temAdapter.ViewHolder> {
    Context ctx;
    Datum iDetails;
    CallBack selFrames;
    boolean iPo;
    public int iSelected = 0;
    public interface CallBack {
        void selFrame(int i);
    }

    public temAdapter(Context context2, boolean y, Datum datum, CallBack callBack) {
        this.ctx = context2;
        this.iPo = y;
        this.iDetails = datum;
        this.selFrames = callBack;
    }



    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bg, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String thumb = this.iDetails.getProductDetails().get(position).getThumb();
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
        if (iPo){
            Glide.with(this.ctx).load(xProfile.BACKEND_RESOURCES_URL + ITEM + image).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.mipmap.ic_launcher)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into(holder.ivItem);
            holder.ivItem.setColorFilter(0);
        } else {
            Glide.with(this.ctx).load(xProfile.BACKEND_RESOURCES_URL + ITEM + thumb+FORMAT).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.mipmap.ic_launcher)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into(holder.ivItem);
            holder.rfItem.iBackgroundColor(this.ctx.getResources().getColor(R.color.black));
            holder.ivItem.setColorFilter(this.ctx.getResources().getColor(R.color.itemColor));
        }

    }

    public int getItemCount() {
        return this.iDetails.getProductDetails().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivItem;
        CornerLayout rfItem;
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            tvTitle =  view.findViewById(R.id.tvTitle);
            ivItem =  view.findViewById(R.id.ivItem);
            rfItem =  view.findViewById(R.id.rfItem);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            temAdapter.this.iSelected = getAdapterPosition();
            temAdapter.this.selFrames.selFrame(temAdapter.this.iSelected);
            temAdapter.this.notifyDataSetChanged();
        }
    }
}
