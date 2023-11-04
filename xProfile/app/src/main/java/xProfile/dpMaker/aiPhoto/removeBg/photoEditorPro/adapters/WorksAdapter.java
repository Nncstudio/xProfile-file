package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.listener.FileClickInterface;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views.CornerImage;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<File> myPhotosFileList;
    private LayoutInflater mInflater;
    private FileClickInterface photoListener;

    public WorksAdapter(Context mContext, ArrayList<File> myPhotosFileList, FileClickInterface photoViewClickListener) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.photoListener = photoViewClickListener;
        this.myPhotosFileList = myPhotosFileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_works, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext).load(Uri.fromFile(myPhotosFileList.get(i)))
                .into(viewHolder.imageViewCover);

    }

    @Override
    public int getItemCount() {
        return myPhotosFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CornerImage imageViewCover;

        public ViewHolder(View view) {
            super(view);
            this.imageViewCover = view.findViewById(R.id.iView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (photoListener != null)
                photoListener.onPhotoClick(myPhotosFileList.get(getAdapterPosition()).getAbsolutePath());
        }
    }

}