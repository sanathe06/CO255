package com.sanath.co255.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Todo implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("isCompleted")
    private boolean isCompleted;

    public Todo(String content, boolean isCompleted) {
        this.content = content;
        this.isCompleted = isCompleted;
    }

    protected Todo(Parcel in) {
        id = in.readInt();
        content = in.readString();
        isCompleted = in.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getContent() {
        return content;
    }

    @NonNull
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };
}
