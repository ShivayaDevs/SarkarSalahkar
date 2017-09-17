package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
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

    private ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts_selection);
        Intent intent = this.getIntent();

        String postId = intent.getStringExtra("POST_KEY");
        tags = intent.getStringArrayListExtra("POST_TAGS");

        if(tags == null){
            tags = new ArrayList<>();
        }

        myRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewExpert);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        mRecyclerView.setAdapter(myRecyclerAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference expertsRef = mFirebaseDatabase.getReference("experts");
        expertsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    myRecyclerAdapter.addItem(user, getScore(user));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private int getScore(User user) {
        int score = 0;
        for (Map.Entry<String, Integer> e : user.rating.entrySet()) {
            if (tags.contains(e.getKey())) {
                score += e.getValue();
            }
        }
        return score;
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

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
            Log.d(TAG,"addItem " + expert.name + " score:" + score);
            scoreMap.put(expert, score);
            Collections.sort(list, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return scoreMap.get(u2) - scoreMap.get(u1);
                }
            });
            //TODO: check
//            list = (ArrayList<User>) list.subList(0, 7);
            notifyDataSetChanged();
        }


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
                textViewTags.setText(displayString);
            }
        }
    }
}
