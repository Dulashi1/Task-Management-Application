package com.example.labexam4;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.labexam4.Model.ToDoListModel;
import com.example.labexam4.R;
import com.example.labexam4.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private boolean isUpdate;

    public static AddNewTask newInstance() { // Static method to create new instance of AddNewTask fragment
        return new AddNewTask(); // Returning new instance of AddNewTask fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // Overriding onCreate method
        super.onCreate(savedInstanceState); // Calling super method
        setStyle(STYLE_NORMAL, R.style.DialogStyle); // Setting dialog style
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Overriding onCreateView method
        View view = inflater.inflate(R.layout.new_task, container, false); // Inflating layout for this fragment
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // Setting soft input mode for the dialog
        return view; // Returning inflated view
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // Overriding onViewCreated method
        super.onViewCreated(view, savedInstanceState); // Calling super method
        newTaskText = view.findViewById(R.id.newTaskText); // Initializing EditText view for new task text input
        newTaskSaveButton = view.findViewById(R.id.newTaskButton); // Initializing Button view for save button

        db = new DatabaseHandler(getActivity()); // Initializing database handler
        db.openDatabase(); // Opening database

        Bundle bundle = getArguments(); // Getting arguments bundle
        if (bundle != null) { // Checking if bundle is not null
            isUpdate = true; // Setting update flag to true
            String task = bundle.getString("task"); // Getting task text from bundle
            newTaskText.setText(task); // Setting task text to EditText
            if (task.length() > 0) // Checking if task text is not empty
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); // Setting button text color
        }

        newTaskText.addTextChangedListener(new TextWatcher() { // Adding text watcher to newTaskText EditText
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // Before text changed event

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // On text changed event
                if (s.toString().isEmpty()) { // Checking if text is empty
                    newTaskSaveButton.setEnabled(false); // Disabling save button
                    newTaskSaveButton.setTextColor(Color.GRAY); // Setting text color to gray
                } else { // If text is not empty
                    newTaskSaveButton.setEnabled(true); // Enabling save button
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); // Setting text color
                }
            }

            @Override
            public void afterTextChanged(Editable s) {} // After text changed event
        });

        newTaskSaveButton.setOnClickListener(new View.OnClickListener() { // Adding click listener to newTaskSaveButton
            @Override
            public void onClick(View v) { // On button click
                String text = newTaskText.getText().toString(); // Getting text from EditText
                if (isUpdate) { // If updating existing task
                    db.updateTask(bundle.getInt("id"), text); // Updating task in database
                } else { // If adding new task
                    ToDoListModel task = new ToDoListModel(); // Creating new task object
                    task.setTask(text); // Setting task text
                    task.setStatus(0); // Setting task status
                    db.insertTask(task); // Inserting task into database
                }
                dismiss(); // Dismissing the dialog
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) { // Overriding onDismiss method
        super.onDismiss(dialog); // Calling super method
        Activity activity = getActivity(); // Getting activity
        if (activity instanceof DialogCloseListener) { // Checking if activity implements DialogCloseListener
            ((DialogCloseListener) activity).handleDialogClose(dialog); // Handling dialog close event
        }
    }
}
