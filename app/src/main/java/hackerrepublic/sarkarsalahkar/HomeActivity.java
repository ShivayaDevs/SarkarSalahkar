package hackerrepublic.sarkarsalahkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hackerrepublic.sarkarsalahkar.models.Post;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private MyRecyclerAdapter mMyRecyclerAdapter;

    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
        mMyRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMyRecyclerAdapter);

        DatabaseReference postsRef = mFirebaseDatabase.getReference("posts");
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mMyRecyclerAdapter.addItem(post);
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

    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private ArrayList<Post> posts;

        MyRecyclerAdapter() {
            posts = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_item_post, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public void addItem(Post post) {
            posts.add(post);
            notifyDataSetChanged();
        }

        public void removeItem(Post post) {
            posts.remove(post);
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView authorView;
            private TextView timeView;
            private TextView postTitleView;
            private TextView postPreview;
            private TextView numStarsView;
            private ImageView coverImageView;

            MyViewHolder(View v) {
                super(v);
                authorView = v.findViewById(R.id.textView_author);
                timeView = v.findViewById(R.id.textView_timestamp);
                postTitleView = v.findViewById(R.id.textView_postTitle);
                postPreview = v.findViewById(R.id.textView_postPreview);
                numStarsView = v.findViewById(R.id.textView_numStars);
                coverImageView = v.findViewById(R.id.imageView_cover);
            }

            void bind(int position) {
                Post post = posts.get(position);

                authorView.setText(post.author);
                postTitleView.setText(post.title);
                postPreview.setText(post.description);
                numStarsView.setText(post.numStars + " stars");
                timeView.setText("1 hr ago");
                Glide.with(HomeActivity.this).load(post.imageUrl).into(coverImageView);
            }
        }
    }

}


/*
    private void uploadToFirebase(Post post) {
        DatabaseReference postsRef = mFirebaseDatabase.getReference("posts");
        postsRef.push().setValue(post);
    }
    */