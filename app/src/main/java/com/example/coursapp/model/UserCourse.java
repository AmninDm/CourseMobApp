package com.example.coursapp.model;

public class UserCourse {
    public Integer id;
    public Integer user_id;
    public Integer course_id;
    public Integer progress;
    public String created_at;

    public UserCourse() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public Integer getCourseId() {
        return course_id;
    }

    public Integer getProgress() {
        return progress;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public void setCourseId(Integer course_id) {
        this.course_id = course_id;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }
}
