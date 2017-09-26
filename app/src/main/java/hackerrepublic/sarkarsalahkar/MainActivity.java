package hackerrepublic.sarkarsalahkar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles the user on-boarding process. The user is given a brief overview of the app using this
 * activity. This is the first thing to run when the app is opened.
 *
 * @author vermayash8
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Views required for on-boarding fragments.
     */
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnSkip, btnNext;
    private TextView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;

    /**
     * Stores the image resource id that is used to display on-boarding items.
     */
    int[] imageResources;

    /**
     * Called when activity is loaded.
     *
     * @param savedInstanceState if any instance was previously saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // Contains images that will be shown on different onoarding screens.
        imageResources = new int[]{
                R.drawable.ic_brainstorming_b,
                R.drawable.ic_contract,
                R.drawable.ic_rep_medal,
                R.drawable.ic_escalate,
                R.drawable.ic_promotion,
                R.drawable.ic_loss,
                R.drawable.ic_discover_singles,
        };
        // adding bottom dots
        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Listen to skip events.
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < imageResources.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    /**
     * Adds the bottom dots to the current page.
     *
     * @param currentPage page number, zero based.
     */
    private void addBottomDots(int currentPage) {
        dots = new TextView[imageResources.length];

        int colorActiveDot = ContextCompat.getColor(this, R.color.dot_light_active);
        int colorInactiveDot = ContextCompat.getColor(this, R.color.dot_dark_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactiveDot);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorActiveDot);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /**
     * Launches the home screen.
     */
    private void launchHomeScreen() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager
            .OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == imageResources.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.got_it));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Do nothing.
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Do nothing.
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        private String[] titles;
        private String[] descriptions;

        public MyViewPagerAdapter() {
            titles = getResources().getStringArray(R.array.start_screen_titles);
            descriptions = getResources().getStringArray(R.array.start_screen_descriptions);
            // For error checking.
            if (titles.length != descriptions.length || titles.length != imageResources.length) {
                throw new RuntimeException("Onboarding resources' length doesn't match.");
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.welcome_screen_1, container, false);

            ImageView imageView = view.findViewById(R.id.image);
            TextView titleView = view.findViewById(R.id.titleView);
            TextView descriptionView = view.findViewById(R.id.descriptionView);

            imageView.setImageResource(imageResources[position]);
            titleView.setText(titles[position]);
            descriptionView.setText(descriptions[position]);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return imageResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
