package hackerrepublic.sarkarsalahkar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Escalates the idea and throws exveptions if idea has failed to escalate due to technival
 * problems.
 */
public class EscalationSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalation_success);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.pBar).setVisibility(View.GONE);
                findViewById(R.id.mainLayoutt).setVisibility(View.VISIBLE);
            }
        }, 1500);


        findViewById(R.id.floatingActionButton_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(EscalationSuccessActivity.this, HomeActivity.class));
            }
        });
    }
}
