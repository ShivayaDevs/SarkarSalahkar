package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import hackerrepublic.sarkarsalahkar.models.User;

/**
 * Provides the functionality to select experts.
 *
 * @author shilpi75
 */
public class ExpertsSelectionActivity extends AppCompatActivity {

    /**
     * For database operations.
     */
    private FirebaseDatabase mFirebaseDatabase;

    /**
     * TAG for logging.
     */
    private static final String TAG = ExpertsSelectionActivity.class.getSimpleName();


    /**
     * Recycler view adapters.
     */
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;

    /**
     * Stores the tags that have been analyzed and found relevant. The tags will now be used to
     * query for the experts.
     */
    private ArrayList<String> tags;

    private ArrayList<User> selectedExperts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts_selection);
        Intent intent = this.getIntent();

        getSupportActionBar().setElevation(0);
        String postId = intent.getStringExtra("POST_KEY");
        tags = intent.getStringArrayListExtra("POST_TAGS");

        // This is here for testing purposes.
        // While testing we are sending no tags so, the results will be only for finance tags.
        if (tags == null) {
            Log.d(TAG, "No tags specified, set to finance...Make sure you're testing.");
            tags = new ArrayList<>();
            tags.add("Finance");
        }

        // Setting the recycler view.
        myRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewExpert);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myRecyclerAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference expertsRef = mFirebaseDatabase.getReference("experts");
        expertsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    // For the first user, it's our responsibility to make the progress bar
                    // invisible.
                    if (findViewById(R.id.progressLoader).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.progressLoader).setVisibility(View.GONE);
                        findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
                    }
                    myRecyclerAdapter.addItem(user, getScore(user));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Not needed.
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Not needed.
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Not needed.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Not needed.
            }
        });

        findViewById(R.id.button_escalate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent escalationIntent = new Intent(ExpertsSelectionActivity.this,
                        EscalationSuccessActivity.class);
                escalationIntent.putExtra("SELECTED_EXPERTS", selectedExperts);
                startActivity(escalationIntent);
            }
        });

    }

    /**
     * Generates and assigns a score to the retrieved mapping.
     *
     * @param user the expert user.
     * @return score that is assigned.
     */
    private int getScore(User user) {
        int score = 0;
        for (Map.Entry<String, Integer> e : user.rating.entrySet()) {
            if (tags.contains(e.getKey())) {
                score += e.getValue();
            }
        }
        return score;
    }

    /**
     * Adapter that is set to the recycler view for displaying experts.
     */
    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        /**
         * Here we need to maintain a scoreMap as well for the ordering of experts.
         */
        ArrayList<User> list = new ArrayList<>();
        Map<User, Integer> scoreMap = new HashMap<>();

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.list_item_expert,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItem(User expert, int score) {
            list.add(expert);
            Log.d(TAG, "addItem " + expert.name + " score:" + score);
            scoreMap.put(expert, score);
            //  sort in descending order of score.
            Collections.sort(list, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return scoreMap.get(u2) - scoreMap.get(u1);
                }
            });
            if (list.size() > 6) {
                list = (ArrayList<User>) list.subList(0, 6);
            }
            notifyDataSetChanged();
        }

        /**
         * Viewholder for the recycler views.
         * Note that currently we are not using the image view.
         * Since that's not a core function of the app, it has
         * not been used.
         */
        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName, textViewBio, textViewTags;
            ImageView dpView;
            CheckBox checkBox;

            MyViewHolder(View v) {
                super(v);
                textViewName = v.findViewById(R.id.textView_name);
                textViewBio = v.findViewById(R.id.textView_bio);
                textViewTags = v.findViewById(R.id.textView_tag);
                dpView = v.findViewById(R.id.dpView);
                checkBox = v.findViewById(R.id.checkBox);
            }

            void bind(int position) {
                User user = list.get(position);
                Log.d(TAG, "User " + position + " score:" + scoreMap.get(user));
                textViewName.setText(user.name);
                textViewBio.setText(user.bio);
                Map<String, Integer> ratingMap = user.rating;
                String displayString = "";
                for (Map.Entry<String, Integer> entry : ratingMap.entrySet()) {
                    displayString += entry.getKey() + ", ";
                }
                if (displayString.length() >= 2) {
                    displayString = displayString.substring(0, displayString.length() - 2);
                }
                textViewTags.setText(displayString);
                if (position % 2 == 0) {
                    dpView.setImageResource(R.drawable.ic_expert);
                } else {
                    dpView.setImageResource(R.drawable.ic_expert2);
                }
            }
        }
    }
}
