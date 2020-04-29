package com.devhack.platform;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import Models.Feed;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedAdaterViewHolder> {
    Context context;
    ArrayList<Feed> list;

    public FeedAdapter(Context context, ArrayList<Feed> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FeedAdaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.feed_layout,parent,false);
        return new FeedAdapter.FeedAdaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdaterViewHolder holder, int position) {
        Feed obj=list.get(position);
        holder.title.setText(obj.getTitle());
        holder.desc.setText(obj.getDesc());
        if(obj.getFile()!=null){
            holder.pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // context.startActivity(new Intent());
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FeedAdaterViewHolder extends RecyclerView.ViewHolder{
        TextView title,desc,img,video;
        CardView pdf;
        public FeedAdaterViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            img=itemView.findViewById(R.id.img);
            video=itemView.findViewById(R.id.video);
            pdf=itemView.findViewById(R.id.pdf);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getImg()!=null)
            return 1;
        else if(list.get(position).getVideo()!=null)
            return 2;
        else if(list.get(position).getFile()!=null)
            return 3;
        else
            return 4;
    }
}
