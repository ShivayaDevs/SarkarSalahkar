package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Provides the functionality to select experts.
 *
 * @author shilpi75
 */
public class ExpertsSelectionActivity extends AppCompatActivity {

    /**
     * TAG for logging.
     */
    private static final String TAG = ExpertsSelectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts_selection);
        Intent intent = this.getIntent();

        String postId = intent.getStringExtra("POST_KEY");
        ArrayList<String> tags = intent.getStringArrayListExtra("POST_TAGS");

        Log.d(TAG, "Post id is : " + postId);
        Log.d(TAG, "Tags are: " + tags);
    }
}
