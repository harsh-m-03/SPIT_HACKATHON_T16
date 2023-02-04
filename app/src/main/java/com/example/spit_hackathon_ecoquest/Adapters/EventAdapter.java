package com.example.spit_hackathon_ecoquest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.spit_hackathon_ecoquest.Models.Events;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Context context;
    List<Events> testModelList;

    public EventAdapter(Context context, List<Events> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_events, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (testModelList != null && testModelList.size() != 0) {
            GestureDetector gestureDetector = new GestureDetector(context, new SingleTapClick());

            Events events=testModelList.get(position);

            holder.title.setText(events.getTitle());
            holder.time.setText(events.getTime());
            holder.location.setText(events.getLocation());
            holder.green_points.setText(events.getGreen_points());
            holder.date.setText(events.getDate());

            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    new OnPressUI().onPressUi(view, motionEvent);
                    if (gestureDetector.onTouchEvent(motionEvent)) {
//                        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                        // Set the message show for the Alert time
//                        builder.setMessage("Do you want to exit ?");
//
//                        // Set Alert Title
//                        builder.setTitle("Alert !");
//
//                        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
//                        builder.setCancelable(false);
//
//                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
//                            // When the user click yes button then app will close
//                        });
//
//                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
//                            // If user click no then dialog box is canceled.
//                            dialog.cancel();
//                        });
//
//                        AlertDialog alertDialog = builder.create();
//                        // Show the Alert Dialog box
//                        alertDialog.show();
                    }
                    return true;
                }
            });
        } else return;
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, green_points, time,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            time = itemView.findViewById(R.id.time);
            green_points = itemView.findViewById(R.id.green_points);
            date = itemView.findViewById(R.id.date);
        }
    }
}
