package com.example.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.checkBox.setChecked(task.isCompleted());

        // Alterar cor da tarefa se for concluída
        if (task.isCompleted()) {
            holder.itemView.setBackgroundColor(Color.GREEN);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        // Marcar tarefa como concluída
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            Toast.makeText(context, isChecked ? "Tarefa concluída" : "Tarefa pendente", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });

        // Excluir tarefa
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Tem certeza que deseja excluir esta tarefa?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        taskList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Tarefa excluída", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        CheckBox checkBox;
        Button deleteButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            checkBox = itemView.findViewById(R.id.checkBoxDone);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
        }
    }
}
