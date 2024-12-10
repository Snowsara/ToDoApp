package com.example.todo;

public class Task {
    private final String title;
    private boolean isCompleted;

    public Task(String title, String description, boolean isCompleted, String createdAt) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public String getTitle() { return title; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
