package com.example.spit_hackathon_ecoquest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spit_hackathon_ecoquest.Models.Task;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    List<Task> testModelList;

    public TaskAdapter(Context context, List<Task> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (testModelList != null && testModelList.size() != 0) {
            GestureDetector gestureDetector = new GestureDetector(context, new SingleTapClick());

            Task task = testModelList.get(position);

            holder.task.setText(task.getTask());
            holder.weekDay.setText(task.getDay());
            holder.greenPoints.setText(task.getGreenPoints());



//            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    new OnPressUI().onPressUi(view, motionEvent);
//                    if (gestureDetector.onTouchEvent(motionEvent)) {
////                        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
////                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
////
////                        // Set the message show for the Alert time
////                        builder.setMessage("Do you want to exit ?");
////
////                        // Set Alert Title
////                        builder.setTitle("Alert !");
////
////                        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
////                        builder.setCancelable(false);
////
////                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
////                            // When the user click yes button then app will close
////                        });
////
////                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
////                            // If user click no then dialog box is canceled.
////                            dialog.cancel();
////                        });
////
////                        AlertDialog alertDialog = builder.create();
////                        // Show the Alert Dialog box
////                        alertDialog.show();
//                    }
//                    return true;
//                }
//            });
        } else return;
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView weekDay, task, greenPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            weekDay = itemView.findViewById(R.id.dayOfTheWeek);
            task = itemView.findViewById(R.id.task);
            greenPoints = itemView.findViewById(R.id.green_points);
        }
    }
}
