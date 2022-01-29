package com.sanath.co255.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sanath.co255.R;
import com.sanath.co255.models.Todo;

public class TodoActivity extends AppCompatActivity {

    public static final int RESULT_CODE = 10001;

    Todo todo;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        //get the data from the intent , which sent from TodosActivity
        todo = getIntent().getParcelableExtra("todo");
        position = getIntent().getIntExtra("position", 0);

        CheckBox checkBoxChecked = findViewById(R.id.checkBoxChecked);
        EditText editTextContent = findViewById(R.id.editTextContent);
        Button buttonSave = findViewById(R.id.buttonSave);

        editTextContent.setText(todo.getContent());
        editTextContent.addTextChangedListener(textWatcher);

        checkBoxChecked.setChecked(todo.isCompleted());
        checkBoxChecked.setOnCheckedChangeListener((buttonView, isChecked) -> todo.setCompleted(isChecked));

        buttonSave.setOnClickListener(v -> {
            //pass updated back to TodosActivity
            setResult(RESULT_CODE, new Intent().putExtra("todo", todo).putExtra("position", position));
            finish();
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            todo.setContent(s.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}