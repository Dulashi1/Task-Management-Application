package com.example.labexam4

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam4.Adapter.ToDoListAdapter
import com.example.labexam4.Model.ToDoListModel
import com.example.labexam4.Utils.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections



class MainActivity : AppCompatActivity(), DialogCloseListener { // MainActivity class implementing DialogCloseListener interface

    private lateinit var worksRecyclerView: RecyclerView // RecyclerView variable declaration
    private lateinit var tasksAdapter: ToDoListAdapter // ToDoListAdapter variable declaration
    private var taskList = mutableListOf<ToDoListModel>() // Mutable list declaration for task items
    private lateinit var db: DatabaseHandler // DatabaseHandler variable declaration
    private lateinit var fab: FloatingActionButton // FloatingActionButton variable declaration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHandler(this) // Initializing DatabaseHandler
        db.openDatabase() // Opening the database

        worksRecyclerView = findViewById(R.id.worksRecyclerView) // Initializing RecyclerView
        worksRecyclerView.layoutManager = LinearLayoutManager(this) // Setting layout manager
        tasksAdapter = ToDoListAdapter(db, this) // Initializing adapter with MainActivity context
        worksRecyclerView.adapter = tasksAdapter // Setting adapter to RecyclerView

        fab = findViewById(R.id.fab) // Initializing FloatingActionButton

        // Setting up ItemTouchHelper for swipe actions on RecyclerView items
        val itemTouchHelper = ItemTouchHelper(RecyclerItemHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(worksRecyclerView)

        taskList = db.getAllTasks().toMutableList() // Fetching tasks from database and converting to mutable list
        Collections.reverse(taskList) // Reversing the task list to show latest tasks first
        tasksAdapter.setTasks(taskList) // Setting tasks to adapter

        fab.setOnClickListener { // Setting click listener for FloatingActionButton
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG) // Show AddNewTask dialog
        }
    }

    override fun handleDialogClose(dialog: DialogInterface?) { // Override method to handle dialog close event
        taskList = db.allTasks.toMutableList() // Fetching tasks from database and converting to mutable list
        Collections.reverse(taskList) // Reversing the task list to show latest tasks first
        tasksAdapter.setTasks(taskList) // Setting updated tasks to adapter
    }
}
