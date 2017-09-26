package hackerrepublic.sarkarsalahkar;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hackerrepublic.sarkarsalahkar.models.Post;

/**
 * Activity for a new post to be added. Provides view for writing idea and selecting photo.
 *
 * @author vermayash8
 */
public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText titleInput;
    private EditText ideaInput;
    private String imageUrl;
    private ImageButton imageButton;

    private static final int PERMISSION_REQUEST_CODE = 232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        getSupportActionBar().setElevation(0);

        titleInput = (EditText) findViewById(R.id.editText_title);
        ideaInput = (EditText) findViewById(R.id.editText_idea);
        imageButton = (ImageButton) findViewById(R.id.imageButton_image);
        Button button = (Button) findViewById(R.id.buttonSave);

        imageButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButton_image) {
            // Allow the user to choose an image from gallery.
            // TODO(TEST): Ask for permission to access Storage in 23+ devices.
            getPermission();
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
                    .Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 101);
        } else if (view.getId() == R.id.buttonSave) {
            if (TextUtils.isEmpty(ideaInput.getText()) || TextUtils.isEmpty(titleInput.getText())
                    || imageUrl == null) {
                Toast.makeText(this, "Please fill all fields first!", Toast.LENGTH_LONG).show();
                return;
            }
            String key = savePost();
            ArrayList<String> tags = new TextAnalyzer(this).getTags(ideaInput.getText().toString() +
                    titleInput.getText().toString());
            Intent expertsSelectionIntent = new Intent(this, AuthoritySelectionActivity.class);
            expertsSelectionIntent.putExtra("POST_KEY", key);
            expertsSelectionIntent.putExtra("POST_TAGS", tags);
            startActivity(expertsSelectionIntent);
        }
    }

    /**
     * Saves the idea post to the backend.
     */
    private String savePost() {
        Post post = new Post.Builder().setAuthor(getString(R.string.current_default_user))
                .setCreationTimestamp("1min ago")
                .setDescription(ideaInput.getText().toString())
                .setTitle(titleInput.getText().toString())
                .setImageUrl(imageUrl).build();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");
        String key = postsRef.push().getKey();
        postsRef.child(key).setValue(post);
        return key;
    }

    /**
     * Handles the result of image selection.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                if (bitmap != null) {
                    imageUrl = imageUri.toString();
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageButton.setImageBitmap(bitmap);
                }
            } catch (OutOfMemoryError ome) {
                Toast.makeText(this, "Please select a smaller image!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                            .READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }
}
