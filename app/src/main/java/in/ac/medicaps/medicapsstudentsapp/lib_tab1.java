package in.ac.medicaps.medicapsstudentsapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;



public class lib_tab1 extends AppCompatActivity {

//below variables are for date picker
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


//    below are some default codes
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lib_make_entry);

//below setting are for datepicker (btn and hidden textview method)

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
                        date.setText(day + "/" + (month+1) + "/" + year);
                    }
                }, year_x, month_x, day_x);
                datePicker.show();
            }
        });

        //below setting are for datepicker for alarm (Textedit method)

        choose_date = findViewById(R.id.AlarmDate);

        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year_y = calendar.get(Calendar.YEAR);
                month_y = calendar.get(Calendar.MONTH);
                day_y = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(lib_tab1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        choose_date.setText(day + "/" + (month+1) + "/" + year);
                    }
                }, year_y, month_y, day_y);
                datePicker.show();
            }
        });

//        Below Settings are for Timepicker of alarm time

        choose_time = findViewById(R.id.AlarmTime);
        choose_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                PickerDialog = new TimePickerDialog(lib_tab1.this, new TimePickerDialog.OnTimeSetListener() {
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

