package com.example.coursapp.model;

public class Course {
    public Integer id;
    public String title;
    public String description;
    public String level;
    public String created_at;

    public Course() {
    }

    public Course(String title, String description, String level) {
        this.title = title;
        this.description = description;
        this.level = level;
    }
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLevel() {
        return level;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }
}
