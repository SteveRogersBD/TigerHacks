package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.greenpulse.R;
import com.example.greenpulse.models.OnBoardItem;

import java.util.List;

public class OnBoardAdapter extends RecyclerView.Adapter<OnBoardAdapter.ViewHolder>{

    Context context;
    List<OnBoardItem>items;

    public OnBoardAdapter(Context context, List<OnBoardItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnBoardItem item = items.get(position);
        holder.title.setText(item.getDescription().toString());
        holder.imageView.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.main_image);
            title = itemView.findViewById(R.id.title_TV);
        }
    }
}
