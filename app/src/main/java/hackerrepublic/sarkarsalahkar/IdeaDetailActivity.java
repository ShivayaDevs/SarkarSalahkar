package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hackerrepublic.sarkarsalahkar.models.Comment;
import hackerrepublic.sarkarsalahkar.models.Post;

public class IdeaDetailActivity extends AppCompatActivity {

    private static final String TAG = IdeaDetailActivity.class.getSimpleName();

    private TextView titleView, authorView, starsView, descriptionView;
    ImageView coverImageView;
    RecyclerView recyclerView;

    CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_detail);

        Intent intent = this.getIntent();
        String postId = intent.getStringExtra("POST_KEY");

        Log.d(TAG, "Got key as:" + postId);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postRef = firebaseDatabase.getReference("posts").child(postId);
        DatabaseReference commentsRef = firebaseDatabase.getReference("comments").child
                ("-KuA-5gQfhDl4FuiODES");

        initializeViews();

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    setPostToUI(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Unable to fetch post from server!");
                Toast.makeText(IdeaDetailActivity.this, "Failed to fetch details from server.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        addComments(commentsRef);
    }

    private void initializeViews() {
        titleView = findViewById(R.id.titleTV);
        authorView = findViewById(R.id.authorTV);
        coverImageView = findViewById(R.id.coverImageView);
        starsView = findViewById(R.id.starsTV);
        descriptionView = findViewById(R.id.descTV);
        recyclerView = findViewById(R.id.comment_ercycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void addComments(DatabaseReference commentsRef) {
        commentsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    adapter.add(comment);
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

    private void setPostToUI(Post post) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        starsView.setText(post.numStars + " stars.");
        descriptionView.setText(post.description);
        Glide.with(this).load(post.imageUrl).into(coverImageView);
    }

    private class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

        ArrayList<Comment> comments = new ArrayList<>();

        @Override
        public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommentsViewHolder(getLayoutInflater().inflate(R.layout.list_item_comment,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(CommentsViewHolder holder, int position) {
            holder.comment.setText(comments.get(position).comment);
            holder.author.setText(comments.get(position).commentor);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public void add(Comment comment) {
            comments.add(comment);
            notifyDataSetChanged();
        }

        class CommentsViewHolder extends RecyclerView.ViewHolder {
            TextView author, comment;

            CommentsViewHolder(View v) {
                super(v);
                author = v.findViewById(R.id.commentor_name);
                comment = v.findViewById(R.id.comment_tv);
            }
        }
    }
}
