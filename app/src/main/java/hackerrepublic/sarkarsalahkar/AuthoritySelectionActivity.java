package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provides the TargetAuthority selection user interface.
 *
 * @author vermayash8
 */
public class AuthoritySelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AuthoritySelectionActivity.class.getSimpleName();

    /**
     * Using an autocomplete textView over here for suggestions.
     * It is compulsory to select a value from the suggestions only.
     */
    private AutoCompleteTextView authorityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_selection);

        authorityEditText = findViewById(R.id.autoCompleteTextView);
        findViewById(R.id.floatingActionButton_escalate).setOnClickListener(this);

        setupEditText();
    }

    /**
     * Adds the adapter to the autocomplete textView.
     */
    void setupEditText() {
        String[] arrayAuthorities = getTestAuthorities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayAuthorities);
        authorityEditText.setAdapter(adapter);
    }


    private String[] getTestAuthorities() {
        //TODO(1): In production add a more target authorities.
        return new String[]{
                "New Delhi Municipal Corporation", "NDMC", "Bombay Municipal Corporation", "MCD",
                "MCF", "Pollution Control Board", "Pollution Institute", "Environment ResearchLab",
                "Finance Department (GOI)", "MCD", "Municipal Corporation", "Railways", "Reserve " +
                "Bank","Water Department"
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.floatingActionButton_escalate) {
            if (!validateData()) {
                Toast.makeText(this, "Please start typing the target authority and select it from" +
                        " suggestions.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = this.getIntent();
            Intent expertSelectionIntent = new Intent(this, ExpertsSelectionActivity.class);
            expertSelectionIntent.putExtra("POST_KEY", intent.getStringExtra("POST_KEY"));
            expertSelectionIntent.putExtra("POST_TAGS", intent.getStringArrayListExtra
                    ("POST_TAGS"));
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
