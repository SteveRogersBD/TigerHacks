package com.example.greenpulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.YoutubeVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    Context context;
    List<YoutubeVideo.VideoResult> videos;

    public VideoAdapter(Context context, List<YoutubeVideo.VideoResult> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YoutubeVideo.VideoResult video = videos.get(position);
        holder.title.setText(video.title);
        String descriptionS = video.channel.name+" | "+video.views+" | "+video.published_date;
        holder.desc.setText(descriptionS);
        if (video.thumbnail.statics != null && !video.thumbnail.statics.isEmpty()) {
            Toast.makeText(context, video.thumbnail.statics, Toast.LENGTH_SHORT).show();
            Picasso.get()
                    .load(video.thumbnail.statics)
                    .error(R.drawable.farming)
                    .into(holder.thumbNailPoster);
        } else {
            Picasso.get()
                    .load(video.thumbnail.statics)
                    .placeholder(R.drawable.farming)
                    .error(R.drawable.farming)
                    .into(holder.thumbNailPoster);
        }
        Picasso.get()
                .load(video.thumbnail.statics)
                .into(holder.thumbNailPoster);


        Picasso.get()
                .load(video.channel.thumbnail)
                .placeholder(R.drawable.farming)
                .into(holder.chanelPic);

        // Set click listener to open YouTube video
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.link));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbNailPoster;
        CircleImageView chanelPic;
        TextView title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbNailPoster = itemView.findViewById(R.id.poster_video);
            chanelPic = itemView.findViewById(R.id.chanel_img);
            title = itemView.findViewById(R.id.caption);
            desc = itemView.findViewById(R.id.description);
        }
    }
}
