package com.sanath.co255.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanath.co255.R;
import com.sanath.co255.models.Todo;
import com.sanath.co255.ui.ItemActionListener;

import java.util.List;

public class TodoAdapter extends
        RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private final List<Todo> todos;
    private final ItemActionListener itemActionListener;

    public TodoAdapter(List<Todo> todos, ItemActionListener itemActionListener) {
        this.todos = todos;
        this.itemActionListener = itemActionListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.content.setText(todo.getContent());
        holder.done.setChecked(todo.isCompleted());
        holder.done.setOnCheckedChangeListener((buttonView, isChecked) -> itemActionListener.onItemStatusChange(position, todo, isChecked));
        holder.delete.setOnClickListener(v -> itemActionListener.onItemDelete(position, todo));
        holder.itemView.setOnClickListener(v -> itemActionListener.onItemTap(position,todo));
    }

    @Override
    public int getItemCount() {
        return todos == null ? 0 : todos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox done;
        public ImageButton delete;
        public TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            done = itemView.findViewById(R.id.done);
            content = itemView.findViewById(R.id.content);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
