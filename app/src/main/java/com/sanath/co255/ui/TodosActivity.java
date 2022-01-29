package com.sanath.co255.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanath.co255.R;
import com.sanath.co255.data.TodoAdapter;
import com.sanath.co255.data.TodoRepository;
import com.sanath.co255.models.Todo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodosActivity extends AppCompatActivity implements ItemActionListener {

    private RecyclerView todoList;
    private EditText todoContent;
    private ImageButton save;
    private ProgressBar progressBar;

    private List<Todo> todos;
    TodoAdapter adapter;
    private TodoRepository todoRepository;


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("result", result.toString());
            if (result.getResultCode() == TodoActivity.RESULT_CODE) {
                if (result.getData() != null) {
                    Todo updatedTodo = result.getData().getParcelableExtra("todo");
                    int position = result.getData().getIntExtra("position", 0);
                    todos.remove(position);
                    todos.add(position, updatedTodo);
                    todoRepository.updateTodo(updatedTodo.getId(), updatedTodo, getTodoUpdateCallback(position));
                }
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        todoRepository = new TodoRepository();
        todos = new ArrayList<>();
        adapter = new TodoAdapter(todos, this);
        todoList.setAdapter(adapter);
        todoList.setLayoutManager(new LinearLayoutManager(this));
        loadTodo();
        save.setOnClickListener(v -> saveNewTodo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadTodo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        todoList = findViewById(R.id.todoList);
        todoContent = findViewById(R.id.todoContent);
        save = findViewById(R.id.save);
        progressBar = findViewById(R.id.progressBar);
        hideProgress();
    }

    private void saveNewTodo() {
        showProgress();
        Todo newTodo = new Todo(todoContent.getText().toString().trim(), false);
        todoRepository.createTodoItem(newTodo, todoCreateCallback);
        clearInputBox();
    }

    private void clearInputBox() {
        todoContent.setText("");
    }

    private void loadTodo() {
        showProgress();
        todoRepository.getAllTodos(loadAllTodoCallback);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemStatusChange(int position, Todo todo, boolean state) {
        showProgress();
        todo.setCompleted(state);
        todoRepository.updateTodo(todo.getId(), todo, getTodoUpdateCallback(position));
    }

    @Override
    public void onItemDelete(int position, Todo todo) {
        showProgress();
        todoRepository.deleteTodo(todo.getId(), getTodoDeleteCallback(position));
    }

    @Override
    public void onItemTap(int position, Todo todo) {
        Intent openTodo = new Intent(this, TodoActivity.class);
        openTodo.putExtra("todo", todo);
        openTodo.putExtra("position", position);
        activityResultLauncher.launch(openTodo);
    }

    Callback<List<Todo>> loadAllTodoCallback = new Callback<List<Todo>>() {
        @Override
        public void onResponse(@NonNull Call<List<Todo>> call, Response<List<Todo>> response) {
            hideProgress();
            List<Todo> newTodos = response.body();
            if (newTodos != null) {
                todos.clear();
                todos.addAll(newTodos);
                Log.d("TODO", newTodos.toString());
                adapter.notifyItemRangeChanged(0, newTodos.size());
            }
        }

        @Override
        public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable t) {
            hideProgress();
            Toast.makeText(TodosActivity.this, getText(R.string.get_todo_failed), Toast.LENGTH_SHORT).show();
        }
    };

    Callback<Todo> todoCreateCallback = new Callback<Todo>() {
        @Override
        public void onResponse(@NonNull Call<Todo> call, Response<Todo> response) {
            hideProgress();
            todos.add(0, response.body());
            adapter.notifyItemInserted(0);
        }

        @Override
        public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
            hideProgress();
            Toast.makeText(TodosActivity.this, getText(R.string.failed_create_todo), Toast.LENGTH_SHORT).show();
        }
    };

    @NonNull
    private Callback<Object> getTodoDeleteCallback(int position) {
        return new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                hideProgress();
                todos.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                hideProgress();
                Toast.makeText(TodosActivity.this, getText(R.string.failed_delete_todo), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    private Callback<Todo> getTodoUpdateCallback(final int position) {
        return new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                hideProgress();
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                hideProgress();
                Toast.makeText(TodosActivity.this, getText(R.string.failed_update_todo), Toast.LENGTH_SHORT).show();
            }
        };
    }
}