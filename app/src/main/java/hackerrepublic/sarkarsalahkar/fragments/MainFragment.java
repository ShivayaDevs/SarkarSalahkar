package hackerrepublic.sarkarsalahkar.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import hackerrepublic.sarkarsalahkar.IdeaDetailActivity;
import hackerrepublic.sarkarsalahkar.NewPostActivity;
import hackerrepublic.sarkarsalahkar.R;
import hackerrepublic.sarkarsalahkar.models.Post;

/**
 * Provides the Home screen user interface and control. Sets the views on home screen by fetching
 * data from backend. The backend this fetches the data is firebase.
 *
 * @author shilpi75
 */
public class MainFragment extends Fragment {

    /**
     * Variables required for the recycler view showing posts.
     */
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mMyRecyclerAdapter;

    private ProgressBar progressBar;

    /**
     * Required constructor.
     */
    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);

        // Initialize firebase database.
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.posts_recycler_view);
        mMyRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mMyRecyclerAdapter);

        progressBar = view.findViewById(R.id.main_screen_progress_bar);

        DatabaseReference postsRef = mFirebaseDatabase.getReference("posts");
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mMyRecyclerAdapter.addItem(dataSnapshot.getKey(), post);
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }
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

        return view;
    }

    /**
     * Provides the adapter to set on the PostsRecyclerView.
     */
    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
        private ArrayList<Post> posts;
        private ArrayList<String> keys;

        public MyRecyclerAdapter() {
            posts = new ArrayList<>();
            keys = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_post, parent,
                    false);
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

        public void addItem(String key, Post post) {
            posts.add(0,post);
            keys.add(0, key);
            notifyDataSetChanged();
        }

        public void removeItem(Post post) {
            posts.remove(post);
            notifyDataSetChanged();
        }

        /**
         * View Holder for the recycler view.
         */
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView authorView;
            private TextView timeView;
            private TextView postTitleView;
            private TextView postPreview;
            private TextView numStarsView;
            private ImageView coverImageView;

            private String key;

            MyViewHolder(View v) {
                super(v);
                authorView = v.findViewById(R.id.textView_author);
                timeView = v.findViewById(R.id.textView_timestamp);
                postTitleView = v.findViewById(R.id.textView_postTitle);
                postPreview = v.findViewById(R.id.textView_postPreview);
                numStarsView = v.findViewById(R.id.textView_numStars);
                coverImageView = v.findViewById(R.id.imageView_cover);
                v.setOnClickListener(this);
            }

            /**
             * Binds the data to the view.
             *
             * @param position position of item in list.
             */
            void bind(int position) {
                Post post = posts.get(position);
                authorView.setText(post.author);
                postTitleView.setText(post.title);
                postPreview.setText(post.description);
                numStarsView.setText(post.numStars + " stars");
                timeView.setText(post.creationTimestamp);
                Glide.with(getActivity()).load(post.imageUrl).into(coverImageView);
                key = keys.get(position);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IdeaDetailActivity.class);
                intent.putExtra("POST_KEY", key);
                startActivity(intent);
            }
        }
    }
}
