package com.example.labexam4.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.labexam4.AddNewTask;
import com.example.labexam4.MainActivity;
import com.example.labexam4.Model.ToDoListModel;
import com.example.labexam4.R;
import com.example.labexam4.Utils.DatabaseHandler;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private List<ToDoListModel> todoList; // List to hold ToDoListModel items
    private MainActivity activity; // Reference to MainActivity
    private DatabaseHandler db; // Reference to DatabaseHandler for database operations

    // Constructor to initialize adapter with DatabaseHandler and MainActivity
    public ToDoListAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    // Inflating layout for each item in RecyclerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    // Binding data to each item in RecyclerView
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase(); // Opening database connection
        ToDoListModel item = todoList.get(position); // Getting ToDoListModel object at current position
        holder.task.setText(item.getTask()); // Setting task text
        holder.task.setChecked(toBoolean(item.getStatus())); // Setting checkbox checked status
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Updating status in database based on checkbox state change
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    // Getting total number of items in RecyclerView
    @Override
    public int getItemCount() {
        if (todoList == null) return 0;
        return todoList.size();
    }

    // Utility method to convert integer to boolean
    private boolean toBoolean(int n) {
        return n != 0;
    }

    // Method to set ToDoListModel items in adapter
    public void setTasks(List<ToDoListModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    // Method to get context from MainActivity
    public Context getContext() {
        return activity;
    }

    // Method to delete item from RecyclerView and database
    public void deleteItem(int position) {
        ToDoListModel item = todoList.get(position);
        db.deleteTask(item.getId()); // Deleting task from database
        todoList.remove(position); // Removing item from list
        notifyItemRemoved(position); // Notifying adapter about item removal
    }

    // Method to edit item in RecyclerView
    public void editItem(int position) {
        ToDoListModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    // ViewHolder class to hold references to views for each item in RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task; // Checkbox for task

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.checkbox); // Initializing checkbox
        }
    }
}

