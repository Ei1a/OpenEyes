package com.example.openeyes.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.view.PlayerActivity;
import com.example.openeyes.R;
import com.example.openeyes.view.RecordActivity;

import java.util.List;

public class VideoAdapterRecord extends RecyclerView.Adapter<VideoAdapterRecord.ViewHolder> {
    private Context mContext;
    private List<VideoItem> videoItemList;

    public VideoAdapterRecord(List<VideoItem> videoItemList){
        this.videoItemList = videoItemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item_record,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                VideoItem videoItem = videoItemList.get(position);
                Intent intent = new Intent(mContext, PlayerActivity.class);
                PlayerActivity.videoItem = videoItem;
                PlayerActivity.isNormalOrDB = false;
                mContext.startActivity(intent);
                 /*
                    转换后才能使用overridePendingTransition方法
                 */
                RecordActivity activity = (RecordActivity) mContext;
                activity.overridePendingTransition(R.anim.welcome_to_main,R.anim.fix_close);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoItem videoItem = videoItemList.get(position);
        Glide.with(mContext).load(videoItem.getImageUrl()).into(holder.videoImage);
        holder.videoTitle.setText(videoItem.getTitle());
        Log.d("DBTest", videoItem.getTitle());
        holder.videoTag.setText(videoItem.getTag());
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView videoImage;
        TextView videoTitle;
        TextView videoTag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.item_layout_record);
            videoImage = (ImageView) itemView.findViewById(R.id.video_image_record);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title_record);
            videoTag = (TextView) itemView.findViewById(R.id.video_tag_record);
        }
    }
}
