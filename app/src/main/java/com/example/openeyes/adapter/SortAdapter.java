package com.example.openeyes.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.openeyes.bean.SortItem;
import com.example.openeyes.util.Utils;
import com.example.openeyes.util.Value;
import com.example.openeyes.view.MainActivity;
import com.example.openeyes.R;
import com.example.openeyes.view.SortActivity;
import com.example.openeyes.viewmodel.MainViewModel;

import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private Context mContext;
    private List<SortItem> sortItemList;

    /*
     * Item type
     */
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View headerView = null;

    public SortAdapter(List<SortItem> sortItemList){
        this.sortItemList = sortItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        if(headerView!=null && viewType==TYPE_HEADER){
            ViewHolder holder = new ViewHolder(headerView);
            return holder;
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.sort_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    //启动SortActivity
                    Intent intent = new Intent(mContext, SortActivity.class);
                    intent.putExtra("sortId", sortItemList.get(position-1).getID());
                    mContext.startActivity(intent);
                }
            };
            holder.sortLayoutLeft.setOnClickListener(listener);
            holder.sortLayoutRight.setOnClickListener(listener);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            SortItem sortItem = sortItemList.get(position-1);
            if(position%2 != 0){    //加载左边布局
                Glide.with(mContext).load(sortItem.getImageUrl()).into(holder.sortImageLeft);
                holder.sortNameLeft.setText(sortItem.getSortName());
                holder.sortLayoutLeft.setVisibility(View.VISIBLE);  //左边可见
                holder.sortLayoutRight.setVisibility(View.GONE);    //右边不可见
            }else {     //加载右边布局
                Glide.with(mContext).load(sortItem.getImageUrl()).into(holder.sortImageRight);
                holder.sortNameRight.setText(sortItem.getSortName());
                holder.sortLayoutLeft.setVisibility(View.GONE); //左边不可见
                holder.sortLayoutRight.setVisibility(View.VISIBLE); //右边可见
            }
        }else{
            Glide.with(mContext).load("http://img.kaiyanapp.com/f6765ae9bcd4551ce40f10fe342b681c.jpeg?imageMogr2/quality/60").into(holder.sortImageHeader);
        }
    }

    @Override
    public int getItemCount() {
        if(headerView != null){
            return sortItemList.size() + 1;
        }
        return sortItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(headerView == null){
            return TYPE_NORMAL;
        }
        if(position == 0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    public void setHeaderView(View header) {
        headerView = header;
        notifyItemInserted(0);
    }

    public boolean isHeaderView(int position){
        if(getItemViewType(position) == TYPE_HEADER){
            return true;
        }
        return false;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sortImageLeft;
        ImageView sortImageRight;
        TextView sortNameLeft;
        TextView sortNameRight;
        ImageView sortImageHeader;
        FrameLayout sortLayoutLeft;
        FrameLayout sortLayoutRight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(itemView == headerView){
                sortImageHeader = (ImageView) itemView.findViewById(R.id.image_sort_header);
            }else{
                sortImageLeft = (ImageView) itemView.findViewById(R.id.sort_item_image_left);
                sortNameLeft = (TextView) itemView.findViewById(R.id.sort_item_name_left);
                sortImageRight = (ImageView) itemView.findViewById(R.id.sort_item_image_right);
                sortNameRight = (TextView) itemView.findViewById(R.id.sort_item_name_right);
                sortLayoutLeft = (FrameLayout) itemView.findViewById(R.id.sort_item_layout_left);
                sortLayoutRight = (FrameLayout) itemView.findViewById(R.id.sort_item_layout_right);
            }
        }
    }
}
