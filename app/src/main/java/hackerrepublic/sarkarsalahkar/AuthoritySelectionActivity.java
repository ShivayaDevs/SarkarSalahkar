package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;

public class AuthoritySelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AuthoritySelectionActivity.class.getSimpleName();

    private AutoCompleteTextView authorityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_selection);

        authorityEditText = findViewById(R.id.autoCompleteTextView);
        findViewById(R.id.floatingActionButton_escalate).setOnClickListener(this);

        setupEditText();
    }

    void setupEditText() {
        String[] arrayAuthorities = getTestAuthorities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayAuthorities);
        authorityEditText.setAdapter(adapter);
    }

    private String[] getTestAuthorities() {
        //TODO: Add a few more tags.
        return new String[]{
                "New Delhi Municipal Corporation", "NDMC", "Bombay Municipal Corporation", "MCD",
                "MCF", "Pollution Control Board", "Pollution Institute", "Environment ResearchLab",
                "Finance Department (GOI)"
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.floatingActionButton_escalate) {
            if (!validateData())
                return;
            Intent intent = this.getIntent();
            String postId = intent.getStringExtra("POST_KEY");
            ArrayList<String> tags = intent.getStringArrayListExtra("POST_TAGS");

            Intent expertSelectionIntent = new Intent(this, ExpertsSelectionActivity.class);
            expertSelectionIntent.putExtra("POST_KEY", postId);
            expertSelectionIntent.putExtra("POST_TAGS", tags);
            startActivity(expertSelectionIntent);
        }
    }

    /**
     * Either no authority should be specified or if it is specified then it should be from the
     * array.
     *
     * @return if input is valid or not.
     */
    private boolean validateData() {
        String input = authorityEditText.getText().toString();
        return TextUtils.isEmpty(input) || Arrays.asList(getTestAuthorities()).contains(input);
    }
}
