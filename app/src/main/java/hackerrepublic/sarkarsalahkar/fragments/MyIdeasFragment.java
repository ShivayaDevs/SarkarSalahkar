package hackerrepublic.sarkarsalahkar.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import hackerrepublic.sarkarsalahkar.R;
import hackerrepublic.sarkarsalahkar.models.Post;

/**
 * Handles the home screen's my ideas part.
 *
 * @author vagisha-nidhi
 */
public class MyIdeasFragment extends Fragment {

    /**
     * Members for setting to recycler view.
     */
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mMyRecyclerAdapter;

    /**
     * Progress bar that will be used to show that the items have not yet loaded.
     */
    ProgressBar progressBar;

    /**
     * Required constructor.
     */
    public MyIdeasFragment() {
    }

    /**
     * Called when view is created.
     *
     * @param inflater responsible for inflating view.
     * @param container inside which view will be inflated.
     * @param savedInstanceState previous instance.
     * @return the inflated view.
     */
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
                    if (post.author.equals(getString(R.string.current_default_user))) {
                        mMyRecyclerAdapter.addItem(post);
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
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
     * Adapter class for posts.
     */
    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
        private ArrayList<Post> posts;

        public MyRecyclerAdapter() {
            posts = new ArrayList<>();
        }

        @Override
        public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_post, parent,
                    false);
            return new MyRecyclerAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyRecyclerAdapter.MyViewHolder holder, int position) {
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

        /**
         * Posts view holder class.
         */
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
                Glide.with(getActivity()).load(post.imageUrl).into(coverImageView);
            }
        }
    }


}
