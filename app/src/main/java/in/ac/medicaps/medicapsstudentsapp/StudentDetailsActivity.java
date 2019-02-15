package in.ac.medicaps.medicapsstudentsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootDatabaseReference;
    private DatabaseReference facultyDatabaseReference;
    private String mGender, mFaculty, mDepartment, mSection, mYearOfGrad, mBus, mBusStop;
    private EditText nameEditTextView, enrollmentEditTextView;
    private Spinner facultySpinner, departmentSpinner,sectionSpinner, yearOfGradSpinner, genderSpinner, busStopSpinner;
    private RadioGroup radioGroupBus;
    private FirebaseAuth mFirebaseAuth;
    private  String mUserName;
    private ArrayAdapter<String> facultySpinnerAdapter, genderSpinnerAdapter, busStopSpinnerAdapter, departmentSpinnerAdapter, yearGradSpinnerAdapter,sectionSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        getSupportActionBar().setTitle("Details");
        Log.e("Count", "at the start of activity");
        mUserName = getIntent().getStringExtra("userName");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRootDatabaseReference = mFirebaseDatabase.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        facultyDatabaseReference = mFirebaseDatabase.getReference().child("faculty");
        nameEditTextView = findViewById(R.id.editTextName);
        enrollmentEditTextView = findViewById(R.id.editTextEnrollment);
        facultySpinner = findViewById(R.id.spinnerFaculty);
        departmentSpinner = findViewById(R.id.spinnerDepartment);
        sectionSpinner = findViewById(R.id.spinnerSection);
        yearOfGradSpinner = findViewById(R.id.spinnerYearGrad);
        genderSpinner = findViewById(R.id.SpinnerGender);
        busStopSpinner = findViewById(R.id.spinnerBusStop);
        radioGroupBus = findViewById(R.id.radioGroupBus);
        busStopSpinner.setEnabled(false);
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        nameEditTextView.setText(user.getDisplayName());
        final ArrayList<String> faculties = new ArrayList<String>();
        facultyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot facultySnapshot: dataSnapshot.getChildren()) {
                    String name = facultySnapshot.getKey();
                    faculties.add(name);
                }
                facultySpinnerAdapter = new ArrayAdapter<String>(StudentDetailsActivity.this,
                        android.R.layout.simple_spinner_item,faculties);
                facultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                facultySpinner.setAdapter(facultySpinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w( "loadPost:onCancelled", databaseError.toException());
            }
        });
        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mFaculty = selection;
                    SetUpDepartmentSpinner();
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFaculty = "Unknown"; // Unknown
            }
        });

        final ArrayList<String> genders = new ArrayList<String>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        genderSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,genders);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mGender = selection;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = "Unknown"; // Unknown
            }
        });

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioBusYes:
                if (checked){
                    mBus = "yes";
                    busStopSpinner.setEnabled(true);
                    setUpBusStopSpinner();
                    break;}
            case R.id.radioBusNo:
                if (checked){
                    mBus = "no";
                    mBusStop = "";
                    busStopSpinner.setEnabled(false);
                    break;}
        }
    }

    private void setUpBusStopSpinner() {

        final ArrayList<String> busStops = new ArrayList<String>();
        mRootDatabaseReference.child("bus_stops").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot busStopSnapshot : dataSnapshot.getChildren()) {
                    String stop = busStopSnapshot.getKey();
                    busStops.add(stop);
                }
                busStopSpinnerAdapter = new ArrayAdapter<String>(StudentDetailsActivity.this,
                        android.R.layout.simple_spinner_item, busStops);
                busStopSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                busStopSpinner.setAdapter(busStopSpinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
        busStopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mBusStop = selection;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBusStop = "Unknown"; // Unknown
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        facultySpinnerAdapter.clear();
        departmentSpinnerAdapter.clear();
        yearGradSpinnerAdapter.clear();
        sectionSpinnerAdapter.clear();
        genderSpinnerAdapter.clear();
        busStopSpinnerAdapter.clear();
    }

    private void SetUpDepartmentSpinner() {
        if (!mFaculty.isEmpty()) {
            final ArrayList<String> departments = new ArrayList<String>();
            facultyDatabaseReference.child(mFaculty).child("departments").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                        String name = departmentSnapshot.getKey();
                        departments.add(name);

                    }
                    departmentSpinnerAdapter = new ArrayAdapter<String>(StudentDetailsActivity.this,
                            android.R.layout.simple_spinner_item, departments);
                    departmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    departmentSpinner.setAdapter(departmentSpinnerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException());
                }
            });
            departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
                    if (!TextUtils.isEmpty(selection)) {
                        mDepartment = selection;
                        setUpYearGradSpinner();
                    }
                }

                // Because AdapterView is an abstract class, onNothingSelected must be defined
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mDepartment = "Unknown"; // Unknown
                }
            });
        }
    }

    private void setUpYearGradSpinner() {
        if (!mDepartment.isEmpty()) {
            final ArrayList<String> yearGrads = new ArrayList<String>();
            facultyDatabaseReference.child(mFaculty).child("departments").child(mDepartment).child("year of graduation").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot yearGradSnapshot : dataSnapshot.getChildren()) {
                        String yearGradNumber = yearGradSnapshot.getKey();
                        yearGrads.add(yearGradNumber);

                    }
                    yearGradSpinnerAdapter = new ArrayAdapter<String>(StudentDetailsActivity.this,
                            android.R.layout.simple_spinner_item, yearGrads);
                    yearGradSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    yearOfGradSpinner.setAdapter(yearGradSpinnerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException());
                }
            });
            yearOfGradSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
                    if (!TextUtils.isEmpty(selection)) {
                        mYearOfGrad = selection;
                        setUpSectionSpinner();
                    }
                }

                // Because AdapterView is an abstract class, onNothingSelected must be defined
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mYearOfGrad = "Unknown"; // Unknown
                }
            });
        }
    }

    private void setUpSectionSpinner() {
        if (!mYearOfGrad.isEmpty()) {
            final ArrayList<String> sections = new ArrayList<String>();
            facultyDatabaseReference.child(mFaculty).child("departments").child(mDepartment).child("year of graduation").child(mYearOfGrad).child("section").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot sectionSnapshot : dataSnapshot.getChildren()) {
                        String sectionNumber = sectionSnapshot.getKey();
                        sections.add(sectionNumber);

                    }
                    sectionSpinnerAdapter = new ArrayAdapter<String>(StudentDetailsActivity.this,
                            android.R.layout.simple_spinner_item, sections);
                    sectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    sectionSpinner.setAdapter(sectionSpinnerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException());
                }
            });
            sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
                    if (!TextUtils.isEmpty(selection)) {
                        mSection = selection;
                    }
                }

                // Because AdapterView is an abstract class, onNothingSelected must be defined
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mSection = "Unknown"; // Unknown
                }
            });
        }
    }

}

