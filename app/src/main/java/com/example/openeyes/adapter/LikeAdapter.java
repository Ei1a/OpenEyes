package com.example.openeyes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openeyes.R;
import com.example.openeyes.bean.VideoItem;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    private Context mContext;
    private List<VideoItem> videoItemList;

    public LikeAdapter(List<VideoItem> list){
        this.videoItemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item_like, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoItem item = videoItemList.get(position);
        Glide.with(mContext).load(item.getImageUrl()).into(holder.videoImage);
        holder.videoTitle.setText(item.getTitle());
        holder.videoTag.setText("#" + item.getTag());
        holder.videoLikeCounts.setText(item.getLikeCounts() + "");
        holder.videoShareCounts.setText(item.getShareCounts() + "");
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout root;
        ImageView videoImage;
        TextView videoTitle;
        TextView videoTag;
        TextView videoLikeCounts;
        TextView videoShareCounts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_like_item);
            videoImage = (ImageView) itemView.findViewById(R.id.video_image_like);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title_like);
            videoTag = (TextView) itemView.findViewById(R.id.video_tag_like);
            videoLikeCounts = (TextView) itemView.findViewById(R.id.text_like_counts);
            videoShareCounts = (TextView) itemView.findViewById(R.id.text_share_counts);
        }
    }
}
