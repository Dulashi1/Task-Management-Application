package com.example.labexam4.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.labexam4.Model.ToDoListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;  // Declare constant for database version
    private static final String NAME = "toDoListDatabase";  // Declare constant for database name
    private static final String TODO_TABLE = "todo";  // Declare constant for table name
    private static final String ID = "id";  // Declare constant for column name of task ID
    private static final String TASK = "task";  // Declare constant for column name of task description
    private static final String STATUS = "status";  // Declare constant for column name of task status

    // SQL query to create the TODO table
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK + " TEXT, " +
            STATUS + " INTEGER)";

    private SQLiteDatabase db;  // Declare SQLiteDatabase variable to reference the database

    // Constructor to initialize the database
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);  // Call superclass constructor with database name and version
    }

    // Method to create the TODO table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);  // Execute SQL query to create the table
    }

    // Method to upgrade the database if the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);  // Execute SQL query to drop the table if it exists
        onCreate(db);  // Call onCreate() method to create a new table
    }

    // Method to open the database for writing
    public void openDatabase() {
        db = this.getWritableDatabase();  // Get a writable database
    }

    // Method to add a new task into the database
    public void insertTask(ToDoListModel task) {
        ContentValues cv = new ContentValues();  // Create a new ContentValues object to hold task values
        cv.put(TASK, task.getTask());  // Put task description into ContentValues
        cv.put(STATUS, 0);  // Put default status (0 for not completed) into ContentValues
        db.insert(TODO_TABLE, null, cv);  // Insert ContentValues into the database table
    }

    // Method to retrieve all tasks from the database
    public List<ToDoListModel> getAllTasks() {
        List<ToDoListModel> taskList = new ArrayList<>();  // Create a new ArrayList to store tasks
        Cursor cursor = null;  // Declare Cursor variable to iterate through query results
        try {
            db.beginTransaction();  // Begin a database transaction
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null);  // Execute query to retrieve all rows from the table
            if (cursor != null && cursor.moveToFirst()) {  // Check if cursor is not null and move to the first row
                do {
                    int idIndex = cursor.getColumnIndex(ID);  // Get column index of task ID
                    int taskIndex = cursor.getColumnIndex(TASK);  // Get column index of task description
                    int statusIndex = cursor.getColumnIndex(STATUS);  // Get column index of task status

                    // Check if column indexes are valid
                    if (idIndex >= 0 && taskIndex >= 0 && statusIndex >= 0) {
                        ToDoListModel task = new ToDoListModel();  // Create a new ToDoListModel object
                        task.setId(cursor.getInt(idIndex));  // Set task ID from cursor
                        task.setTask(cursor.getString(taskIndex));  // Set task description from cursor
                        task.setStatus(cursor.getInt(statusIndex));  // Set task status from cursor
                        taskList.add(task);  // Add task to the list
                    }
                } while (cursor.moveToNext());  // Move to the next row
            }
            db.setTransactionSuccessful();  // Set transaction successful if no exceptions occurred
        } finally {
            if (cursor != null) {
                cursor.close();  // Close the cursor to release its resources
            }
            db.endTransaction();  // End the transaction
        }
        return taskList;  // Return the list of tasks
    }

    // Method to update the status of a task
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();  // Create a new ContentValues object to hold updated values
        cv.put(STATUS, status);  // Put updated status into ContentValues
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});  // Update the row with the specified ID
    }

    // Method to update the task text
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();  // Create a new ContentValues object to hold updated values
        cv.put(TASK, task);  // Put updated task description into ContentValues
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});  // Update the row with the specified ID
    }

    // Method to delete a task from the database
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});  // Delete the row with the specified ID
    }
}

