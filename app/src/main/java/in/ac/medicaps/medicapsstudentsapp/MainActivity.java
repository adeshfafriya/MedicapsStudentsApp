package in.ac.medicaps.medicapsstudentsapp;


import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import android.view.LayoutInflater;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mStudentsDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStudentsDatabaseReference = mFirebaseDatabase.getReference().child("users").child("students");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    checkDetails();
                } else {
                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.ic_logo)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    private void checkDetails() {
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userEmailId = user.getEmail();
        String[] arrOfStr = userEmailId.split("@", 2);
        final String userName = arrOfStr[0];
        mStudentsDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Email = dataSnapshot.child("email").getValue(String.class);
                String Enrollment = dataSnapshot.child("enrollmentNumber").getValue(String.class);
                if (Email.equals(user.getEmail()) && Enrollment.equals("")) {
                    Intent intent = new Intent(MainActivity.this, StudentDetailsActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);

                } else if (!(dataSnapshot.hasChild("registeredCourses"))) {
                    final String mFaculty, mDepartment, mYearOfGrad;
                    mFaculty = dataSnapshot.child("faculty").getValue(String.class);
                    mDepartment = dataSnapshot.child("department").getValue(String.class);
                    mYearOfGrad = dataSnapshot.child("yearOfGrad").getValue(String.class);

                    Intent intent = new Intent(MainActivity.this, CourseRegisterActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("faculty", mFaculty);
                    intent.putExtra("department", mDepartment);
                    intent.putExtra("yearOfGrad", mYearOfGrad);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUserMetadata metadata = mFirebaseAuth.getCurrentUser().getMetadata();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                String userEmailId = user.getEmail();
                String[] arrOfStr = userEmailId.split("@", 2);
                String userName = arrOfStr[0];
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    in.ac.medicaps.medicapsstudentsapp.UserData userData = new in.ac.medicaps.medicapsstudentsapp.UserData(user.getUid(), user.getEmail(), user.getDisplayName(), "", "", "", "", "", "", "", "", "");
                    mStudentsDatabaseReference.child(userName).setValue(userData);
                } else {
                    Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
                }

                checkDetails();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance().signOut(MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }


    Button btn;
    TextView date;
    DatePickerDialog datePicker;
    int year_x;
    int month_x;
    int day_x;
    Calendar calendar;

    EditText choose_date;
    int year_y;
    int month_y;
    int day_y;

    //    Below variables are for time picker
    EditText choose_time;
    TimePickerDialog PickerDialog;
    int currentHour;
    int currentMinute;
    String amPm;

    public void libraryAssistantTab1() {
        // body


        setContentView(R.layout.fragment_lib_make_entry);

//below setting are for datepicker (btn and hidden textview method)

        btn = findViewById(R.id.btnDate);
        date = findViewById(R.id.tvSelectedDate);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        year_x = calendar.get(Calendar.YEAR);
                        month_x = calendar.get(Calendar.MONTH);
                        day_x = calendar.get(Calendar.DAY_OF_MONTH);
                        datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                                date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year_x, month_x, day_x);
                        datePicker.show();
                    }
                }
        );

        //below setting are for datepicker for alarm (Textedit method)

        choose_date = findViewById(R.id.AlarmDate);

        choose_date.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               calendar = Calendar.getInstance();
                                               year_y = calendar.get(Calendar.YEAR);
                                               month_y = calendar.get(Calendar.MONTH);
                                               day_y = calendar.get(Calendar.DAY_OF_MONTH);
                                               datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                                   @Override
                                                   public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                                                       choose_date.setText(day + "/" + (month + 1) + "/" + year);
                                                   }
                                               }, year_y, month_y, day_y);
                                               datePicker.show();
                                           }
                                       }
        );

//        Below Settings are for Timepicker of alarm time

        choose_time = findViewById(R.id.AlarmTime);
        choose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                PickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        choose_time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                PickerDialog.show();
            }
        });

    }
}


