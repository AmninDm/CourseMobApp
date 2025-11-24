package com.example.coursapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursapp.adapter.MyCoursesAdapter;
import com.example.coursapp.model.Course;
import com.example.coursapp.model.UserCourse;
import com.example.coursapp.network.request.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MyCoursesActivity extends AppCompatActivity {

    private RecyclerView recyclerMyCourses;
    private MyCoursesAdapter adapter;
    private List<Course> myCoursesList = new ArrayList<>();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Мои курсы");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        currentUserId = getIntent().getStringExtra("user_id");

        recyclerMyCourses = findViewById(R.id.recyclerMyCourses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMyCourses.setLayoutManager(layoutManager);

        adapter = new MyCoursesAdapter(myCoursesList);
        recyclerMyCourses.setAdapter(adapter);

        loadMyCourses();
    }

    private void loadMyCourses() {
        String userIdFilter = "eq." + currentUserId;

        ApiClient.getApi().getUserCourses(userIdFilter, "*").enqueue(new Callback<List<UserCourse>>() {
            @Override
            public void onResponse(Call<List<UserCourse>> call, Response<List<UserCourse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<UserCourse> userCourses = response.body();

                    loadCourseDetails(userCourses);
                } else {
                    Toast.makeText(MyCoursesActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserCourse>> call, Throwable t) {
                Toast.makeText(MyCoursesActivity.this, "Нет подключения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCourseDetails(List<UserCourse> userCourses) {
        myCoursesList.clear();

        for (UserCourse uc : userCourses) {
            String courseIdFilter = "eq." + uc.getCourseId();

            ApiClient.getApi().getAllCourses("*").enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        Course course = response.body().get(0);
                        myCoursesList.add(course);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Course>> call, Throwable t) {
                }
            });
        }
    }
}
