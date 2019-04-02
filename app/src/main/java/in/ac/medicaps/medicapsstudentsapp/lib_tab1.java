package in.ac.medicaps.medicapsstudentsapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


public class lib_tab1 extends AppCompatActivity {


    Button btn;
    TextView date;
    DatePickerDialog datePicker;
    int year_x;
    int month_x;
    int day_x;
    Calendar calendar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lib_make_entry);


        btn = findViewById(R.id.btnDate);
        date = findViewById(R.id.tvSelectedDate);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(lib_tab1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        date.setText("do");
                    }
                }, year_x, month_x, day_x);
                datePicker.show();
            }
        });
    }
}

