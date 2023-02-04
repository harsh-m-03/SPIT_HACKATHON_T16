package com.example.spit_hackathon_ecoquest.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spit_hackathon_ecoquest.Models.Task;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.example.spit_hackathon_ecoquest.TaskProof;

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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.commit();
                    String sharedPreferencesValue = sharedPreferences.getString(task.getTask(), "true");

                    if(sharedPreferencesValue.equals("true")) {
                        Intent intent = new Intent(context, TaskProof.class);
                        intent.putExtra("day", task.getDay());
                        intent.putExtra("task", task.getTask());
                        intent.putExtra("green_points", task.getGreenPoints());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "Task Already completed", Toast.LENGTH_SHORT).show();
                    }

                }
            });

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
