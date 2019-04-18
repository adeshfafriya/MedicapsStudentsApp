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
import java.util.ArrayList;

import android.content.Context;

import android.widget.AdapterView;
import android.widget.ListView;

public class lib_tab1 extends Fragment {

//below variables are for date picker


    public lib_tab1(){}

//    below are some default codes
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((MainActivity)getActivity()).libraryAssistantTab1();
}}

