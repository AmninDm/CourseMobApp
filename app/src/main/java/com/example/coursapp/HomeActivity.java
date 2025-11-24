package com.example.coursapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursapp.adapter.CourseCleanAdapter;
import com.example.coursapp.adapter.MyCoursesAdapter;
import com.example.coursapp.model.Course;
import com.example.coursapp.model.UserCourse;
import com.example.coursapp.network.request.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerCourses;
    private CourseCleanAdapter allCoursesAdapter;
    private MyCoursesAdapter myCoursesAdapter;
    private List<Course> allCoursesList = new ArrayList<>();
    private List<Course> myCoursesList = new ArrayList<>();
    private MaterialToolbar toolbar;
    private boolean showingMyCourses = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerCourses = findViewById(R.id.recyclerCourses);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvNavHeaderName = headerView.findViewById(R.id.tvNavHeaderName);
        String currentUser = getSharedPreferences("app", MODE_PRIVATE)
                .getString("current_user", "Гость");
        tvNavHeaderName.setText("Привет, " + currentUser + "!");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCourses.setLayoutManager(layoutManager);

        allCoursesAdapter = new CourseCleanAdapter(allCoursesList);
        myCoursesAdapter = new MyCoursesAdapter(myCoursesList);

        recyclerCourses.setAdapter(allCoursesAdapter);

        allCoursesAdapter.setOnAddClickListener(position -> {
            Course course = allCoursesList.get(position);
            addCourseToUser(course);
        });

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
        loadAllCourses();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_all_courses) {
            showAllCourses();
        } else if (id == R.id.nav_my_courses) {
            showMyCourses();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAllCourses() {
        showingMyCourses = false;
        toolbar.setTitle("Все курсы");
        recyclerCourses.setAdapter(allCoursesAdapter);
        loadAllCourses();
    }

    private void showMyCourses() {
        showingMyCourses = true;
        toolbar.setTitle("Мои курсы");
        recyclerCourses.setAdapter(myCoursesAdapter);

        myCoursesAdapter.setOnDeleteClickListener((position, course) -> {
            deleteCourseFromUser(position, course);
        });

        loadMyCourses();
    }

    private void logout() {
        getSharedPreferences("app", MODE_PRIVATE)
                .edit()
                .remove("current_user")
                .remove("user_id")
                .apply();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadAllCourses() {
        ApiClient.getApi().getAllCourses("*").enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    allCoursesList.clear();
                    allCoursesList.addAll(response.body());
                    allCoursesAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(HomeActivity.this, "Ошибка загрузки курсов", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Нет подключения к серверу", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMyCourses() {
        String userId = getSharedPreferences("app", MODE_PRIVATE).getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show();
            return;
        }

        String userIdFilter = "eq." + userId;

        ApiClient.getApi().getUserCourses(userIdFilter, "*").enqueue(new Callback<List<UserCourse>>() {
            @Override
            public void onResponse(Call<List<UserCourse>> call, Response<List<UserCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserCourse> userCourses = response.body();
                    loadCourseDetails(userCourses);
                } else {
                    Toast.makeText(HomeActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserCourse>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Нет подключения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCourseDetails(List<UserCourse> userCourses) {
        myCoursesList.clear();

        if (userCourses.isEmpty()) {
            myCoursesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "У вас пока нет добавленных курсов", Toast.LENGTH_SHORT).show();
            return;
        }

        for (UserCourse uc : userCourses) {
            for (Course course : allCoursesList) {
                if (course.getId().equals(uc.getCourseId())) {
                    myCoursesList.add(course);
                    break;
                }
            }
        }
        myCoursesAdapter.notifyDataSetChanged();
    }

    private void addCourseToUser(Course course) {
        String userId = getSharedPreferences("app", MODE_PRIVATE)
                .getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show();
            return;
        }

        checkIfCourseExists(userId, course.getId(), exists -> {
            if (exists) {
                Toast.makeText(this, "Курс уже добавлен!", Toast.LENGTH_SHORT).show();
            } else {
                performAddCourse(userId, course);
            }
        });
    }

    private void checkIfCourseExists(String userId, Integer courseId, OnCheckCallback callback) {
        String userIdFilter = "eq." + userId;

        ApiClient.getApi().getUserCourses(userIdFilter, "*").enqueue(new Callback<List<UserCourse>>() {
            @Override
            public void onResponse(Call<List<UserCourse>> call, Response<List<UserCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean found = false;
                    for (UserCourse uc : response.body()) {
                        if (uc.getCourseId().equals(courseId)) {
                            found = true;
                            break;
                        }
                    }
                    callback.onResult(found);
                } else {
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<List<UserCourse>> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }

    private void performAddCourse(String userId, Course course) {
        UserCourse userCourse = new UserCourse();
        userCourse.setUserId(Integer.parseInt(userId));
        userCourse.setCourseId(course.getId());
        userCourse.setProgress(0);

        ApiClient.getApi().addUserCourse(userCourse).enqueue(new Callback<List<UserCourse>>() {
            @Override
            public void onResponse(Call<List<UserCourse>> call, Response<List<UserCourse>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Курс добавлен: " + course.getTitle(), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    Toast.makeText(HomeActivity.this, "Курс уже добавлен!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Ошибка добавления курса", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserCourse>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface OnCheckCallback {
        void onResult(boolean exists);
    }
    private void deleteCourseFromUser(int position, Course course) {
        String userId = getSharedPreferences("app", MODE_PRIVATE)
                .getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show();
            return;
        }
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Удалить курс?")
                .setMessage("Вы уверены, что хотите удалить курс \"" + course.getTitle() + "\"?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    performDeleteCourse(userId, position, course);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void performDeleteCourse(String userId, int position, Course course) {
        String userIdFilter = "eq." + userId;
        String courseIdFilter = "eq." + course.getId();

        ApiClient.getApi().deleteUserCourse(userIdFilter, courseIdFilter).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                Log.d("DELETE_COURSE_DEBUG", "URL: " + call.request().url());
                Log.d("DELETE_COURSE_DEBUG", "Код: " + response.code());

                if (response.isSuccessful() || response.code() == 204) {
                    myCoursesList.remove(position);
                    myCoursesAdapter.notifyItemRemoved(position);
                    myCoursesAdapter.notifyItemRangeChanged(position, myCoursesList.size());

                    Toast.makeText(HomeActivity.this, "Курс удалён: " + course.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
