package com.example.openeyes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.view.MainActivity;
import com.example.openeyes.view.PlayerActivity;
import com.example.openeyes.R;
import com.example.openeyes.view.SearchActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context mContext;
    private List<VideoItem> videoItemList;

    public VideoAdapter(List<VideoItem> videoItemList){
        this.videoItemList = videoItemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                VideoItem videoItem = videoItemList.get(position);
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("item", videoItem);
                intent.putExtra("isNormal", true);
                mContext.startActivity(intent);
                /*
                    转换后才能使用overridePendingTransition方法
                 */
                if(MainActivity.isMainOrSearch){
                    MainActivity activity = (MainActivity) mContext;
                    activity.overridePendingTransition(R.anim.welcome_to_main,R.anim.fix_close);
                } else {
                    SearchActivity activity = (SearchActivity) mContext;
                    activity.overridePendingTransition(R.anim.welcome_to_main,R.anim.fix_close);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoItem videoItem = videoItemList.get(position);
        holder.videoTitle.setText(videoItem.getTitle());
        holder.videoAuthorName.setText(videoItem.getAuthorName());
        holder.videoTag.setText(videoItem.getTag());
        Glide.with(mContext).load(videoItem.getImageUrl()).into(holder.videoImage);
        Glide.with(mContext).load(videoItem.getHeadIconUrl()).into(holder.headIcon);
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView videoImage;
        CircleImageView headIcon;
        TextView videoTitle;
        TextView videoAuthorName;
        TextView videoTag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            headIcon = (CircleImageView) itemView.findViewById(R.id.head_icon);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            videoAuthorName = (TextView) itemView.findViewById(R.id.video_author_name);
            videoTag = (TextView) itemView.findViewById(R.id.video_tag);
        }
    }

}
