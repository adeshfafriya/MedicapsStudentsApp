package in.ac.medicaps.medicapsstudentsapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LibraryAssistant extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_assistant);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new lib_tab1());
        adapter.addFragment(new lib_tab2());
        viewPager.setAdapter(adapter);
    }
}


