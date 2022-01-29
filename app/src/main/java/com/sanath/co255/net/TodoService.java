package com.sanath.co255.net;

import com.sanath.co255.models.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoService {
    @GET("/posts")
    Call<List<Todo>> listTodo();

    @POST("/posts")
    Call<Todo> createTodo(@Body Todo todo);

    @PUT("/posts/{id}")
    Call<Todo> updateTodo(@Path("id") int Id,@Body Todo todo);

    @DELETE("/posts/{id}")
    Call<Object> deleteTodo(@Path("id") int Id);
}
