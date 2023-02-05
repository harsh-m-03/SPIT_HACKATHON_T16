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
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
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
            holder.price.setText(item.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Set the message show for the Alert time
                    builder.setMessage("Once you confirm your order you won't be able to undo the order, also the green points won't be return in any circumstances. Do you wish to continue?");

                    // Set Alert Title
                    builder.setTitle("Alert !");

                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        database.getReference().child("Test/Users").child(auth.getUid()).child("greenPoints")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userCoins = snapshot.getValue(String.class);
                                        int userCointsInInt = Integer.parseInt(userCoins);
                                        int priceOfItemInInt = Integer.parseInt(item.getPrice());

                                        if (priceOfItemInInt > userCointsInInt) {
                                            Toast.makeText(context, "Not enough coins", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();

                                        }else{
                                            AddressGetterBottomSheet frag = new AddressGetterBottomSheet();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", item.getName());
                                            bundle.putString("price", item.getPrice());
                                            bundle.putString("id", item.getUid());
                                            frag.setArguments(bundle);
                                            frag.show(((HaraBazarActivity) context).getSupportFragmentManager(), frag.getTag());

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                      });

                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
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
