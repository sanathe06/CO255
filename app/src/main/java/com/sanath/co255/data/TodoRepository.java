package com.sanath.co255.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanath.co255.models.Todo;
import com.sanath.co255.net.TodoService;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoRepository {

    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://peaceful-plains-28026.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    TodoService service;

    public TodoRepository() {
        service = retrofit.create(TodoService.class);
    }

    public void getAllTodos(Callback<List<Todo>> callback) {
        service.listTodo().enqueue(callback);
    }

    public void createTodoItem(Todo newTodo, Callback<Todo> callback) {
        service.createTodo(newTodo).enqueue(callback);
    }

    public void deleteTodo(int id, Callback<Object> callback) {
        service.deleteTodo(id).enqueue(callback);
    }

    public void updateTodo(int id, Todo todo, Callback<Todo> callback) {
        service.updateTodo(id, todo).enqueue(callback);
    }
}
