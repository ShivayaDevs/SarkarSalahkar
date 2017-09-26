package hackerrepublic.sarkarsalahkar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hackerrepublic.sarkarsalahkar.fragments.MainFragment;
import hackerrepublic.sarkarsalahkar.fragments.MyIdeasFragment;
import hackerrepublic.sarkarsalahkar.fragments.ProfileFragment;

/**
 * Provides the userInterface for the home screen. The home screen is used to display the current
 * top posts from the topics that we follow, Profile and MyIdeas View. Note that these all are
 * individual fragments which are used.
 *
 * @author vermayash8
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Uses a tab layout, so viewpager and tabLayout work together.
     */
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the view items from XML.
        setContentView(R.layout.activity_home);

        // Set the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
            }
        });

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // If internet is not available.
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Please connect to internet and re-open the app.", Toast
                    .LENGTH_LONG).show();
        }
    }

    /**
     * Adds the fragments to the viewPagerAdapter and sets the adapter to view pager.
     *
     * @param viewPager to which items need to be set.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "Feed");
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new MyIdeasFragment(), "My Ideas");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Gets status of network.
     *
     * @param context context
     * @return boolean
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnected();
    }

}
