package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import static xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils.Constants.ITEM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.model.Datum;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CornerLayout;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    Context ctx;
    Datum iDetails;
    CallBack selFrames;
    public int iSelected = 0;
    public interface CallBack {
        void selFrame(int i);
    }

    public StickerAdapter(Context context2, Datum datum, CallBack callBack) {
        this.ctx = context2;
        this.iDetails = datum;
        this.selFrames = callBack;
    }



    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stickers, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String image = this.iDetails.getProductDetails().get(position).getImage();
        if (this.iSelected == position) {
            holder.rfItem.setBorderColor(this.ctx.getResources().getColor(R.color.mainColor));
        } else {
            holder.rfItem.setBorderColor(this.ctx.getResources().getColor(R.color.lineColor));
        }
        Glide.with(this.ctx).load(xProfile.BACKEND_RESOURCES_URL + ITEM + image+".png").apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().centerCrop()).error((int) R.mipmap.ic_launcher)).thumbnail(Glide.with(this.ctx).load(Integer.valueOf(R.drawable.iv_loading))).into(holder.ivItem);

    }

    public int getItemCount() {
        return this.iDetails.getProductDetails().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivItem;
        CornerLayout rfItem;

        ViewHolder(View view) {
            super(view);
            ivItem =  view.findViewById(R.id.ivItem);
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
