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


public class lib_tab1 extends Fragment {


    Button btn;
    TextView date;
    DatePicker datePicker;
    int year_x;
    int month_x;
    int day_x;
    Calendar calendar;
    static final int DIALOG_ID = 0;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lib_make_entry, container, false);


        btn = getActivity().findViewById(R.id.btnDate);
        date = getActivity().findViewById(R.id.tvSelectedDate);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                getActivity().showDialog(DIALOG_ID);
            }
        });


        DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year_x = year;
                month_x = month;
                day_x = dayOfMonth;

//                Toast.makeText(lib_tab1.this, year_x + "/" + month_x + "/" + day_x, Toast.LENGTH_LONG.show());

            }
        };

//        private Dialog onCreateDialog(int id){
//            if (id == DIALOG_ID)
////                return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
//            return null;
//        }




        return view;
    }}