package com.sanath.co255.ui;

import com.sanath.co255.models.Todo;

public interface ItemActionListener {
    void onItemStatusChange(int position,Todo todo, boolean checked);
    void onItemDelete(int position,Todo todo);
    void onItemTap(int position,Todo todo);
}
