package com.example.coursapp.network.request;

import com.example.coursapp.model.User;
import com.example.coursapp.model.Course;
import com.example.coursapp.model.UserCourse;
import com.example.coursapp.network.request.SignUpRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface SupabaseApi {

    String BASE_URL = "https://thdrqgopmsxlnwvxlalr.supabase.co/rest/v1/";
    String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRoZHJxZ29wbXN4bG53dnhsYWxyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM5ODUzMDksImV4cCI6MjA3OTU2MTMwOX0.LvN8OGauay3AYvKaND-pW2cmTWZvEAwqL-yzIHFrDn0";

    @POST("users")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    Call<List<User>> register(@Body SignUpRequest request);

    @GET("users")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Accept: application/json"
    })
    Call<List<User>> getUserByUsername(@Query("username") String username, @Query("select") String select);

    @GET("courses")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Accept: application/json"
    })
    Call<List<Course>> getAllCourses(@Query("select") String select);

    @POST("user_courses")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    Call<List<UserCourse>> addUserCourse(@Body UserCourse userCourse);

    @GET("user_courses")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Accept: application/json"
    })
    Call<List<UserCourse>> getUserCourses(@Query("user_id") String userId, @Query("select") String select);

    @DELETE("user_courses")
    @Headers({
            "apikey: " + API_KEY,
            "Authorization: Bearer " + API_KEY,
            "Accept: application/json"
    })
    Call<Void> deleteUserCourse(@Query("user_id") String userId, @Query("course_id") String courseId);
}
