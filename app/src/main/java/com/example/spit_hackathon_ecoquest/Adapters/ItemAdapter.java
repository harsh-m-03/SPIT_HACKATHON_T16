package com.example.spit_hackathon_ecoquest.Adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spit_hackathon_ecoquest.Models.Item;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    List<Item> testModelList;

    public ItemAdapter(Context context, List<Item> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_hara_bazar, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (testModelList != null && testModelList.size() != 0) {
            GestureDetector gestureDetector = new GestureDetector(context, new SingleTapClick());

            Item item = testModelList.get(position);
            Picasso.get().load(item.getImage()).placeholder(R.drawable.dummy_image).into(holder.image);
            holder.name.setText(item.getName());
            holder.price.setText("$ "+item.getPrice());
        } else return;
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView price, name;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.itemImage);
        }
    }
}
