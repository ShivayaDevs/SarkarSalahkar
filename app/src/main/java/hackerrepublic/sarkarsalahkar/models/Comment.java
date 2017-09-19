package hackerrepublic.sarkarsalahkar.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by yash on 19/9/17.
 */

@IgnoreExtraProperties
public class Comment {

    public String commentor;
    public String comment;

    public Comment() {
        // Firebase
    }
}
