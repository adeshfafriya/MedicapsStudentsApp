package in.ac.medicaps.medicapsstudentsapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper myDb;
    EditText editbook_id, editbook_name, editissue_date, editreturn_date, editTextId;
    Button btnSaveData;
    Button btnViewAll;
    Button btnUpdate;
    Button btnDelete;




    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mStudentsDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editTextId = (EditText) findViewById(R.id.editTextID);
        editbook_id = (EditText) findViewById(R.id.editText_book_id);
        editbook_name = (EditText) findViewById(R.id.editText_book_name);
        editissue_date = (EditText) findViewById(R.id.editText_issue_date);
        editreturn_date = (EditText) findViewById(R.id.editText_return_date);
        editTextId = (EditText) findViewById(R.id.editText_Id)
        btnSaveData = (Button) findViewById(R.id.button_save);
        btnViewAll = (Button) findViewById(R.id.button_view);
        btnUpdate = (Button) findViewById(R.id.button_Update);
        btnDelete = (Button) findViewById(R.id.button_delete);
        AddData();
        viewAll();
        updateData();
        deleteData();



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
                if (user != null){
                    checkDetails();
                }
                else {
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
    public void AddData(){
        btnSaveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertdata(editbook_id.getText().toString(),
                                editbook_name.getText().toString(),
                                editissue_date.getText().toString(),
                                editreturn_date.getText().toString(),
                                );
                        if(isInserted = true)
                            Toast.makeText(MainActivity.this,"DATA SAVED", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"DATA NOT SAVED", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
    public void viewAll(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0){
                            //show message
                            showMessage("ERROR","NOTHING FOUND" );
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()){
                            buffer.append("book_id" + res.getString(0)+"\n");
                            buffer.append("book_name" + res.getString(1)+"\n");
                            buffer.append("issue_date" + res.getString(2)+"\n");
                            buffer.append("return_date" + res.getString(3)+"\n\n");

                        }
                        //show data
                        showMessage("DATA",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
    }
    public void updateData(){

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = myDb.updateData(editTextId.getText().toString(), editbook_id.getText().toString(), editbook_name.getText().toString(), editissue_date.getText().toString(), editreturn_date.getText().toString() );
                        if (isUpdated == true)
                            Toast.makeText(MainActivity.this,"DATA UPDATED", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"DATA NOT UPDATED", Toast.LENGTH_LONG).show();



                    }
                }
        );
    }
    public void deleteData(){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if (deletedRows > 0)
                            Toast.makeText(MainActivity.this,"DATA DELETED", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"DATA NOT DELETED", Toast.LENGTH_LONG).show();
                    }
                }
        );
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
                if (Email.equals(user.getEmail())&&Enrollment.equals("")){
                    Intent intent = new Intent(MainActivity.this, StudentDetailsActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);

                }
                else if (!(dataSnapshot.hasChild("registeredCourses"))){
                    final String mFaculty, mDepartment, mYearOfGrad;
                    mFaculty = dataSnapshot.child("faculty").getValue(String.class);
                    mDepartment = dataSnapshot.child("department").getValue(String.class);
                    mYearOfGrad = dataSnapshot.child("yearOfGrad").getValue(String.class);

                    Intent intent  = new Intent(MainActivity.this, CourseRegisterActivity.class);
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
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                FirebaseUserMetadata metadata = mFirebaseAuth.getCurrentUser().getMetadata();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                String userEmailId = user.getEmail();
                String[] arrOfStr = userEmailId.split("@", 2);
                String userName = arrOfStr[0];
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    in.ac.medicaps.medicapsstudentsapp.UserData userData = new in.ac.medicaps.medicapsstudentsapp.UserData(user.getUid(), user.getEmail(), user.getDisplayName(), "","","","","","","","","");
                    mStudentsDatabaseReference.child(userName).setValue(userData);
                } else {
                    Toast.makeText(this, "Signed in",Toast.LENGTH_SHORT).show();
                }

                checkDetails();

            } else if (resultCode == RESULT_CANCELED){
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
    protected void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
