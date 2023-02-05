package com.example.spit_hackathon_ecoquest.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spit_hackathon_ecoquest.BottomSheets.AddressGetterBottomSheet;
import com.example.spit_hackathon_ecoquest.HaraBazarActivity;
import com.example.spit_hackathon_ecoquest.Models.Item;
import com.example.spit_hackathon_ecoquest.Models.Orders;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    List<Orders> testModelList;

    public OrderAdapter(Context context, List<Orders> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_orders, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (testModelList != null && testModelList.size() != 0) {
            GestureDetector gestureDetector = new GestureDetector(context, new SingleTapClick());

            Orders orders = testModelList.get(position);
            holder.name.setText(orders.getName());
            holder.address.setText(orders.getAddress());
            holder.price.setText(orders.getPrice());

        } else return;
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView price, name,address;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);
        }
    }
}
