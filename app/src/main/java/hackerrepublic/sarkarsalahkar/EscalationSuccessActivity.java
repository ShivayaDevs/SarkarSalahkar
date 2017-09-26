package hackerrepublic.sarkarsalahkar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Escalates the idea and throws exveptions if idea has failed to escalate due to technical
 * problems.
 */
public class EscalationSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalation_success);

        // to allow the processing to be done in background.
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

        // This is used for debugging purpose, i.e. to show the promoted dialog while making demo
        // video. Won't be used in production.
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("temp");
//
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.exists()) {
//                    showAlertDialog();
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void showAlertDialog() {
        Dialog dialog = new PromotedDIalog(this);
        dialog.show();
    }
}
