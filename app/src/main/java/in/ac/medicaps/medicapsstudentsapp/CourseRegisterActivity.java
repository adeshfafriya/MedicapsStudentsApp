package in.ac.medicaps.medicapsstudentsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import in.ac.medicaps.medicapsstudentsapp.Model.Courses;
import in.ac.medicaps.medicapsstudentsapp.ViewHolder.CourseViewHolder;

public class CourseRegisterActivity extends AppCompatActivity {
    private String mUserName, mFaculty, mDepartment, mYearOfGrad;
    private DatabaseReference coursesRef, courseRegisterRef;
    private RecyclerView recyclerView;
    private HashMap<String, String> selectedCourses;
    RecyclerView.LayoutManager layoutManager;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_register);
        getSupportActionBar().setTitle("Select Courses");
        mUserName = getIntent().getStringExtra("userName");
        mFaculty = getIntent().getStringExtra("faculty");
        mDepartment = getIntent().getStringExtra("department");
        mYearOfGrad = getIntent().getStringExtra("yearOfGrad");
        courseRegisterRef = FirebaseDatabase.getInstance().getReference().child("users").child("students").child(mUserName).child("registeredCourses");
        coursesRef = FirebaseDatabase.getInstance().getReference().child("faculty").child(mFaculty).child("departments").child(mDepartment).child("year of graduation").child(mYearOfGrad).child("courses");
        selectedCourses = new HashMap<>();
        submitBtn = findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(CourseRegisterActivity.this);
                progressDialog.setTitle("Updating Information");
                progressDialog.setMessage("Please wait, while we are updating your courses information");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                courseRegisterRef.setValue(selectedCourses).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent intent = new Intent(CourseRegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Courses> options =
                new FirebaseRecyclerOptions.Builder<Courses>()
                        .setQuery(coursesRef, Courses.class)
                        .build();


        FirebaseRecyclerAdapter<Courses, CourseViewHolder> adapter =
                new FirebaseRecyclerAdapter<Courses, CourseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull final Courses model)
                    {
                        holder.courseNameTextView.setText(model.getCourseName());
                        holder.courseCodeTextView.setText(model.getCourseCode());
                        holder.courseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked){
                                    selectedCourses.put(model.getCourseCode(), model.getCourseName());
                                }
                                else {
                                    selectedCourses.remove(model.getCourseCode());
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_layout, parent, false);
                        CourseViewHolder holder = new CourseViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
