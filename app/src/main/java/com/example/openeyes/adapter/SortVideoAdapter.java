package com.example.openeyes.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openeyes.bean.VideoItem;
import com.example.openeyes.view.PlayerActivity;
import com.example.openeyes.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SortVideoAdapter extends RecyclerView.Adapter<SortVideoAdapter.ViewHolder> {
    private final String TAG = "mDebug";
    public final static String TAG_RECYCLERVIEW = "Sort";
    private Context mContext;
    private List<VideoItem> itemList;

    public SortVideoAdapter(List<VideoItem> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.video_item_sort,parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        VideoItem videoItem = itemList.get(position);
        Glide.with(mContext).load(videoItem.getHeadIconUrl()).into(holder.headIcon);
        holder.videoAuthorName.setText(videoItem.getAuthorName());
        holder.videoTitle.setText(videoItem.getTitle());
        holder.videoDescription.setText(videoItem.getVideoDescription());
        //跳转PlayerActivity
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                PlayerActivity.videoItem = videoItem;
                PlayerActivity.isNormalOrDB = true;
                mContext.startActivity(intent);
            }
        });

        /*
        配置GSYVideoPlayer
         */
        holder.videoPlayer.setUpLazy(videoItem.getPlayUrl(),true,null,null,null);
        //设置封面
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(videoItem.getImageUrl()).into(imageView);
        holder.videoPlayer.setThumbImageView(imageView);
        holder.videoPlayer.setThumbPlay(true);
        holder.videoPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
        //添加title
        holder.videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        holder.videoPlayer.getBackButton().setVisibility(View.GONE);
        //隐藏全屏按键
        holder.videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //设置全屏按键
//        holder.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.videoPlayer.startWindowFullscreen(mContext,false,true);
//            }
//        });
        //防止错位设置
        holder.videoPlayer.setPlayTag(TAG_RECYCLERVIEW);
        holder.videoPlayer.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或横屏全屏
        holder.videoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        holder.videoPlayer.setReleaseWhenLossAudio(true);
        //全屏动画
        holder.videoPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.videoPlayer.setIsTouchWiget(false);
        //不提示非Wifi
        holder.videoPlayer.setNeedShowWifiTip(false);
        //循环播放
        holder.videoPlayer.setLooping(true);
        //设置控制UI显示时间
        holder.videoPlayer.setDismissControlTime(1500);



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView headIcon;
        TextView videoAuthorName;
        TextView videoTitle;
        TextView videoDescription;
        StandardGSYVideoPlayer videoPlayer;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headIcon = (CircleImageView) itemView.findViewById(R.id.head_icon_sort);
            videoAuthorName = (TextView) itemView.findViewById(R.id.video_author_name_sort);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title_sort);
            videoDescription = (TextView) itemView.findViewById(R.id.video_description_sort);
            videoPlayer = (StandardGSYVideoPlayer) itemView.findViewById(R.id.gsy_video_player);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_sort);

        }
    }
}
